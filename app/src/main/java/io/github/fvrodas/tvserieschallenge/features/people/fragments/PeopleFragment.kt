package io.github.fvrodas.tvserieschallenge.features.people.fragments

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import io.github.fvrodas.core.domain.entities.PersonEntity
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.ListFragmentBinding
import io.github.fvrodas.tvserieschallenge.features.people.activities.PersonDetailsActivity
import io.github.fvrodas.tvserieschallenge.features.people.viewmodels.PeopleUiState
import io.github.fvrodas.tvserieschallenge.features.people.viewmodels.PeopleViewModel
import io.github.fvrodas.tvserieschallenge.features.people.adapters.PeopleRecyclerViewAdapter
import io.github.fvrodas.tvserieschallenge.features.people.viewmodels.PAGE_NONE
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val SPAN_COUNT: Int = 3

class PeopleFragment : Fragment() {

    companion object {
        fun newInstance() = PeopleFragment()
    }

    private val viewModel: PeopleViewModel by viewModel()

    private lateinit var viewBinding: ListFragmentBinding

    private val peopleRecyclerViewAdapter = PeopleRecyclerViewAdapter(object :
        PeopleRecyclerViewAdapter.Companion.PeopleRecyclerViewAdapterListener {
        override fun onItemPressed(person: PersonEntity) {
            Intent(requireContext(), PersonDetailsActivity::class.java).apply {
                putExtra(PersonDetailsActivity.EXTRA_PERSON, person)
                startActivity(this)
            }
        }

    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = ListFragmentBinding.inflate(inflater, container, false)

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager =
            GridLayoutManager(requireContext(), SPAN_COUNT, GridLayoutManager.VERTICAL, false)

        viewBinding.showsRecyclerView.layoutManager = layoutManager
        viewBinding.showsRecyclerView.adapter = peopleRecyclerViewAdapter

        viewBinding.includedToolbar.searchToolbar.background =
            ColorDrawable(resources.getColor(R.color.darker_gray, null))

        lifecycleScope.launch {
            viewModel.peopleUiState.collect {
                when (it) {
                    is PeopleUiState.Loading -> viewBinding.progressIndicator.visibility =
                        View.VISIBLE
                    is PeopleUiState.Success -> {
                        viewBinding.pagerConstraintLayout.visibility =
                            if (it.pageNumber == PAGE_NONE) View.GONE else View.VISIBLE
                        viewBinding.currentPageTextView.text =
                            getString(R.string.page_template, (it.pageNumber + 1))
                        viewBinding.progressIndicator.visibility = View.GONE
                        peopleRecyclerViewAdapter.submitList(it.people)
                    }
                    is PeopleUiState.Failure -> {
                        viewBinding.progressIndicator.visibility = View.GONE
                        Toast.makeText(context, it.error, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

        viewBinding.includedToolbar.searchView.queryHint =
            resources.getString(R.string.people_query_hint)

        viewBinding.includedToolbar.searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchShowByName(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.retrieveCurrentPage()
                }
                return true
            }
        })

        viewBinding.previousButton.setOnClickListener {
            viewModel.retrievePreviousPage()
        }

        viewBinding.nextButton.setOnClickListener {
            viewModel.retrieveNextPage()
        }
    }

}