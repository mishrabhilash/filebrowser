package com.abhilashmishra.filebrowser.ui.main.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.abhilashmishra.filebrowser.ui.main.FileType

class Displayable constructor(
    val name: String,
    val path: String,
    var selected: Boolean,
    val thumbnail: Drawable?,
    val fileType: FileType
) : ListItem() {
    var onClickAction: (Displayable) -> Unit = {
        Log.d("check--", "asdf")
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Displayable) {
            other.path == this.path
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }
}