package com.abhilashmishra.filebrowser.ui.main.datasource

import android.content.Context
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.util.Size
import androidx.paging.PositionalDataSource
import com.abhilashmishra.filebrowser.ui.main.FileType
import com.abhilashmishra.filebrowser.ui.main.model.Displayable
import java.io.File


class FileDataSource(private val fileType: FileType, private val context: Context) : PositionalDataSource<Displayable>() {
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Displayable>) {
        callback.onResult(getFiles(params.requestedLoadSize, params.requestedStartPosition, fileType, context), 0, 1000)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Displayable>) {
        callback.onResult(getFiles(params.loadSize, params.startPosition, fileType, context))
    }

    private fun getFiles(size: Int, index: Int, fileType: FileType, context: Context): MutableList<Displayable> {
        val displayableList: MutableList<Displayable> = mutableListOf()
        val uri = when (fileType) {
            FileType.Photo -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            FileType.Video -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            FileType.Music -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            FileType.App -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            FileType.Other -> MediaStore.Files.getContentUri("external")
        }
        val projection: Array<String>? = null
        val selection: String? = when (fileType) {
            FileType.Other -> MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_NONE
            else -> null
        }
        val selectionArgs: Array<String>? = null
        val sortOrder: String? = MediaStore.MediaColumns.DATE_ADDED + " DESC LIMIT " + size + " OFFSET " + index

        val cr = context.contentResolver
        cr.query(uri, projection, selection, selectionArgs, sortOrder).use { cur ->
            if (cur != null) {
                while (cur.moveToNext()) {
                    val path = cur.getString(cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA))
                    val title = File(path).name
                    val thumbnailBitmap = try {
                        when (fileType) {
                            FileType.Photo -> ThumbnailUtils.createImageThumbnail(File(path), Size(200, 200), null)
                            FileType.Video -> ThumbnailUtils.createVideoThumbnail(File(path), Size(200, 200), null)
                            FileType.Music -> ThumbnailUtils.createAudioThumbnail(File(path), Size(200, 200), null)
                            FileType.App -> ThumbnailUtils.createAudioThumbnail(File(path), Size(200, 200), null)
                            FileType.Other -> null
                        }
                    } catch (e: Exception) {
                        null
                    }
//                    val thumbnailBitmap = ThumbnailUtils.createImageThumbnail(File(path), Size(200, 200), null)
                    val displayable = Displayable(title, path, false, thumbnailBitmap, fileType)
                    displayableList.add(displayable)
                }
                cur.close()
            }
        }
        return displayableList
    }
}