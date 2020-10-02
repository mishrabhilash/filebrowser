package com.abhilashmishra.filebrowser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.abhilashmishra.filebrowser.ui.main.FileType
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launcher.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra(ListActivity.KEY_TAB_TYPE_LIST, arrayListOf(FileType.Photo, FileType.Video))
            intent.putExtra(ListActivity.KEY_TAB_TITLE_LIST, arrayListOf(R.string.tab_text_photo, R.string.tab_text_video))
            startActivity(intent)
        }
    }
}