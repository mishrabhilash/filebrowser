package com.abhilashmishra.filebrowser.ui.main

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.lifecycle.*
import com.abhilashmishra.filebrowser.ui.main.model.Displayable
import com.abhilashmishra.filebrowser.ui.main.model.ListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import java.io.File

class PageViewModel : ViewModel() {
    var displayableData: ArrayList<ListItem> = ArrayList()
    var loadedIndex = 0
    var updatedContentSize = 0
    private var _loading = MutableLiveData<Boolean>(false)

    var loading: LiveData<Boolean> = Transformations.map(_loading) {
        it
    }

    var loadedList: MutableLiveData<ArrayList<Displayable>> = MutableLiveData()

    companion object {
        const val QUERY_CALL_SIZE = 20
        const val NEXT_ITEM_CALL_THRESHOLD = 5
    }

    @ExperimentalCoroutinesApi
    fun loadDisplayables(fileType: FileType, context: Context) {
        if (_loading.value == true) {
            return
        }
        viewModelScope.launch {
            val list = ArrayList<Displayable>()
            _loading.postValue(true)
            when (fileType) {
                FileType.App -> getApps(context, list)
                else -> getFiles(loadedIndex, fileType, context, list)
            }

            _loading.postValue(false)
            loadedList.postValue(list)
        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun getFiles(index: Int, fileType: FileType, context: Context, displayableList: MutableList<Displayable>) {
        Dispatchers.IO {
            val uri = when (fileType) {
                FileType.Photo -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                FileType.Video -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                FileType.Music -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                else -> MediaStore.Files.getContentUri("external")
            }
            val projection: Array<String>? = null
            val selection: String? = when (fileType) {
                FileType.Other -> MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.MEDIA_TYPE_NONE
                else -> null
            }
            val selectionArgs: Array<String>? = null
            val sortOrder: String? = MediaStore.MediaColumns.DATE_ADDED + " DESC LIMIT " + QUERY_CALL_SIZE + " OFFSET " + index

            val cr = context.contentResolver
            cr.query(uri, projection, selection, selectionArgs, sortOrder).use { cur ->
                if (cur != null) {
                    while (cur.moveToNext()) {
                        val path = cur.getString(cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA))
                        val title = File(path).name
                        val thumbnailBitmap = null
                        val displayable = Displayable(title, path, false, thumbnailBitmap, fileType)
                        displayableList.add(displayable)
                    }
                    cur.close()
                }
            }
        }
    }

    @ExperimentalCoroutinesApi
    private suspend fun getApps(context: Context, displayableList: java.util.ArrayList<Displayable>) {
        Dispatchers.IO {
            val packageManager = context.packageManager
            packageManager.getInstalledApplications(PackageManager.GET_META_DATA)?.let {
                it.forEach { applicationInfo ->
                    val icon = applicationInfo.loadIcon(packageManager)
                    if (!isSystemPackage(applicationInfo)) {
                        val appName = "${packageManager.getApplicationLabel(applicationInfo)}.apk"
                        val displayable = Displayable(appName, getApkDir(applicationInfo), false, icon, FileType.App)
                        displayableList.add(displayable)
                    }
                }
            }
        }
    }

    private fun isSystemPackage(applicationInfo: ApplicationInfo): Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    private fun getApkDir(applicationInfo: ApplicationInfo): String {
        return applicationInfo.sourceDir
    }

    @ExperimentalCoroutinesApi
    fun updateLastVisibleItemPosition(fileType: FileType, context: Context, lastVisibleItemPosition: Int) {
        if (fileType == FileType.App) {
            if (loadedIndex == 0) {
                loadDisplayables(fileType, context)
            }
        } else if (lastVisibleItemPosition > loadedIndex - NEXT_ITEM_CALL_THRESHOLD) {
            loadDisplayables(fileType, context)
        }
    }
}