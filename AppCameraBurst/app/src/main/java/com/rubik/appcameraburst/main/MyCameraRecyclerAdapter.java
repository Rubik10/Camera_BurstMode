package com.rubik.appcameraburst.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rubik.appcameraburst.R;

import java.io.File;
import java.util.List;

/**
 * Created by Rubik on 7/10/16.
 */

public class MyCameraRecyclerAdapter  extends RecyclerView.Adapter<MyCameraRecyclerAdapter.MyViewHolder>{
    private Context context;
    public List<File> listImages;

    public MyCameraRecyclerAdapter (Context cxt, List<File> list ) {
        this.context = cxt;
        this.listImages = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.minigalery_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {return listImages.size();}

    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final File file = listImages.get(position);
        String name = file.getName();
            Log.d("Adapter", name);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

            Log.d("Adapter", file.getPath());
        holder.imageView.setImageBitmap(bitmap);
      //  holder.tittle.setText(name);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
       // private TextView tittle;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageview2);
          //  tittle = (TextView) view.findViewById(R.id.title2);
        }

    }
}

