package com.abhilashmishra.filebrowser.ui.main.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.abhilashmishra.filebrowser.ui.main.model.ListItem

abstract class BaseListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun initView(listItem: ListItem, position: Int)
}