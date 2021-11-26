package com.example.whatsappstatussaver;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterRV extends RecyclerView.Adapter<AdapterRV.MyViewHolder> {



    Context context;
    ArrayList<ModelClass> fileslist;

    public AdapterRV(Context context, ArrayList<ModelClass> fileslist) {
        this.context = context;
        this.fileslist = fileslist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,null,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final ModelClass modelClass = fileslist.get(position);
        if(modelClass.getUri().toString().endsWith(".mp4"))
        {
            holder.play.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.play.setVisibility(View.INVISIBLE);
        }
        Glide.with(context).load(modelClass.getUri()).into(holder.mainStatus);



        holder.mainStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(modelClass.getUri().toString().endsWith(".mp4"))
                {
                    final String path = fileslist.get(position).getPath();
                    String destPath = Environment.getExternalStorageDirectory().getAbsolutePath()+Constant.SAVE_FOLDER_NAME;

                    Intent intent = new Intent(context, Video.class);
                    intent.putExtra("DEST_PATH_VIDEO",destPath);
                    intent.putExtra("FILE_PATH",path);
                    intent.putExtra("FILENAME_VIDEO",modelClass.getFileName());
                    intent.putExtra("URI_VIDEO",modelClass.getUri().toString());
                    context.startActivity(intent);
                }
                else
                {
                    final String path = fileslist.get(position).getPath();
                    String destPath = Environment.getExternalStorageDirectory().getAbsolutePath()+Constant.SAVE_FOLDER_NAME;

                    Intent intent = new Intent(context,Picture.class);
                    intent.putExtra("DEST_PATH_PICTURE",destPath);
                    intent.putExtra("FILE_PATH",path);
                    intent.putExtra("FILENAME_PICTURE",modelClass.getFileName());
                    intent.putExtra("URI_PICTURE",modelClass.getUri().toString());
                    context.startActivity(intent);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return fileslist.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView mainStatus, play;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mainStatus = itemView.findViewById(R.id.thumbnail_of_status);
            play = itemView.findViewById(R.id.play);

        }
    }

}
