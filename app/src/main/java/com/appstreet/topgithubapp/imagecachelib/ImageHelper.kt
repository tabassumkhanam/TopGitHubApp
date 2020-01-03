package com.appstreet.topgithubapp.imagecachelib

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.ImageView
import com.jakewharton.disklrucache.DiskLruCache
import java.io.BufferedOutputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.lang.ref.WeakReference
import java.net.URL
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ImageHelper constructor(private val context: Context) {

    private var memoryCache: LruCache<String, Bitmap>
    private val DISK_CACHE_SIZE = 1024 * 1024 * 10 // 10MB
    private val IO_BUFFER_SIZE = 8 * 1024
    private val DISK_CACHE_SUBDIR = "thumbnails"
    private var diskLruCache: DiskLruCache? = null
    private val lock = ReentrantLock()
    init {
        // Initializing LRU cache
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, value: Bitmap): Int {
                return value.byteCount / 1024
            }
        }

        //Initializing DiskLRU cache
        val cacheDir = getDiskCacheDir(DISK_CACHE_SUBDIR)
        diskLruCache = DiskLruCache.open(cacheDir, 1, 1, DISK_CACHE_SIZE.toLong())

    }

    /**
     * Load image bitmap in ImageView if stored in cache
     * Else Download the image and store in disk and memory cache
     * @param urlPath - image url
     * @param imageView - ImageView in which image needs to load
     * @param placeholderRes - Default place holder image resource
     */
    fun loadBitmap(urlPath: String, imageView: ImageView, placeholderRes: Int) {
        //val key = urlPath.substringAfterLast("/").toLowerCase().substringBefore(".")
        val key = urlPath.substringAfterLast("/").toLowerCase()
        imageView.tag = key
        var bitmap: Bitmap?
        bitmap = getBitmapFromMemCache(key)
        if (bitmap == null) {
            bitmap = getBitmapFromDiskCache(key)
            if (bitmap == null) {
                bitmapWorkerTask(urlPath, imageView, placeholderRes)
            } else {
                addBitmapToMemoryCache(key, bitmap)
                if (imageView.tag == key) imageView.setImageBitmap(bitmap)
            }
        } else {
            if (imageView.tag == key) imageView.setImageBitmap(bitmap)
        }
    }

    /**
     * Get the directory of disk cache
     */
    private fun getDiskCacheDir(uniqueName: String): File {
        val path = context.cacheDir.path
        return File(path + File.separator + uniqueName)
    }

    /**
     * Get bitmap from Memory cache if stored
     * @param key - Key name of bitmap
     */
    private fun getBitmapFromMemCache(key: String): Bitmap? =
        lock.withLock {
            return memoryCache.get(key)
        }


    /**
     * Get bitmap from Disk cache if stored
     * @param key - Key name of bitmap
     */
    private fun getBitmapFromDiskCache(key: String): Bitmap? =
        lock.withLock {
            val snapshot = diskLruCache?.get(key)
            val inputStream = snapshot?.getInputStream(0)
            return BitmapFactory.decodeStream(inputStream)
        }

    /**
     * Download image and keep in memory and disk cache
     * @param urlPath - image url
     * @param imageView - ImageView in which image needs to load
     * @param placeholderRes - Default place holder image resource
     */
    private fun bitmapWorkerTask(urlPath: String, imageView: ImageView, placeholderRes: Int) {

        val weakReference = WeakReference(imageView)
        weakReference.get()?.setImageResource(placeholderRes)
        AppExecutors.getInstance().networkIO().execute {
            var stream: InputStream? = null
            try {
                stream = URL(urlPath).openConnection().getInputStream()
                if (stream != null) {

                    val options = BitmapFactory.Options().apply {
                        inJustDecodeBounds = true
                        BitmapFactory.decodeStream(stream, null, this)
                        inSampleSize = calculateInSampleSize(this, 120, 120)
                        inJustDecodeBounds = false
                    }
                    stream.close()
                    stream = URL(urlPath).openConnection().getInputStream()
                    val bitmap = BitmapFactory.decodeStream(stream, null, options)

                    if (bitmap != null) {
                        val key = urlPath.substringAfterLast("/").toLowerCase()
                        addBitmapToCache(key, bitmap)

                        AppExecutors.getInstance().mainThread().execute {
                            if (weakReference.get()?.tag == key) weakReference.get()?.setImageBitmap(bitmap)

                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                stream?.close()
            }
        }
    }

    private fun addBitmapToCache(key: String, bitmap: Bitmap) {
        // Add to memory cache as before
        if (getBitmapFromMemCache(key) == null) {
            addBitmapToMemoryCache(key, bitmap)
        }
        addBitmapToDiskCache(key, bitmap)
    }

    /**
     * Add bitmap to memory cache
     * @param key - key value of bitmap
     * @param bitmap - Bitmap of image
     */
    @Synchronized
    private fun addBitmapToMemoryCache(key: String, bitmap: Bitmap) {
            memoryCache.put(key, bitmap)
    }

    /**
     * Add bitmap to disk cache
     * @param key - key value of bitmap
     * @param bitmap - Bitmap of image
     */
    private fun addBitmapToDiskCache(key: String, bitmap: Bitmap) {
        synchronized(lock) {
            diskLruCache?.apply {
                val editor = edit(key)
                var out: OutputStream? = null
                try {
                    out = BufferedOutputStream(editor.newOutputStream(0), IO_BUFFER_SIZE)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, out)
                    diskLruCache?.flush()
                    editor?.commit()
                } finally {
                    out?.close()
                    //editor?.abort()
                }
            }
        }
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}