package com.spark.skimageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SKDiskCacheUtils {

    private static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cn.spark.image";

    public SKDiskCacheUtils() {

    }

    /**
     * 从本地读取图片位图
     *
     * @param url
     * @return
     */
    public Bitmap getBitmapFromDisk(String url) {
        String filename = null;
        filename = SKMD5Utils.MD5Code(url);
        File file = new File(CACHE_PATH, filename);
        if (file.exists()) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
        return null;
    }

    /**
     * 缓存到本地
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToDisk(String url, Bitmap bitmap) {
        String filename = SKMD5Utils.MD5Code(url);
        try {
            File file = new File(CACHE_PATH, filename);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdir();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static final String TAG = "ImageLoader";

    public void clearDiskCache(String url) {
        String filename = SKMD5Utils.MD5Code(url);
        File file = new File(CACHE_PATH, filename);
        if (file.exists()) {
            deleteFile(file);
        }
    }

    public void cleanDiskCache() {
        File file = new File(CACHE_PATH);
        if (file.exists()) {
            deleteFile(file);
            Log.e(TAG, "delete file " + file.getAbsolutePath());
        }
    }

    private void deleteFile(File f) {
        if (f.isDirectory()) {
            for (File file : f.listFiles()) {
               deleteFile(file);
            }
        } else {
            f.delete();
        }
    }

}
