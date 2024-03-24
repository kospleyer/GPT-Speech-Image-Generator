package com.ol.gpt.image.generator.util

import android.content.Context
import android.content.ContextWrapper
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


object FileUtils {

    const val IMAGES_DIRECTORY_PATH_SUFFIX = "/images"
    const val DEFAULT_IMAGE_PREFIX = "pic"
    const val PROFILE_IMAGE_PREFIX = "profile-pic"
    const val JPEG_TYPE = ".jpeg"
    private const val MAX_IMAGE_SIZE = 512
    private const val MAX_SIZE = 1000 * 1024


    fun getImagesDirPath(context: Context): String {
        val cw = ContextWrapper(context)
        return cw.filesDir.absolutePath + IMAGES_DIRECTORY_PATH_SUFFIX
    }

    fun processImage(context: Context, src: Uri, path: String) {
        val temp = File.createTempFile("process-", null)

        temp.deleteOnExit()
        copyFile(context, src, temp.absolutePath)
        compressBitmap(context, path)
    }

    fun copyFile(context: Context, src: Uri, path: String) {
        val fi: InputStream = context.contentResolver.openInputStream(src)!!
        val file = File(path)
        if (file.parentFile?.exists() == false) {
            file.parentFile!!.mkdirs()
        }

        if (!file.exists()) {
            file.createNewFile()
        }

        val os = BufferedOutputStream(FileOutputStream(file))
        val bytes = ByteArray(1000000)
        var len = fi.read(bytes, 0, bytes.size)
        while (len > 0) {
            os.write(bytes)
            len = fi.read(bytes, 0, bytes.size)
        }
        os.close()
        fi.close()
    }

    // some photos gets rotated, so we need to rotate it back
    private fun rotateBitmap(path: String, bmp: Bitmap): Bitmap {
        val exif = ExifInterface(path)
        val orientation: Int = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
        val matrix = Matrix()
        when (orientation) {
            6 ->
                matrix.postRotate(90F)
            3 ->
                matrix.postRotate(180F)
            8 ->
                matrix.postRotate(270F)
        }
        return Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true);
    }

    private fun resizeBitmap(path: String): Bitmap {
        val options = BitmapFactory.Options()
//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = false
        var bmp = BitmapFactory.decodeFile(path, options)
        bmp = rotateBitmap(path, bmp)
        val sizes = getMaxWidthAndHeight(bmp)

        return Bitmap.createScaledBitmap(bmp, sizes[0], sizes[1], true)
    }

    private fun compressBitmap(context: Context, imageUri: String) {
        val finalFile = getRealPathFromURI(context, imageUri)

        val scaledBitmap: Bitmap = resizeBitmap(finalFile!!)
        var out = FileOutputStream(finalFile)
        var quality = 100

        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        while (out.channel.size() > MAX_SIZE && quality >= 10) { // 1mb max
            out.close()
            out = FileOutputStream(finalFile)
            quality -= 5
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, quality, out)
        }
        out.close()
    }

    private fun getMaxWidthAndHeight(bitmap: Bitmap, maxSize: Int = MAX_IMAGE_SIZE): IntArray {
        var width = bitmap.width
        var height = bitmap.height
        if (width > maxSize || height > maxSize) {
            if (width > height) {
                height = height * maxSize / width
                width = maxSize
            } else {
                width = width * maxSize / height
                height = maxSize
            }
        }
        return intArrayOf(width, height)
    }

    private fun getRealPathFromURI(context: Context, contentURI: String): String? {
        val contentUri = Uri.parse(contentURI)
        val cursor: Cursor? = context.contentResolver.query(contentUri, null, null, null, null)
        return if (cursor == null) {
            contentUri.path
        } else {
            cursor.moveToFirst()
            val index: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(index)
        }
    }
}