package com.abhilashmishra.filebrowser.ui.main.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abhilashmishra.filebrowser.R
import com.abhilashmishra.filebrowser.ui.main.model.Displayable

class ImageFileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView by lazy {
        itemView.findViewById<TextView>(R.id.fileName)
    }

    private val imageView: ImageView by lazy {
        itemView.findViewById<ImageView>(R.id.imageBitmap)
    }

    fun initView(displayable: Displayable) {
        textView.text = displayable.name
        imageView.setImageBitmap(displayable.thumbnail)
    }
}