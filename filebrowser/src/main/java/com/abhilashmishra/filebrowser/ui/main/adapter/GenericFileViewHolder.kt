package com.abhilashmishra.filebrowser.ui.main.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.abhilashmishra.filebrowser.R
import com.abhilashmishra.filebrowser.ui.main.FileType
import com.abhilashmishra.filebrowser.ui.main.model.Displayable
import com.abhilashmishra.filebrowser.ui.main.model.ListItem

class GenericFileViewHolder(itemView: View) : BaseListViewHolder(itemView) {

    private val fileName: TextView by lazy {
        itemView.findViewById<TextView>(R.id.fileName)
    }

    private val extensiontName: TextView by lazy {
        itemView.findViewById<TextView>(R.id.extenstionTextView)
    }

    private val checkedImage: ImageView by lazy {
        itemView.findViewById<ImageView>(R.id.checkedImage)
    }

    private val checkedImageBackground: View by lazy {
        itemView.findViewById<View>(R.id.checkedImageBackground)
    }

    override fun initView(listItem: ListItem, position: Int) {
        val displayable = listItem as Displayable
        itemView.setOnClickListener {
            displayable.selected = !displayable.selected
            displayable.onClickAction.invoke(displayable)
            handleCheckVisibility(displayable)
        }
        handleCheckVisibility(displayable)

        extensiontName.text = if (displayable.name.contains('.')) {
            displayable.name.substringAfterLast('.')
        } else {
            ""
        }

        fileName.text = displayable.name
    }

    private fun handleCheckVisibility(displayable: Displayable) {
        if (displayable.selected) {
            checkedImage.visibility = View.VISIBLE
            checkedImageBackground.visibility = View.VISIBLE
        } else {
            checkedImage.visibility = View.GONE
            checkedImageBackground.visibility = View.GONE
        }
    }
}