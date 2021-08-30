/*
 * Copyright 2016 Mario Velasco Casquero
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.techswivel.baseproject.utils

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore

/**
 * Author: Mario Velasco Casquero
 * Date: 21/07/2016
 */
object ImageRotator {

    private val TAG = ImageRotator::class.java.simpleName

    /**
     * Get rotation degrees of the selected image. E.g.: 0º, 90º, 180º, 240º.
     *
     * @param context    context.
     * @param imageUri   URI of image which will be analyzed.
     * @param fromCamera true if the image was taken from camera,
     * false if it was selected from the gallery.
     * @return degrees of rotation.
     */
    fun getRotation(context: Context, imageUri: Uri, fromCamera: Boolean): Int {
        val rotation: Int
        if (fromCamera) {
            rotation = getRotationFromCamera(context, imageUri)
        } else {
            rotation = getRotationFromGallery(context, imageUri)
        }
        Log.i(TAG, "Image rotation: $rotation")
        return rotation
    }

    private fun getRotationFromCamera(context: Context, imageFile: Uri): Int {
        var rotate = 0
        try {

            context.contentResolver.notifyChange(imageFile, null)
            val exif = ExifInterface(imageFile.path!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
                else -> rotate = 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return rotate
    }

    private fun getRotationFromGallery(context: Context, imageUri: Uri): Int {
        var result = 0
        val columns = arrayOf(MediaStore.Images.Media.ORIENTATION)
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(imageUri, columns, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val orientationColumnIndex = cursor.getColumnIndex(columns[0])
                result = cursor.getInt(orientationColumnIndex)
            }
        } catch (e: Exception) {
            //Do nothing
        } finally {
            cursor?.close()
        } //End of try-catch block
        return result
    }

    /**
     * Rotate image X degrees.
     */
    fun rotate(bitmap: Bitmap?, degrees: Int): Bitmap? {
        var bitmap = bitmap
        if (bitmap != null && degrees != 0) {
            val matrix = Matrix()
            matrix.postRotate(degrees.toFloat())
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        return bitmap
    }

}// not called
