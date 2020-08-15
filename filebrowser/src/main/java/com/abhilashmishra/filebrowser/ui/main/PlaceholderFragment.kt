package com.abhilashmishra.filebrowser.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhilashmishra.filebrowser.ContainerInterface
import com.abhilashmishra.filebrowser.R
import com.abhilashmishra.filebrowser.ui.main.adapter.ImageFileAdapter
import com.abhilashmishra.filebrowser.ui.main.model.Displayable
import com.abhilashmishra.filebrowser.ui.main.model.LoaderItem
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var adapter: ImageFileAdapter
    private lateinit var fileType: FileType
    private lateinit var containerInterface: ContainerInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fileType = arguments?.getParcelable(ARG_FILE_TYPE)!!
        containerInterface = activity as ContainerInterface
        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java)
        val diffCallback = object : DiffUtil.ItemCallback<Displayable>() {
            override fun areItemsTheSame(oldItem: Displayable, newItem: Displayable): Boolean {
                return oldItem.path == newItem.path
            }

            override fun areContentsTheSame(oldItem: Displayable, newItem: Displayable): Boolean {
                return oldItem.selected == newItem.selected
            }
        }
        adapter = ImageFileAdapter(pageViewModel.displayableData)
    }

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_file_list, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.filesRecyclerView)
        when (fileType) {
            FileType.Photo, FileType.Video -> {
                recyclerView.layoutManager = GridLayoutManager(container?.context, 2)
                (recyclerView.layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (pageViewModel.displayableData[position] is LoaderItem) {
                            2
                        } else {
                            1
                        }
                    }
                }
            }
            else -> {
                recyclerView.layoutManager = LinearLayoutManager(container?.context)
            }
        }

        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                pageViewModel.updateLastVisibleItemPosition(fileType, requireContext(), lastVisibleItemPosition)
            }
        })
        pageViewModel.updateLastVisibleItemPosition(
            fileType, requireContext(),
            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        )
        pageViewModel.loading.observe(viewLifecycleOwner, Observer {
            if (it) {
                val loader = when (fileType) {
                    FileType.Photo, FileType.Video -> LoaderItem(LoaderItem.LOADER_HORIZONTAL)
                    else -> LoaderItem(LoaderItem.LOADER_VERTICAL)
                }
                pageViewModel.displayableData.add(loader)
                adapter.notifyItemInserted(pageViewModel.displayableData.size)
            } else {
                if (pageViewModel.displayableData.size > 0) {
                    val lastItem = pageViewModel.displayableData[pageViewModel.displayableData.size - 1]
                    if (lastItem is LoaderItem) {
                        pageViewModel.displayableData.removeAt(pageViewModel.displayableData.size - 1)
                        adapter.notifyItemRemoved(pageViewModel.displayableData.size)
                    }
                }
            }
        })

        pageViewModel.loadedList.observe(viewLifecycleOwner, Observer { list ->
            list.forEach { displayable ->
                displayable.onClickAction = {
                    containerInterface.onDisplableClicked(displayable)
                }
            }
            pageViewModel.displayableData.addAll(list)
            pageViewModel.loadedIndex += list.size
            pageViewModel.updatedContentSize = list.size
            adapter.notifyItemRangeInserted(pageViewModel.loadedIndex - pageViewModel.updatedContentSize, pageViewModel.updatedContentSize)
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private const val ARG_FILE_TYPE = "arg_file_type"

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