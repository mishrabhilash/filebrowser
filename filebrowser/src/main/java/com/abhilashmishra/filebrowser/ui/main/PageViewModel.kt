package com.abhilashmishra.filebrowser.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.abhilashmishra.filebrowser.ui.main.datasource.FileDataSourceFactory
import com.abhilashmishra.filebrowser.ui.main.model.Displayable

class PageViewModel : ViewModel() {

    lateinit var displayableData: LiveData<PagedList<Displayable>>

    fun loadDisplayables(fileType: FileType, context: Context) {
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .build()
        displayableData = LivePagedListBuilder(
            FileDataSourceFactory(
                fileType,
                context
            ), config
        )
            .build()
    }
}