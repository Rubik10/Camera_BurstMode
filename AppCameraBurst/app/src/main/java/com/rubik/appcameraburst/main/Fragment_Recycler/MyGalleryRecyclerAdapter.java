package com.rubik.appcameraburst.main.Fragment_Recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rubik.appcameraburst.R;

import java.io.File;
import java.util.List;

/**
 * Created by Rubik on 7/10/16.
 */

public class MyGalleryRecyclerAdapter extends RecyclerView.Adapter<MyGalleryRecyclerAdapter.ViewHolder> {

    private List<File> listFiles;
    private Context context;
    private SparseBooleanArray selectedItemsIds;

    public MyGalleryRecyclerAdapter(Context context, List<File> listFiles) {
        this.context = context;
        this.listFiles = listFiles;
        selectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup viewGroup, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(mainGroup);

    }

    @Override
    public int getItemCount() {return (null != listFiles ? listFiles.size() : 0);}

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final File file = listFiles.get(position);
        String name = file.getName();
            Log.d("Adapter", name);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

            Log.d("Adapter", file.getPath());
        holder.imageView.setImageBitmap(bitmap);
        holder.title.setText(name);

        /** Change background color of the selected items in list view  **/
        holder.itemView.setBackgroundColor(selectedItemsIds.get(position) ? 0x9934B5E4 : Color.TRANSPARENT);
    }

    /***
     * Methods required for do selections, remove selections, etc.
     */

    //Toggle selection methods
    public void toggleSelection(int position) {
        selectView(position, !selectedItemsIds.get(position));
    }

        //Remove selected selections
    public void removeSelection() {
        selectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }



        //Put or delete selected position into SparseBooleanArray
    public void selectView(int position, boolean value) {
        if (value)
            selectedItemsIds.put(position, value);
        else
            selectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    //Get total selected count
    public int getSelectedCount() {
        return selectedItemsIds.size();
    }

    //Return all selected ids
    public SparseBooleanArray getSelectedIds() {
        return selectedItemsIds;
    }



     class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView title;

         ViewHolder(View view) {
            super(view);

            this.title = (TextView) view.findViewById(R.id.title3);
            this.imageView = (ImageView) view.findViewById(R.id.image_view);
        }
    }


}
