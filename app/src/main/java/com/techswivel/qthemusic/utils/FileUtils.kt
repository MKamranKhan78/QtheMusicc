package com.techswivel.qthemusic.utils

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.util.Base64
import com.techswivel.qthemusic.application.QTheMusicApplication
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object FileUtils {
    private const val TAG = "FileUtils"

    fun createImageFile(context: Context): File? {
        val timeStamp = Calendar.getInstance().timeInMillis
        val imageFileName = "Image$timeStamp"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image: File? = null
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return image
    }

    fun getGooglePhotosUri(context: Context, uri: Uri): String? {
        if (isNewGooglePhotosUri(uri)) {
            val pathUri = uri.path
            val newUri =
                pathUri!!.substring(pathUri.indexOf("content"), pathUri.lastIndexOf("/ACTUAL"))
            return getDataColumn(context, Uri.parse(newUri), null, null)
        }
        return null
    }

    fun isNewGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.contentprovider" == uri.authority
    }

    fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    fun getEncoded64ImageStringFromBitmap(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        val byteFormat = stream.toByteArray()
        // get the base 64 string
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP)
    }

    fun getPathFromURI(contentUri: Uri, activity: Activity): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.MediaColumns.DATA)
        val cursor = activity!!.contentResolver.query(contentUri, proj, null, null, null)
        if (cursor!!.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }

    fun getSavedImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    data class Video(
        val uri: Uri,
        val name: String,
        val duration: Int,
        val size: Int
    )

    private fun getVideoDataFromContentResolver(): MutableList<Video> {
        val videoList = mutableListOf<Video>()
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE
        )
// Show only videos that are at least 5 minutes in duration.
        val selection = "${MediaStore.Video.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(
            TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS).toString()
        )
// Display videos in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
        val query = QTheMusicApplication.getContext().contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val durationColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                // Stores column values and the contentUri in a local object
                // that represents the media file.
                videoList += Video(contentUri, name, duration, size)
                for (data in videoList) {
                    Log.e(TAG, "uri = ".plus(data.uri))
                }
            }
        }
        return videoList
    }

    fun decodeImageFromFiles(path: String, width: Int, height: Int): Bitmap {
        val scaleOptions = BitmapFactory.Options()
        scaleOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, scaleOptions)
        var scale = 1
        while (scaleOptions.outWidth / scale / 2 >= width && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2
        }
        // decode with the sample size
        val outOptions = BitmapFactory.Options()
        outOptions.inSampleSize = scale
        return BitmapFactory.decodeFile(path, outOptions)
    }

    @Throws(IOException::class)
    fun getCompressed(context: Context?, path: String): File {
        val SDF = SimpleDateFormat("yyyymmddhhmmss", Locale.getDefault())
        if (context == null)
            throw NullPointerException("Context must not be null.")
        //getting device external cache directory, might not be available on some devices,
        // so our code fall back to internal storage cache directory, which is always available but in smaller quantity
        var cacheDir = context.externalCacheDir
        if (cacheDir == null)
        //fall back
            cacheDir = context.cacheDir

        val rootDir = cacheDir!!.absolutePath + "/ImageCompressor"
        val root = File(rootDir)

        //Create ImageCompressor folder if it doesnt already exists.
        if (!root.exists())
            root.mkdirs()

        //decode and resize the original bitmap from @param path.
        val bitmap =
            decodeImageFromFiles(path, /* your desired width*/512, /*your desired height*/ 512)

        //create placeholder for the compressed image file
        val compressed = File(root, SDF.format(Date()) + ".jpg" /*Your desired format*/)

        //convert the decoded bitmap to stream
        val byteArrayOutputStream = ByteArrayOutputStream()

        /*compress bitmap into byteArrayOutputStream
            Bitmap.compress(Format, Quality, OutputStream)
            Where Quality ranges from 1 - 100.
         */
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)

        /*
        Right now, we have our bitmap inside byteArrayOutputStream Object, all we need next is to write it to the compressed file we created earlier,
        java.io.FileOutputStream can help us do just That!
         */
        val fileOutputStream = FileOutputStream(compressed)
        fileOutputStream.write(byteArrayOutputStream.toByteArray())
        fileOutputStream.flush()

        fileOutputStream.close()

        //File written, return to the caller. Done!
        return compressed
    }

    fun getVideoIdFromFilePath(
        contentUri: Uri,
        context: Context
    ): String {

        val videoList = getVideoDataFromContentResolver()
        var videoId: Long
        Log.d(TAG, "Loading file " + contentUri);

        // This returns us content://media/external/videos/media (or something like that)
        // I pass in "external" because that's the MediaStore's name for the external
        // storage on my device (the other possibility is "internal")
        var videosUri: Uri = MediaStore.Video.Media.getContentUri("external");

        Log.d(TAG, "videosUri = " + videosUri.toString());

        var projection = arrayOf(
            MediaStore.Video.VideoColumns._ID
        )
        var path = contentUri.query
        for (data in videoList) {
            val thisPath = data.uri.query
            Log.e(TAG, "uri = ".plus(thisPath))
        }
        // TODO This will break if we have no matching item in the MediaStore.
/*    val selectionArgs = arrayOf(
        path
    )
     val cursor = context.contentResolver.query(videosUri, projection, MediaStore.Video.VideoColumns.DATA+ " LIKE ?" , selectionArgs, null);
    cursor!!.moveToFirst()

    val columnIndex :Int= cursor.getColumnIndex(projection[0])
    videoId = cursor.getLong(columnIndex);

    Log.d(TAG,"Video ID is " + videoId);
    cursor.close();*/
        return " ";

    }
}
