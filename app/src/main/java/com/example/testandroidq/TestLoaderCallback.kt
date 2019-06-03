package com.example.testandroidq

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.util.Log

private val MEDIASTORE_PROJECTION = arrayOf(
    MediaStore.Images.ImageColumns.DATE_TAKEN,
    MediaStore.MediaColumns.WIDTH,
    MediaStore.MediaColumns.HEIGHT,
    MediaStore.MediaColumns.DATA
)

data class ImageMetadata (
    val timeTaken: Long,
    val width: Int,
    val height: Int,
    val filePath: String)

class TestLoaderCallback(
    private val context: Context
): LoaderManager.LoaderCallbacks<Cursor> {
    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {
        return CursorLoader(
            context,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            MEDIASTORE_PROJECTION,
            null,
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC LIMIT 5")
    }

    override fun onLoadFinished(p0: Loader<Cursor>, cursor: Cursor?) {
        val imageMetadataList = mutableListOf<ImageMetadata>()

        if (cursor == null) {
            return
        }

        if (cursor.moveToFirst()) {
            do {
                val imagePath = cursor.getString(3)
                if (imagePath != null) {
                    imageMetadataList.add(
                        ImageMetadata(
                            cursor.getLong(0),
                            cursor.getInt(1),
                            cursor.getInt(2),
                            imagePath))
                }
            } while (cursor.moveToNext())
        }

        for (imageData in imageMetadataList) {
            // imageData: ImageMetadata(timeTaken=-1, width=0, height=0, filePath=/storage/emulated/0/Pictures/Screenshots/Screenshot_20190603-111236.png)
            Log.d("TestOnly", "imageData: ${imageData}")
        }
        return
    }

    override fun onLoaderReset(p0: Loader<Cursor>) {
        // NOOP
    }

}