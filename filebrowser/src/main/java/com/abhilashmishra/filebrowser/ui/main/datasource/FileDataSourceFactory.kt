package com.abhilashmishra.filebrowser.ui.main.datasource

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.abhilashmishra.filebrowser.ui.main.FileType
import com.abhilashmishra.filebrowser.ui.main.model.Displayable

class FileDataSourceFactory(val fileType: FileType, val context: Context) : DataSource.Factory<Int, Displayable>() {
    private val sourceLiveData = MutableLiveData<FileDataSource>()
    private lateinit var latestSource: FileDataSource
    override fun create(): DataSource<Int, Displayable> {
        latestSource = FileDataSource(fileType, context)
        sourceLiveData.postValue(latestSource)
        return latestSource
    }
}