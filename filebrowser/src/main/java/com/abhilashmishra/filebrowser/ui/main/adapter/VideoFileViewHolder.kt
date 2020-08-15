package com.abhilashmishra.filebrowser.ui.main.adapter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import com.abhilashmishra.filebrowser.R
import com.abhilashmishra.filebrowser.ui.main.model.Displayable
import com.abhilashmishra.filebrowser.ui.main.model.ListItem
import com.bumptech.glide.Glide
import java.io.File

class VideoFileViewHolder(itemView: View) : BaseListViewHolder(itemView) {

    private val imageView: ImageView by lazy {
        itemView.findViewById<ImageView>(R.id.imageBitmap)
    }

    private val checkedImage: ImageView by lazy {
        itemView.findViewById<ImageView>(R.id.checkedImage)
    }

    private val checkedImageBackground: View by lazy {
        itemView.findViewById<View>(R.id.checkedImageBackground)
    }

    private val playButton: View by lazy {
        itemView.findViewById<View>(R.id.playButton)
    }

    override fun initView(listItem: ListItem, position: Int) {
        val displayable = listItem as Displayable
        val resources = imageView.context.resources
        (imageView.layoutParams as ConstraintLayout.LayoutParams).apply {
            if (position % 2 == 0) {
                marginStart = 0
                marginEnd = resources.getDimensionPixelSize(R.dimen.dimen_1dp)
            } else {
                marginStart = resources.getDimensionPixelSize(R.dimen.dimen_1dp)
                marginEnd = 0
            }
            bottomMargin = resources.getDimensionPixelSize(R.dimen.dimen_2dp)
            width = (resources.displayMetrics.widthPixels - resources.getDimensionPixelSize(R.dimen.dimen_2dp)) / 2
            height = width
        }
        if (displayable.thumbnail == null) {
            Glide.with(imageView.context)
                .asBitmap()
                .load(displayable.path)
                .into(imageView)
        }
        itemView.setOnClickListener {
            displayable.selected = !displayable.selected
            displayable.onClickAction.invoke(displayable)
            handleCheckVisibility(displayable)
        }

        playButton.setOnClickListener {
            val myMime = MimeTypeMap.getSingleton()
            val mimeType = myMime.getMimeTypeFromExtension(displayable.name.substringAfterLast("."))
            val sharedUri = FileProvider.getUriForFile(
                itemView.context,
                "${itemView.context.applicationInfo.packageName}.fileprovider",
                File(displayable.path)
            )
            val newIntent = Intent(Intent.ACTION_VIEW)
            newIntent.setDataAndType(sharedUri, mimeType)
            newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                itemView.context.startActivity(newIntent)
            } catch (e: ActivityNotFoundException) {

            }
        }


        handleCheckVisibility(displayable)
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