package com.abhilashmishra.filebrowser.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abhilashmishra.filebrowser.R
import com.abhilashmishra.filebrowser.ui.main.FileType
import com.abhilashmishra.filebrowser.ui.main.model.Displayable
import com.abhilashmishra.filebrowser.ui.main.model.ListItem
import com.abhilashmishra.filebrowser.ui.main.model.LoaderItem

class ImageFileAdapter(val displayableList: ArrayList<ListItem>) : RecyclerView.Adapter<BaseListViewHolder>() {
    companion object {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseListViewHolder = when (viewType) {
        FileType.Photo.ordinal -> {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_image_thumbnail_horizontal, parent, false)
            ImageFileViewHolder(itemView)
        }

        FileType.Video.ordinal -> {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_video_thumbnail_horizontal, parent, false)
            VideoFileViewHolder(itemView)
        }

        FileType.App.ordinal -> {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_app_vertical, parent, false)
            AppFileViewHolder(itemView)
        }

        LoaderItem.LOADER_HORIZONTAL -> {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_loader_horizontal, parent, false)
            LoaderHorizontalViewHolder(itemView)
        }

        LoaderItem.LOADER_VERTICAL -> {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_loader_vertical, parent, false)
            LoaderHorizontalViewHolder(itemView)
        }

        else -> {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_file_extension_vertical, parent, false)
            GenericFileViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: BaseListViewHolder, position: Int) {
        holder.initView(displayableList[position], position)
    }

    override fun getItemCount(): Int {
        return displayableList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = displayableList[position]
        return if (item is Displayable) {
            item.fileType.ordinal
        } else if (item is LoaderItem) {
            item.type
        } else {
            -1
        }

    }
}