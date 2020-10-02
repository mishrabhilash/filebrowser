package com.abhilashmishra.filebrowser.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.abhilashmishra.filebrowser.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_photo,
    R.string.tab_text_video,
    R.string.tab_text_music,
    R.string.tab_text_app,
    R.string.tab_text_other
)

private val TAB_TYPES = arrayOf(
    FileType.Photo,
    FileType.Video,
    FileType.Music,
    FileType.App,
    FileType.Other
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context,
                           fm: FragmentManager,
                           val tabTypeList: Array<FileType> = TAB_TYPES,
                           val tabTitleResource: Array<Int> = TAB_TITLES) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(tabTypeList[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(tabTitleResource[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return tabTypeList.size
    }


}