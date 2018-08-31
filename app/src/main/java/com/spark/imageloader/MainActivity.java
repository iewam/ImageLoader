package com.spark.imageloader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.spark.skimageloader.SKImageLoader;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Switch diskSwitch;
    private Switch memorySwitch;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.id_imageview);
        diskSwitch = (Switch) findViewById(R.id.id_diskSwitch);
        memorySwitch = (Switch) findViewById(R.id.id_memorySwitch);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private static final String img_url = "http://img06.tooopen.com/images/20161112/tooopen_sy_185726882764.jpg";

    public void loadImage(View view) {
        SKImageLoader.getInstance(this).loadImage(imageView, img_url, diskSwitch.isChecked(), memorySwitch.isChecked());
    }

    public void clearMemoryCache(View view) {
        imageView.setImageResource(R.drawable.empty);
        SKImageLoader.getInstance(this).cleanMemoryCache();
    }

    public void clearDiskCache(View view) {
        imageView.setImageResource(R.drawable.empty);
        SKImageLoader.getInstance(this).cleanDiskCache();
    }
}
