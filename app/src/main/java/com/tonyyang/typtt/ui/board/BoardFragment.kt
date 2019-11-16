package com.tonyyang.typtt.ui.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tonyyang.typtt.R
import com.tonyyang.typtt.model.Articles
import com.tonyyang.typtt.viewmodel.BoardViewModel
import kotlinx.android.synthetic.main.fragment_board.*

class BoardFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(BoardViewModel::class.java)
    }

    private val boardAdapter by lazy {
        BoardAdapter().also {
            it.listener = boardItemListener
        }
    }

    private val boardItemListener = object : BoardAdapter.OnItemClickListener {

        override fun onItemClick(view: View, articles: Articles) {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url = arguments?.let {
            BoardFragmentArgs.fromBundle(it).url
        } ?: ""
        recycler_view.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = boardAdapter
        }
        viewModel.articleListLiveData.observe(this, Observer {
            boardAdapter.updateList(it)
        })
        viewModel.isRefreshLiveData.observe(this, Observer {
            swipe_refresh.isRefreshing = it
        })
        swipe_refresh.setOnRefreshListener {
            viewModel.loadData(url)
        }
        viewModel.loadData(url)
    }
}