package com.abhilashmishra.filebrowser.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhilashmishra.filebrowser.R
import com.abhilashmishra.filebrowser.ui.main.adapter.ImageFileAdapter
import com.abhilashmishra.filebrowser.ui.main.model.Displayable

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var adapter: ImageFileAdapter
    private lateinit var fileType: FileType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileType = arguments?.getParcelable(ARG_FILE_TYPE)!!
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java)
        val diffCallback = object : DiffUtil.ItemCallback<Displayable>() {
            override fun areItemsTheSame(oldItem: Displayable, newItem: Displayable): Boolean {
                return oldItem.path == newItem.path
            }

            override fun areContentsTheSame(oldItem: Displayable, newItem: Displayable): Boolean {
                return oldItem.selected == newItem.selected
            }
        }
        adapter = ImageFileAdapter(diffCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_file_list, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.filesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(container?.context)
        recyclerView.adapter = adapter
        pageViewModel.loadDisplayables(fileType, requireContext())
        pageViewModel.displayableData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
//            contactsEmpty.visibility = if (adapter.itemCount > 0) {
//                View.GONE
//            } else {
//                View.VISIBLE
//            }
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_FILE_TYPE = "arg_file_type"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(fileType: FileType): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_FILE_TYPE, fileType)
                }
            }
        }
    }
}