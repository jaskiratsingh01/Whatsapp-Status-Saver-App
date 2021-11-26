package com.example.whatsappstatussaver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class Video extends AppCompatActivity {


    ImageView backBtn, downloadBtn, shareBtn;
    VideoView mainVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        getSupportActionBar().setTitle("Video");


        backBtn = findViewById(R.id.back_btn_vid);
        downloadBtn = findViewById(R.id.download_btn_vid);
        shareBtn = findViewById(R.id.share_btn_vid);
        mainVideo = findViewById(R.id.particular_video);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Intent  intent = getIntent();
        String destPath = intent.getStringExtra("DEST_PATH_VIDEO");
        String filePath = intent.getStringExtra("FILE_PATH");
        String uri = intent.getStringExtra("URI_VIDEO");
        String fileName = intent.getStringExtra("FILENAME_VIDEO");

        File destPathDir = new File(destPath);
        File file = new File(filePath);


        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(mainVideo);

        mainVideo.setMediaController(mediaController);
        mainVideo.setVideoURI(Uri.fromFile(file));
        mainVideo.requestFocus();
        mainVideo.start();





        //Checking if file is already Saved or not
        boolean filePresent = new File(destPath, fileName).exists();



        if(filePresent)
        {
            downloadBtn.setImageResource(0);
            downloadBtn.setImageResource(R.drawable.ic_baseline_check_circle_24);
        }




        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(filePresent)
                {
                    Toast.makeText(getApplicationContext(),"Already Saved!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        org.apache.commons.io.FileUtils.copyFileToDirectory(file,destPathDir);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }


                    MediaScannerConnection.scanFile(getApplicationContext(),
                            new String[]{destPath + fileName},
                            new String[]{"*/*"},
                            new MediaScannerConnection.MediaScannerConnectionClient() {
                                @Override
                                public void onMediaScannerConnected() {

                                }

                                @Override
                                public void onScanCompleted(String path, Uri uri) {

                                }
                            });


                    Dialog dialog = new Dialog(Video.this);
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.show();

                    Button button = dialog.findViewById(R.id.okbutton);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(),"Saved!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        }
                    });
                }

            }
        });




        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setType("video/mp4");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Video");
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
                sendIntent.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(sendIntent, "Share Video:"));

            }
        });


    }
}