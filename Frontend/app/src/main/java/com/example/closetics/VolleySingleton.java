package com.example.closetics;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


/**
 * Helper class used to send HTTP requests.
 * Uses Volley library.
 */
public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx;

    /**
     * Private Constructor.
     * Should not be called directly (and therefore is private).
     *
     * @param context current application context
     */
    private VolleySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    /**
     * Returns the instance of the VolleySingleton class.
     * Creates a new instance only if not initialized before
     * (so only on the first call).
     *
     * @param context current application context
     * @return The only instance of VolleySingleton class
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    /**
     * Returns the current instance of the HTTP request queue.
     * Creates a new instance only if not initialized before
     * (so only on the first call).
     *
     * @return The only instance of RequestQueue class
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Adds given request to the current HTTP request queue.
     *
     * @param req request to add
     * @param <T> generic type of the request
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * Returns the current instance of the image loader.
     *
     * @return The only instance of ImageLoader
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
