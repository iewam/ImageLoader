package com.spark.skimageloader;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class SKImageLoader {

    private static String TAG = "ImageLoader";
    private static SKImageLoader instance = null;
    private SKMemoryCacheUtils memoryCacheUtils;
    private SKDiskCacheUtils diskCacheUtils;
    private SKNetCacheUtils netCacheUtils;
    private Context currentContext;

    public static SKImageLoader getInstance(Context context) {
        if (instance == null) {
            synchronized (SKImageLoader.class) {
                if (instance == null) {
                    instance = new SKImageLoader();
                }
            }
        }
        instance.currentContext = context;
        return instance;
    }

    private SKImageLoader() {
        memoryCacheUtils = new SKMemoryCacheUtils();
        diskCacheUtils = new SKDiskCacheUtils();
        netCacheUtils = new SKNetCacheUtils(memoryCacheUtils, diskCacheUtils);
    }


    /**
     * 加载图片
     *
     * @param imageView
     * @param url
     */
    public void loadImage(ImageView imageView, String url) {
        loadImage(imageView, url, true, true);
    }

    private boolean diskCache;
    private boolean memoryCache;
    public void loadImage(ImageView imageView, String url, boolean diskCache, boolean memoryCache) {
        this.diskCache = diskCache;
        this.memoryCache =  memoryCache;

        imageView.setImageResource(R.drawable.empty);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Bitmap bitmap = null;
        if (memoryCache) {
            bitmap = memoryCacheUtils.getBitmapFromMemory(url);
        }
        if (bitmap != null) {
            Log.e(TAG, "load from memory cache");
            toastMsg("load from memory cache");
            imageView.setImageBitmap(bitmap);
            return;
        }

        if (diskCache) {
            bitmap = diskCacheUtils.getBitmapFromDisk(url);
        }
        if (bitmap != null) {
            Log.e(TAG, "load from disk cache");
            toastMsg("load from disk cache");
            imageView.setImageBitmap(bitmap);
            return;
        }

        Log.e(TAG, "load from net work");
        toastMsg("load from net work");
        netCacheUtils.loadImage(imageView, url, diskCache, memoryCache);
    }


    public void clearMemoryCache(String key) {
        if (memoryCache) {
            memoryCacheUtils.clearMemoryCache(key);
            Log.e(TAG, "memory cache clear complete");
            toastMsg("memory cache clear complete");

        } else {
            Log.e(TAG, "memory cache is unable");
            toastMsg("memory cache is unable");
        }
    }

    public void cleanMemoryCache() {
        if (memoryCache) {
            memoryCacheUtils.cleanMemoryCache();
            Log.e(TAG, "memory cache clean complete");
            toastMsg("memory cache clean complete");
        } else {
            Log.e(TAG, "memory cache is unable");
            toastMsg("memory cache is unable");

        }
    }

    public void clearDiskCache(String url) {
        if (diskCache) {
            diskCacheUtils.clearDiskCache(url);
            Log.e(TAG, "disk cache clear complete");
            toastMsg("disk cache clear complete");
        } else {
            Log.e(TAG, "disk cache is unable");
            toastMsg("disk cache is unable");
        }
    }

    public void cleanDiskCache() {
        if (diskCache) {
            diskCacheUtils.cleanDiskCache();
            Log.e(TAG, "disk cache clean complete");
            toastMsg("disk cache clean complete");
        } else {
            Log.e(TAG, "disk cache is unable");
            toastMsg("disk cache is unable");
        }
    }


    private void toastMsg(String msg) {
        Toast.makeText(currentContext, msg, Toast.LENGTH_SHORT).show();
    }
}
