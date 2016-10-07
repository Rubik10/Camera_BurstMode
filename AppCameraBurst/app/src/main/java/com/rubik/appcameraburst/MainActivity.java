package com.rubik.appcameraburst;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rubik.appcameraburst.main.MyCameraRecyclerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private static final String TAG = "Camera Loop Picture";
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private Camera mCamera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private Button buttonCapture;
    private Button butonView;
    private TextView textCount;

    private RecyclerView recyclerview;
    private MyCameraRecyclerAdapter myAdapter;

    private static final int CAMERA_ID = 1;
    private long mDelay;
    private boolean isTakingPictures = false;
    public static List<File> listFiles;

    private boolean isCheckedPermission = false;
    private static int countPics = 0;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context=this;
        Log.d(TAG, "onCreate");

        if (!isCheckedPermission) {
            Log.d(TAG, "permission NOT checked");
            checksCameraPermission();
        }

        Log.d(TAG, "permission checked");
        listFiles = new ArrayList<>();
        initControls();
        initRecycleView();
        initSurface();

    }

    private void initControls() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        buttonCapture = (Button) findViewById(R.id.buttonCapture);
        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCapture.setText("Stop");
                takePictures(100);
            }
        });

        butonView = (Button) findViewById(R.id.button2);
        butonView.setOnClickListener(startGallery);
        textCount = (TextView) findViewById(R.id.txtNumber);
    }

    private void initSurface() {
        Log.d(TAG, "initSurface");
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    private void initRecycleView () {
        recyclerview = (RecyclerView) findViewById(R.id.recycler_view);
        myAdapter = new MyCameraRecyclerAdapter(this,listFiles);

        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 1, GridLayout.HORIZONTAL,false));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
    }


    public void takePictures(long delay) {
        if (!isTakingPictures) {
            mDelay = delay;
            isTakingPictures = true;
            countPics=0;
            butonView.setVisibility(View.INVISIBLE);

            clearListFile();
            initBurstMode();
        } else {
            stopBurstMode();
            buttonCapture.setText("Start");
            butonView.setVisibility(View.VISIBLE);
            Log.d(TAG, String.valueOf(listFiles.size()));
            textCount.setText(String.valueOf(listFiles.size())  + " pics");
            updateRecycleView();
        }
    }

    Button.OnClickListener startGallery = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO : Cargarlo con un asyncktask
            final ProgressDialog dialog = ProgressDialog.show(context, "Loading...", "loading gallery", false, false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), GalleryShareActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            },2000);
        }
    };

    private void startCamera() {
        Log.d(TAG, "startCamera");
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;

        } else {*/
        mCamera = Camera.open(CAMERA_ID); //Integer.valueOf(cameraId)
        // }
    }

    private void stopCamera() {
        stopBurstMode();
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;

            surfaceHolder.removeCallback(this);
            surfaceHolder = null;
        }
    }

    protected void createCameraPreview(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initBurstMode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isTakingPictures) {return;}
                    Thread.sleep(mDelay);
                    if (!isTakingPictures) {return;}

                    mCamera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            Log.d(TAG, "Picture Callback");
                            try {
                                saveImage(data);
                                countPics ++;
                                textCount.setText(String.valueOf(countPics) + " pics");
                            } catch (IOException e) { e.printStackTrace();
                            } finally {
                                mCamera.startPreview();
                                initBurstMode();
                            }
                        }
                    });

                } catch (InterruptedException e) {e.printStackTrace();}
            }
        }).start();

    }

    private void stopBurstMode() {
        isTakingPictures = false;
    }




    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Rubik_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        Log.d(TAG, imageFile.getAbsolutePath());
        return imageFile;
    }

    private void saveImage (byte[] data) throws IOException {
        // String.valueOf(TimeStamp.getTime());
         /* String str = String.format(
                                        "%s/%09d.jpg",directory.getAbsoluteFile().toString(), TimeStamp.getTime());*/

       /* Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            Log.d("Bitmap", String.valueOf(bitmap.getByteCount()));*/

        File file = createImageFile();
        // MediaStore.Images.Media.insertImage(getContentResolver(), file.getPath(), file.getName()  , "");

        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(data);
        outStream.close();

        listFiles.add(file);
        Log.d(TAG, "imagen guardada");
    }


    private void clearListFile () {
        listFiles.clear();
    }

    private void updateRecycleView () {
        recyclerview.setAdapter(myAdapter);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        if (isCheckedPermission) {
            startCamera();
            createCameraPreview(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
        if (isCheckedPermission) {
            mCamera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopCamera();
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        if (isCheckedPermission) {
            if (surfaceView.isActivated()) { //surfaceView.isShown() &&
                startCamera();
            } else {
                clearListFile();
                updateRecycleView();
                initSurface();
                textCount.setText("0 pics");
                butonView.setVisibility(View.INVISIBLE);
            }
        } else {
            return;
        }


    }


    public void checksCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("MyApp", "SDK >= 23");
            if  (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Request permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);

                if (! shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    showMessageOKCancel("You need to allow camera usage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                                }
                            });
                }

            } else {
                Log.d(TAG, "Permission granted: taking pic");
                isCheckedPermission=true;
            }
        }
        else {
            Log.d(TAG, "Android < 6.0");
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionsResult");

        switch(requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isCheckedPermission=true;
                    startCamera();
                    createCameraPreview(surfaceHolder);
                } else {
                    Toast.makeText(this, "You did not allow camera usage :(", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onRequestPermissionsResult -> Error");
                }
                return;
        }
    }


}
