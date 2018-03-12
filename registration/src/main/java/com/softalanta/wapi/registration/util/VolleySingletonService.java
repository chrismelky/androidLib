package com.softalanta.wapi.registration.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Singleton implementation of Volley Http Request
 */

public class VolleySingletonService {

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static VolleySingletonService mInstance;
    private static Context mContext;

    private VolleySingletonService(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                 cache.put(url,bitmap);
            }
        });
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return  mRequestQueue;
    }

    public static synchronized VolleySingletonService getInstance(Context context){
        if(mInstance == null){
            mInstance = new VolleySingletonService(context);
        }
        return mInstance;
    }

    public <T> void addToRequestToQueue(Request<T> request){
        mRequestQueue.add(request);
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }



}
