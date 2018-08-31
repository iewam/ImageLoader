package com.spark.skimageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 内存缓存
 */
public class SKMemoryCacheUtils {
    private LruCache<String, Bitmap> mMemoryCache;

    public SKMemoryCacheUtils() {
        //得到手机最大允许内存的1/10,即超过指定内存,则开始回收
        long maxMemory = Runtime.getRuntime().maxMemory() / 10;
        mMemoryCache = new LruCache<String, Bitmap>((int) maxMemory) {
            //在每次存入缓存的时候调用,用于计算每个条目的大小
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getByteCount();
                return byteCount;
            }
        };
    }

    /**
     * 从内存缓存中读取图片
     * @param url 图片的url
     * @return 图片位图
     */
    public Bitmap getBitmapFromMemory(String url) {
        return mMemoryCache.get(url);
    }

    /**
     * 将图片缓存到内存中
     * @param url
     * @param bitmap
     */
    public void setBitmapToMemory(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }

    /**
     * 清除内存缓存
     * @param key
     */
    public void clearMemoryCache(String key) {

        mMemoryCache.remove(key);
    }

    public void cleanMemoryCache() {
        mMemoryCache.evictAll();
    }
}
