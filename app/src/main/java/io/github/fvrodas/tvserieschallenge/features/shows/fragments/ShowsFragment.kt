package io.github.fvrodas.tvserieschallenge.features.shows.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import io.github.fvrodas.tvserieschallenge.databinding.ShowsFragmentBinding
import io.github.fvrodas.tvserieschallenge.features.shows.adapters.ShowsRecyclerViewAdapter
import io.github.fvrodas.tvserieschallenge.features.shows.viewmodels.ShowsUiState
import io.github.fvrodas.tvserieschallenge.features.shows.viewmodels.ShowsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowsFragment : Fragment() {

    companion object {
        fun newInstance() = ShowsFragment()
    }

    private val viewModel: ShowsViewModel by viewModel()

    private lateinit var viewBinding: ShowsFragmentBinding

    private val showRecyclerViewAdapter = ShowsRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewBinding = ShowsFragmentBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        viewBinding.showsRecyclerView.layoutManager = layoutManager
        viewBinding.showsRecyclerView.adapter = showRecyclerViewAdapter

        lifecycleScope.launch {
            viewModel.showsUiState.collectLatest {
                when(it) {
                    is ShowsUiState.Success -> showRecyclerViewAdapter.submitList(it.shows)
                    is ShowsUiState.Failure -> Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewBinding.showsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchShowByName(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        viewModel.retrieveShowsByPageNumber(0)
    }

}