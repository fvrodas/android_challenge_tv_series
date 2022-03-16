package io.github.fvrodas.tvserieschallenge.features.favorite_shows.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.FragmentFavoriteShowsBinding
import io.github.fvrodas.tvserieschallenge.features.favorite_shows.adapters.FavoriteShowsListRecyclerViewAdapter
import io.github.fvrodas.tvserieschallenge.features.favorite_shows.viewmodels.FavoriteShowsUiState
import io.github.fvrodas.tvserieschallenge.features.favorite_shows.viewmodels.FavoriteShowsViewModel
import io.github.fvrodas.tvserieschallenge.features.shows.activities.ShowDetailsActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteShowsFragment : Fragment() {

    companion object {
        fun newInstance() = FavoriteShowsFragment()
    }

    private lateinit var viewBinding: FragmentFavoriteShowsBinding

    private val viewModel: FavoriteShowsViewModel by viewModel()

    private val showRecyclerViewAdapter = FavoriteShowsListRecyclerViewAdapter(object :
        FavoriteShowsListRecyclerViewAdapter.Companion.FavoriteShowsRecyclerViewAdapterListener {
        override fun onItemPressed(show: ShowEntity) {
            Intent(requireContext(), ShowDetailsActivity::class.java).apply {
                putExtra(ShowDetailsActivity.EXTRA_SHOW, show)
                startActivity(this)
            }
        }

        override fun onItemLongPressed(show: ShowEntity): Boolean {
            AlertDialog.Builder(requireContext()).apply {
                setTitle(R.string.alert_delete_title)
                setMessage(R.string.alert_delete_message)
                setNegativeButton(
                    android.R.string.cancel
                ) { dialogInterface, _ -> dialogInterface.dismiss() }
                setPositiveButton(android.R.string.ok) { dialogInterface, _ ->
                    show.let {
                        viewModel.deleteShowFromFavorite(it)
                    }
                    dialogInterface.dismiss()
                }
                show()
            }
            return true
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentFavoriteShowsBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.favoriteShowsToolbar.title = getString(R.string.favorite_shows_title)

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewBinding.favoriteShowsRecyclerview.layoutManager = layoutManager
        viewBinding.favoriteShowsRecyclerview.adapter = showRecyclerViewAdapter

        lifecycleScope.launch {
            viewModel.favoriteShowsUiState.collect {
                when (it) {
                    is FavoriteShowsUiState.Loading -> viewBinding.progressIndicator.visibility =
                        View.VISIBLE
                    is FavoriteShowsUiState.Success -> {
                        viewBinding.progressIndicator.visibility = View.GONE
                        showRecyclerViewAdapter.submitList(it.shows)
                    }
                    is FavoriteShowsUiState.Failure -> {
                        viewBinding.progressIndicator.visibility = View.GONE
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

        viewModel.retrieveFavoriteShows()
    }

}