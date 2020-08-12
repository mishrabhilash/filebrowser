package com.abhilashmishra.filebrowser.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.abhilashmishra.filebrowser.R
import com.abhilashmishra.filebrowser.ui.main.model.Displayable

class ImageFileAdapter(diffCallback: DiffUtil.ItemCallback<Displayable>) : PagedListAdapter<Displayable, ImageFileViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFileViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_file_image, parent, false)
        return ImageFileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageFileViewHolder, position: Int) {
        getItem(position)?.let {
            holder.initView(it)
        }
    }
}