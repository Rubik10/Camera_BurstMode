package com.rubik.appcameraburst.main.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

/**
     * Created by Rubik on 21/9/16.
 */

public class UploadData {

    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";

    private Context context;
    //private static ProgressDialog loading;

    public UploadData(Context context) {
        this.context = context;
    }


    public static void uploadImage (final Context context, final Bitmap bitmap, final String nameImage){
             //Showing the progress dialog
       //  loading = ProgressDialog.show(context,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                AppCongif.UPLOAD_URL,
                onVolleyResponse(context),
                onVolleyError(context)
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                    //Converting Bitmap to String
                String image = getStringImage(bitmap);
                    //Getting Image Name
                String name = nameImage;
                    //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();
                    //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);
                    //returning parameters
                return params;
            }
        };
            //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
            //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private static Response.Listener<String> onVolleyResponse(final Context context) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               //  loading.dismiss();  //Disimissing the progress dialog
                    //Showing toast message of the response
                Log.d(UploadData.class.getSimpleName(), "Image upload succesfully");
                Toast.makeText(context, response , Toast.LENGTH_LONG).show();
            }
        };

    }

    private static Response.ErrorListener onVolleyError(final Context context) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
              //  loading.dismiss();  //Dismissing the progress dialog
                    //Showing toast
                Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Volley error -> ", volleyError.getMessage());
            }
        };
    }

    private static String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }

}
