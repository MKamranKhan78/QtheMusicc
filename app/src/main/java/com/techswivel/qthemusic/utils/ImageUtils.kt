package com.techswivel.qthemusic.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Created by Anatol on 11/12/2016.
 */

object ImageUtils {

    private val BASE_IMAGE_NAME = "i_prefix_"

    fun savePicture(context: Context, bitmap: Bitmap, imageSuffix: String): String {
        val savedImage = getTemporalFile(context, "$imageSuffix.jpeg")
        var fos: FileOutputStream? = null
        if (savedImage.exists()) {
            savedImage.delete()
        }
        try {
            fos = FileOutputStream(savedImage.path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

        return savedImage.absolutePath
    }

    fun getTemporalFile(context: Context, payload: String): File {
        return File(context.externalCacheDir, BASE_IMAGE_NAME + payload)
    }

    @SuppressLint("NewApi")
    fun getBitmapFromUri(selectedPhotoUri: Uri, context: Context): Bitmap? {
        val bitmap = when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                selectedPhotoUri
            )
            else -> {
                val source =
                    context.contentResolver?.let { ImageDecoder.createSource(it, selectedPhotoUri) }
                source?.let { ImageDecoder.decodeBitmap(it) }
            }
        }
        return bitmap
    }
}
