package com.abhilashmishra.filebrowser.ui.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class FileType : Parcelable {
    Photo,
    Video,
    Music,
    App,
    Other
}