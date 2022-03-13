package io.github.fvrodas.tvserieschallenge.features.shows.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.tvserieschallenge.databinding.ShowsFragmentBinding
import io.github.fvrodas.tvserieschallenge.features.shows.activities.ShowDetailsActivity
import io.github.fvrodas.tvserieschallenge.features.shows.adapters.ShowsRecyclerViewAdapter
import io.github.fvrodas.tvserieschallenge.features.shows.viewmodels.ShowsUiState
import io.github.fvrodas.tvserieschallenge.features.shows.viewmodels.ShowsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowsFragment : Fragment() {

    companion object {
        fun newInstance() = ShowsFragment()
    }

    private val viewModel: ShowsViewModel by viewModel()

    private lateinit var viewBinding: ShowsFragmentBinding

    private val showRecyclerViewAdapter = ShowsRecyclerViewAdapter(object :
        ShowsRecyclerViewAdapter.Companion.ShowsRecyclerViewAdapterListener {
        override fun onItemPressed(show: ShowEntity) {
            Intent(requireContext(), ShowDetailsActivity::class.java).apply {
                putExtra(ShowDetailsActivity.EXTRA_SHOW, show)
                startActivity(this)
            }
        }

        override fun onItemLongPressed(show: ShowEntity): Boolean {
            return false
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = ShowsFragmentBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager =
            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        viewBinding.showsRecyclerView.layoutManager = layoutManager
        viewBinding.showsRecyclerView.adapter = showRecyclerViewAdapter

        lifecycleScope.launch {
            viewModel.showsUiState.collect {
                when (it) {
                    is ShowsUiState.Loading -> viewBinding.progressIndicator.visibility =
                        View.VISIBLE
                    is ShowsUiState.Success -> {
                        viewBinding.progressIndicator.visibility = View.GONE
                        showRecyclerViewAdapter.submitList(it.shows)
                    }
                    is ShowsUiState.Failure -> {
                        viewBinding.progressIndicator.visibility = View.GONE
                        Toast.makeText(context, it.error, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

        viewBinding.includedToolbar.searchView.queryHint = "Search Shows"

        viewBinding.includedToolbar.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchShowByName(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.retrieveShowsByPageNumber(0)
                }
                return true
            }
        })

        viewModel.retrieveShowsByPageNumber(0)
    }

}