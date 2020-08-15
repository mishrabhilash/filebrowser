package com.abhilashmishra.filebrowser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.abhilashmishra.filebrowser.ui.main.SectionsPagerAdapter
import com.abhilashmishra.filebrowser.ui.main.model.Displayable
import com.abhilashmishra.filebrowser.ui.main.model.Sendable
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity(), ContainerInterface {

    companion object {
        const val KEY_SELECTED_LIST = "key.selected.list"
        const val KEY_SELECTED_LIST_TYPE = "key.selected.list.type"
        const val KEY_SELECTED_LIST_TYPE_FILEPATH = "key.selected.list.type.filepath"
    }

    val button: View by lazy {
        findViewById<View>(R.id.sendContainer)
    }

    val buttonText: TextView by lazy {
        findViewById<TextView>(R.id.sendText)
    }

    val selectedSet: HashSet<Displayable> = HashSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        titleTextView.text = resources.getString(R.string.file_browser)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        button.visibility = GONE

        button.setOnClickListener { view ->

        }
    }

    override fun onDisplableClicked(displyable: Displayable) {
        if (displyable.selected) {
            selectedSet.add(displyable)
        } else {
            selectedSet.remove(displyable)
        }
        updateSelectionUi()
    }

    private fun updateSelectionUi() {
        if (selectedSet.size > 0) {
            button.visibility = VISIBLE
            buttonText.text = resources.getQuantityString(R.plurals.send_files, selectedSet.size, selectedSet.size)
            titleTextView.text = resources.getQuantityString(R.plurals.file_selection_title, selectedSet.size, selectedSet.size)
        } else {
            button.visibility = GONE
            titleTextView.text = resources.getString(R.string.file_browser)
        }
    }

    private fun sendResult() {
        val returnIntent = Intent()
        returnIntent
            .putParcelableArrayListExtra(KEY_SELECTED_LIST, getResultList())
            .putExtra(
                KEY_SELECTED_LIST_TYPE,
                KEY_SELECTED_LIST_TYPE_FILEPATH
            )
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun getResultList(): ArrayList<Sendable> {
        val resultList = ArrayList<Sendable>()
        selectedSet.forEach { displayable ->
            resultList.add(Sendable(displayable.name, displayable.path))
        }
        return resultList
    }
}

