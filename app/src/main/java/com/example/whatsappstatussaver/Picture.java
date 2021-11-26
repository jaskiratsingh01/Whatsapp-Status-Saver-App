package com.example.whatsappstatussaver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Picture extends AppCompatActivity {


    ImageView mainImg, backBtn, downloadBtn, shareBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);


        getSupportActionBar().setTitle("Picture");


        mainImg = findViewById(R.id.particular_image);
        backBtn = findViewById(R.id.back_btn_pic);
        downloadBtn = findViewById(R.id.download_btn_pic);
        shareBtn = findViewById(R.id.share_btn_pic);



        Zoomy.Builder builder = new Zoomy.Builder(this)
                .target(mainImg)
                .animateZooming(false)
                .enableImmersiveMode(false)
                .tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {
                        //When mainImg is clicked ----- code here
                    }
                });
        builder.register();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        Intent intent = getIntent();
        String destPath = intent.getStringExtra("DEST_PATH_PICTURE");
        String filePath = intent.getStringExtra("FILE_PATH");
        String uri = intent.getStringExtra("URI_PICTURE");
        String fileName = intent.getStringExtra("FILENAME_PICTURE");


        File destPathDir = new File(destPath);
        File file = new File(filePath);

        Glide.with(getApplicationContext()).load(uri).into(mainImg);



        //Checking if file is already Saved or not
        boolean filePresent = new File(destPath, fileName).exists();



        if(filePresent)
        {
            downloadBtn.setImageResource(0);
            downloadBtn.setImageResource(R.drawable.ic_baseline_check_circle_24);
        }


        boolean finalFilePresent = filePresent;
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YoYo.with(Techniques.FlipInX).duration(600).repeat(0).playOn(downloadBtn);


                if(finalFilePresent)
                {
                    Toast.makeText(getApplicationContext(),"Already Saved!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        org.apache.commons.io.FileUtils.copyFileToDirectory(file, destPathDir);
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

                    Dialog dialog = new Dialog(Picture.this);
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.show();

                    Button button = dialog.findViewById(R.id.okbutton);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "Saved!",Toast.LENGTH_SHORT).show();
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
                YoYo.with(Techniques.FlipInX).duration(600).repeat(0).playOn(shareBtn);


                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                BitmapDrawable bitmapDrawable = (BitmapDrawable)mainImg.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                Intent shareIntent;

                try{
                    FileOutputStream outputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                    outputStream.flush();
                    outputStream.close();

                    shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }

                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        });


    }
}