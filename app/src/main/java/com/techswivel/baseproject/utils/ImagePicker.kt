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

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.techswivel.baseproject.BuildConfig
import com.techswivel.baseproject.R
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.*

object ImagePicker {

    private val DEFAULT_REQUEST_CODE = 234

    private val DEFAULT_MIN_WIDTH_QUALITY = 400        // min pixels
    private val DEFAULT_MIN_HEIGHT_QUALITY = 400        // min pixels
    private val TAG = ImagePicker::class.java.simpleName
    private val TEMP_IMAGE_NAME = "tempImage"

    private var minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY
    private var minHeightQuality = DEFAULT_MIN_HEIGHT_QUALITY

    private var mChooserTitle: String? = null
    private var mPickImageRequestCode = DEFAULT_REQUEST_CODE
    private var mGalleryOnly = false

    /**
     * Launch a dialog to pick an image from camera/gallery apps with custom request code.
     *
     * @param activity which will launch the dialog.
     * @param requestCode request code that will be returned in result.
     */
    fun pickImage(activity: Activity, requestCode: Int) {
        pickImage(activity, activity.getString(R.string.pick_image_intent_text), requestCode, false)
    }

    /**
     * Launch a dialog to pick an image from camera/gallery apps with custom request code.
     *
     * @param fragment which will launch the dialog.
     * @param requestCode request code that will be returned in result.
     */
    fun pickImage(fragment: Fragment, requestCode: Int) {
        pickImage(fragment, fragment.getString(R.string.pick_image_intent_text), requestCode, false)
    }

    /**
     * Launch a dialog to pick an image from camera/gallery apps with custom request code.
     *
     * @param fragment which will launch the dialog.
     */
    fun pickImage(fragment: Fragment) {
        pickImage(
            fragment,
            fragment.getString(R.string.pick_image_intent_text),
            DEFAULT_REQUEST_CODE,
            false
        )
    }

    /**
     * Launch a dialog to pick an image from gallery apps only with custom request code.
     *
     * @param activity which will launch the dialog.
     * @param requestCode request code that will be returned in result.
     */
    fun pickImageGalleryOnly(activity: Activity, requestCode: Int) {
        pickImage(activity, activity.getString(R.string.pick_image_intent_text), requestCode, true)

    }

    /**
     * Launch a dialog to pick an image from gallery apps only with custom request code.
     *
     * @param fragment which will launch the dialog.
     * @param requestCode request code that will be returned in result.
     */
    fun pickImageGalleryOnly(fragment: Fragment, requestCode: Int) {
        pickImage(fragment, fragment.getString(R.string.pick_image_intent_text), requestCode, true)
    }

    /**
     * Launch a dialog to pick an image from camera/gallery apps.
     *
     * @param fragment     which will launch the dialog and will get the result in
     * onActivityResult()
     * @param chooserTitle will appear on the picker dialog.
     * @param requestCode request code that will be returned in result.
     */
    @JvmOverloads
    fun pickImage(
        fragment: Fragment, chooserTitle: String,
        requestCode: Int = DEFAULT_REQUEST_CODE, galleryOnly: Boolean = false
    ) {
        mGalleryOnly = galleryOnly
        mPickImageRequestCode = requestCode
        mChooserTitle = chooserTitle
        startChooser(fragment)
    }

    /**
     * Launch a dialog to pick an image from camera/gallery apps.
     *
     * @param activity     which will launch the dialog and will get the result in
     * onActivityResult()
     * @param chooserTitle will appear on the picker dialog.
     */
    @JvmOverloads
    fun pickImage(
        activity: Activity,
        chooserTitle: String = activity.getString(R.string.pick_image_intent_text),
        requestCode: Int = DEFAULT_REQUEST_CODE,
        galleryOnly: Boolean = false
    ) {
        mGalleryOnly = galleryOnly
        mPickImageRequestCode = requestCode
        mChooserTitle = chooserTitle
        startChooser(activity)
    }

    private fun startChooser(fragmentContext: Fragment) {
        val chooseImageIntent =
            fragmentContext.getContext()?.let { getPickImageIntent(it, mChooserTitle) }
        fragmentContext.startActivityForResult(chooseImageIntent, mPickImageRequestCode)
    }

    private fun startChooser(activityContext: Activity) {
        val chooseImageIntent = getPickImageIntent(activityContext, mChooserTitle)
        activityContext.startActivityForResult(chooseImageIntent, mPickImageRequestCode)
    }

    /**
     * Get an Intent which will launch a dialog to pick an image from camera/gallery apps.
     *
     * @param context      context.
     * @param chooserTitle will appear on the picker dialog.
     * @return intent launcher.
     */
    fun getPickImageIntent(context: Context, chooserTitle: String?): Intent? {
        var chooserIntent: Intent? = null
        var intentList: MutableList<Intent> = ArrayList()

        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        intentList = addIntentsToList(context, intentList, pickIntent)

        // Check is we want gallery apps only
        if (!mGalleryOnly) {
            // Camera action will fail if the app does not have permission, check before adding intent.
            // We only need to add the camera intent if the app does not use the CAMERA permission
            // in the androidmanifest.xml
            // Or if the user has granted access to the camera.
            // See https://developer.android.com/reference/android/provider/MediaStore.html#ACTION_IMAGE_CAPTURE
            if (!appManifestContainsPermission(
                    context,
                    Manifest.permission.CAMERA
                ) || hasCameraAccess(context)
            ) {
                val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                takePhotoIntent.putExtra("return-data", true)
                takePhotoIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    FileProvider.getUriForFile(
                        context, BuildConfig.APPLICATION_ID + ".provider",
                        ImageUtils.getTemporalFile(context, mPickImageRequestCode.toString())
                    )
                )
                //Uri.fromFile(ImageUtils.getTemporalFile(context, String.valueOf(mPickImageRequestCode))));
                intentList = addIntentsToList(context, intentList, takePhotoIntent)
            }
        }

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                intentList.removeAt(intentList.size - 1),
                chooserTitle
            )
            chooserIntent!!.putExtra(
                Intent.EXTRA_INITIAL_INTENTS,
                intentList.toTypedArray<Parcelable>()
            )
        }

        return chooserIntent
    }

    private fun addIntentsToList(
        context: Context,
        list: MutableList<Intent>,
        intent: Intent
    ): MutableList<Intent> {
        Log.i(TAG, "Adding intents of type: " + intent.action!!)
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
            Log.i(TAG, "App package: $packageName")
        }
        return list
    }

    /**
     * Checks if the current context has permission to access the camera.
     * @param context             context.
     */
    private fun hasCameraAccess(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) === PackageManager.PERMISSION_GRANTED
    }

    /**
     * Checks if the androidmanifest.xml contains the given permission.
     * @param context             context.
     * @return Boolean, indicating if the permission is present.
     */
    private fun appManifestContainsPermission(context: Context, permission: String): Boolean {
        val pm = context.packageManager
        try {
            val packageInfo = pm.getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
            var requestedPermissions: Array<String>? = null
            if (packageInfo != null) {
                requestedPermissions = packageInfo.requestedPermissions
            }
            if (requestedPermissions == null) {
                return false
            }

            if (requestedPermissions.size > 0) {
                val requestedPermissionsList = Arrays.asList(*requestedPermissions)
                return requestedPermissionsList.contains(permission)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * Called after launching the picker with the same values of Activity.getImageFromResult
     * in order to resolve the result and get the image.
     *
     * @param context             context.
     * @param requestCode         used to identify the pick image action.
     * @param resultCode          -1 means the result is OK.
     * @param imageReturnedIntent returned intent where is the image data.
     * @return image.
     */
    @Nullable
    fun getImageFromResult(
        context: Context, requestCode: Int, resultCode: Int,
        imageReturnedIntent: Intent?
    ): Bitmap? {
        Log.i(TAG, "getImageFromResult() called with: resultCode = [$resultCode]")
        var bm: Bitmap? = null
        if (resultCode == Activity.RESULT_OK) {
            val imageFile = ImageUtils.getTemporalFile(context, requestCode.toString())
            val selectedImage: Uri?
            val isCamera = (imageReturnedIntent == null
                    || imageReturnedIntent.data == null
                    || imageReturnedIntent.data!!.toString().contains(imageFile.toString()))
            if (isCamera) {
                /** CAMERA  */
                //selectedImage = Uri.fromFile(imageFile);
                selectedImage = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider", imageFile
                )


            } else {
                /** ALBUM  */
                selectedImage = imageReturnedIntent!!.data
            }
            Log.i(TAG, "selectedImage: " + selectedImage!!)

            bm = decodeBitmap(context, selectedImage)
            val rotation = ImageRotator.getRotation(context, selectedImage, isCamera)
            bm = ImageRotator.rotate(bm, rotation)
        }
        return bm
    }

    /**
     * Called after launching the picker with the same values of Activity.getImageFromResult
     * in order to resolve the result and get the image path.
     *
     * @param context             context.
     * @param requestCode         used to identify the pick image action.
     * @param resultCode          -1 means the result is OK.
     * @param imageReturnedIntent returned intent where is the image data.
     * @return path to the saved image.
     */
    @Nullable
    fun getImagePathFromResult(
        context: Context, requestCode: Int, resultCode: Int,
        imageReturnedIntent: Intent?
    ): String? {
        Log.i(TAG, "getImagePathFromResult() called with: resultCode = [$resultCode]")
        var selectedImage: Uri? = null
        if (resultCode == Activity.RESULT_OK && requestCode == mPickImageRequestCode) {
            val imageFile = ImageUtils.getTemporalFile(context, mPickImageRequestCode.toString())
            val isCamera = (imageReturnedIntent == null
                    || imageReturnedIntent.data == null
                    || imageReturnedIntent.data!!.toString().contains(imageFile.toString()))
            if (isCamera) {
                return imageFile.absolutePath
            } else {
                selectedImage = imageReturnedIntent!!.data
            }
            Log.i(TAG, "selectedImage: " + selectedImage!!)
        }
        return if (selectedImage == null) {
            null
        } else getFilePathFromUri(context, selectedImage)
    }

    /**
     * Get stream, save the picture to the temp file and return path.
     *
     * @param context context
     * @param uri uri of the incoming file
     * @return path to the saved image.
     */
    private fun getFilePathFromUri(context: Context, uri: Uri): String? {
        var `is`: InputStream? = null
        if (uri.authority != null) {
            try {
                `is` = context.contentResolver.openInputStream(uri)
                val bmp = BitmapFactory.decodeStream(`is`)
                return ImageUtils.savePicture(context, bmp, uri.path!!.hashCode().toString())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } finally {
                try {
                    `is`!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return null
    }

    /**
     * Called after launching the picker with the same values of Activity.getImageFromResult
     * in order to resolve the result and get the input stream for the image.
     *
     * @param context             context.
     * @param requestCode         used to identify the pick image action.
     * @param resultCode          -1 means the result is OK.
     * @param imageReturnedIntent returned intent where is the image data.
     * @return stream.
     */
    fun getInputStreamFromResult(
        context: Context, requestCode: Int, resultCode: Int,
        imageReturnedIntent: Intent?
    ): InputStream? {
        Log.i(TAG, "getFileFromResult() called with: resultCode = [$resultCode]")
        if (resultCode == Activity.RESULT_OK && requestCode == mPickImageRequestCode) {
            val imageFile = ImageUtils.getTemporalFile(context, mPickImageRequestCode.toString())
            val selectedImage: Uri?
            val isCamera = (imageReturnedIntent == null
                    || imageReturnedIntent.data == null
                    || imageReturnedIntent.data!!.toString().contains(imageFile.toString()))
            if (isCamera) {
                /** CAMERA  */
                //selectedImage = Uri.fromFile(imageFile);
                selectedImage = FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".provider", imageFile
                )
            } else {
                /** ALBUM  */
                selectedImage = imageReturnedIntent!!.data
            }
            Log.i(TAG, "selectedImage: " + selectedImage!!)

            try {
                return if (isCamera) {
                    // We can just open the temporary file stream and return it
                    FileInputStream(imageFile)
                } else {
                    // Otherwise use the ContentResolver
                    context.contentResolver.openInputStream(selectedImage)
                }
            } catch (ex: FileNotFoundException) {
                Log.e(TAG, "Could not open input stream for: $selectedImage")
                return null
            }

        }
        return null
    }

    /**
     * Loads a bitmap and avoids using too much memory loading big images (e.g.: 2560*1920)
     */
    fun decodeBitmap(context: Context, theUri: Uri): Bitmap? {
        var outputBitmap: Bitmap? = null
        var fileDescriptor: AssetFileDescriptor? = null

        try {
            fileDescriptor = context.contentResolver.openAssetFileDescriptor(theUri, "r")

            // Get size of bitmap file
            val boundsOptions = BitmapFactory.Options()
            boundsOptions.inJustDecodeBounds = true
            BitmapFactory.decodeFileDescriptor(fileDescriptor!!.fileDescriptor, null, boundsOptions)

            // Get desired sample size. Note that these must be powers-of-two.
            val sampleSizes = intArrayOf(8, 4, 2, 1)
            var selectedSampleSize = 1 // 1 by default (original image)

            for (sampleSize in sampleSizes) {
                selectedSampleSize = sampleSize
                val targetWidth = boundsOptions.outWidth / sampleSize
                val targetHeight = boundsOptions.outHeight / sampleSize
                if (targetWidth >= minWidthQuality && targetHeight >= minHeightQuality) {
                    break
                }
            }

            // Decode bitmap at desired size
            val decodeOptions = BitmapFactory.Options()
            decodeOptions.inSampleSize = selectedSampleSize
            outputBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.fileDescriptor,
                null,
                decodeOptions
            )
            if (outputBitmap != null) {
                Log.i(
                    TAG, "Loaded image with sample size " + decodeOptions.inSampleSize + "\t\t"
                            + "Bitmap width: " + outputBitmap.width
                            + "\theight: " + outputBitmap.height
                )
            }
            fileDescriptor.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return outputBitmap
    }


    /*
    GETTERS AND SETTERS
     */

    fun setMinQuality(minWidthQuality: Int, minHeightQuality: Int) {
        ImagePicker.minWidthQuality = minWidthQuality
        ImagePicker.minHeightQuality = minHeightQuality
    }
}// not called
/**
 * Launch a dialog to pick an image from camera/gallery apps with custom request code.
 *
 * @param activity which will launch the dialog.
 */
/**
 * Launch a dialog to pick an image from camera/gallery apps.
 *
 * @param activity     which will launch the dialog.
 * @param chooserTitle will appear on the picker dialog.
 */
/**
 * Launch a dialog to pick an image from camera/gallery apps.
 *
 * @param fragment     which will launch the dialog and will get the result in
 * onActivityResult()
 * @param chooserTitle will appear on the picker dialog.
 */
