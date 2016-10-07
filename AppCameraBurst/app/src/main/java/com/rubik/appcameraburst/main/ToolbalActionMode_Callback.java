package com.rubik.appcameraburst.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.rubik.appcameraburst.GalleryShareActivity;
import com.rubik.appcameraburst.R;
import com.rubik.appcameraburst.main.Fragment_Recycler.MyGalleryRecyclerAdapter;
import com.rubik.appcameraburst.main.Fragment_Recycler.RecyclerView_Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rubik on 7/10/16.
 */

public class ToolbalActionMode_Callback implements ActionMode.Callback {

    private Context context;
    private MyGalleryRecyclerAdapter rAdapter;
    private List<File> listFile;

    public ToolbalActionMode_Callback(Context context, MyGalleryRecyclerAdapter recyclerAdapter, List<File> listFile) {
        this.context = context;
        this.rAdapter = recyclerAdapter;
        this.listFile = listFile;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_main, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        //Sometimes the meu will not be visible so for that we need to set their visibility manually in this method
        //So here show action menu according to SDK Levels
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_delete), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_share), MenuItemCompat.SHOW_AS_ACTION_NEVER);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.action_upload), MenuItemCompat.SHOW_AS_ACTION_NEVER);
        } else {
            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_share).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_upload).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Fragment recyclerFragment = new GalleryShareActivity().getFragment(0);//Get recycler view fragment

        switch (item.getItemId()) {
            case R.id.action_delete:
                if (recyclerFragment != null)
                    ((RecyclerView_Fragment) recyclerFragment).deleteRows();    //delete selected rows
                break;

            case R.id.action_share:

                ArrayList<Uri> imageUris = new ArrayList<Uri>();
                 ((RecyclerView_Fragment) recyclerFragment).shareImages(imageUris);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                shareIntent.setType("image/*"); //modificar el tipo MIME por si desean compatir otro tipo de archivos
                context.startActivity(Intent.createChooser(shareIntent, "Compatir con.."));

                mode.finish();//Finish action mode
                break;

            case R.id.action_upload:
                ((RecyclerView_Fragment) recyclerFragment).uploadImages();  //Upload selected images
                break;
        }
        return false;
    }



    @Override
    public void onDestroyActionMode(ActionMode mode) {

        //When action mode destroyed remove selected selections and set action mode to null
            rAdapter.removeSelection();  // remove selection
            Fragment recyclerFragment = new GalleryShareActivity().getFragment(0);//Get recycler fragment
            if (recyclerFragment != null)
                ((RecyclerView_Fragment) recyclerFragment).setNullToActionMode();//Set action mode null
    }



}
