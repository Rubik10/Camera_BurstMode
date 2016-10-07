package com.rubik.appcameraburst.main.Fragment_Recycler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import com.rubik.appcameraburst.MainActivity;
import com.rubik.appcameraburst.R;
import com.rubik.appcameraburst.main.ToolbalActionMode_Callback;
import com.rubik.appcameraburst.main.server.UploadData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rubik on 7/10/16.
 */

public class RecyclerView_Fragment extends Fragment {

    private static View view;
    private static RecyclerView recyclerView;
    private static List<File> listFiles;
    private static MyGalleryRecyclerAdapter myAdapter;
    private ActionMode mActionMode;

    public RecyclerView_Fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gallery_fragment, container, false);
        listFiles = MainActivity.listFiles;
        initRecyclerView();
        implementRecyclerViewClickListeners();
        return view;
    }


    private void initRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewGallery);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayout.VERTICAL,false));

        myAdapter = new MyGalleryRecyclerAdapter(getActivity(), listFiles);
        recyclerView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    //Implement item click and long click over recycler view
    private void implementRecyclerViewClickListeners() {
        recyclerView.addOnItemTouchListener(new MyItemTouchListener(getActivity(), recyclerView, new MyItemOnToucRecycler_Listener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                if (mActionMode != null)
                    onListItemSelect(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                onListItemSelect(position);
            }
        }));
    }

    //List item select method
    private void onListItemSelect(int position) {
        myAdapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = myAdapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null)
                // there are some selected items, start the actionMode
            mActionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(new ToolbalActionMode_Callback (
                    getActivity(),myAdapter,listFiles));

        else if (!hasCheckedItems && mActionMode != null)
                // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null)
                //set action mode title on item selection
            mActionMode.setTitle(String.valueOf(myAdapter.getSelectedCount()) + " selected");


    }
    //Set action mode null after use
    public void setNullToActionMode() {
        if (mActionMode != null) mActionMode = null;
    }

    //Delete selected rows
    public void deleteRows() {
        SparseBooleanArray selected = myAdapter.getSelectedIds();//Get selected ids

            //Loop all selected ids
        for (int i = (selected.size() - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                    //If current id is selected remove the item via key
                MainActivity.listFiles.remove(selected.keyAt(i));
                myAdapter.notifyDataSetChanged();//notify adapter
            }
        }
        Toast.makeText(getActivity(), selected.size() + " item deleted.", Toast.LENGTH_SHORT).show();//Show Toast
        mActionMode.finish();//Finish action mode after use
    }

    public void shareImages(ArrayList<Uri> imageUris) {
         imageUris = new ArrayList<Uri>();
            //Get selected ids on basis of current fragment action mode
        SparseBooleanArray selected = myAdapter.getSelectedIds();//Get selected ids
        int selectedSize = selected.size();

            //Loop to all selected items
        for (int i = (selectedSize - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                    //get selected pic
                File file = listFiles.get(selected.keyAt(i));
                String title = file.getPath();
                imageUris.add(Uri.fromFile(file));

                    //Print the data to show if its working properly or not
                Log.e("Selected Items", "Title - " + title + "\n" + "Name - " + file.getName());
            }
        }
        mActionMode.finish();//Finish action mode after use
    }


    public void uploadImages () {
        Log.d("Selected Items", "upload images");

        SparseBooleanArray selected = myAdapter.getSelectedIds();//Get selected ids
        int selectedSize = selected.size();

            // Get the Bitmap for uploading
        for (int i = (selectedSize - 1); i >= 0; i--) {
            if (selected.valueAt(i)) {
                File file = listFiles.get(selected.keyAt(i));
                String name = file.getName();
                    Log.d("Selected Items", file.getPath());
               Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                UploadData.uploadImage(getContext(), bitmap, name);
            }
        }

        mActionMode.finish();//Finish action mode after use
    }





}
