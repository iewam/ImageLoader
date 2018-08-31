package com.spark.skimageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SKNetCacheUtils {

    private static String TAG = "ImageLoader";
    private SKMemoryCacheUtils memoryCacheUtils;
    private SKDiskCacheUtils diskCacheUtils;

    public SKNetCacheUtils(SKMemoryCacheUtils memoryCacheUtils, SKDiskCacheUtils diskCacheUtils) {
        this.memoryCacheUtils = memoryCacheUtils;
        this.diskCacheUtils = diskCacheUtils;
    }

    /**
     * 异步加载网络图片
     * @param imageView
     * @param url
     */
    private boolean disCache;
    private boolean memoryCache;
    public void loadImage(ImageView imageView, String url, boolean diskCache, boolean memoryCache) {
        this.disCache = diskCache;
        this.memoryCache = memoryCache;
        new BitmapTask().execute(imageView, url);
    }


    /**
     * AsyncTask就是对handler和线程池的封装
     * 第一个泛型:参数类型
     * 第二个泛型:更新进度的泛型
     * 第三个泛型:onPostExecute的返回结果
     */
    class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView imageView;
        private String url;

        /**
         * 耗时操作，在子线程中
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object[] params) {
            imageView = (ImageView) params[0];
            url = (String)params[1];
            return downloadBitmapWithUrl(url);
        }

        /**
         * 更新进度操作，在主线程中
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         * 下载完成操作，在主线程中
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                if (memoryCache) {
                    memoryCacheUtils.setBitmapToMemory(url, bitmap);
                }
                if (disCache) {
                    diskCacheUtils.setBitmapToDisk(url, bitmap);
                }
            }
        }
    }


    /**
     * 下载图片
     * @param url
     * @return
     */
    private Bitmap downloadBitmapWithUrl(String url) {

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // 压缩图片
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(), null, options);
                return bitmap;
            } else {
                Log.e(TAG, "server error");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "network error");
        }

        return null;
    }


}
