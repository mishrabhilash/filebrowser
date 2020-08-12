package com.abhilashmishra.filebrowser.ui.main.model

import android.graphics.Bitmap
import com.abhilashmishra.filebrowser.ui.main.FileType

class Displayable constructor(val name: String, val path: String, val selected: Boolean, val thumbnail: Bitmap?, fileType: FileType)