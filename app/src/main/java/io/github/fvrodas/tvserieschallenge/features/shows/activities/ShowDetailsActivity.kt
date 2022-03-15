package io.github.fvrodas.tvserieschallenge.features.shows.activities

import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpannable
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import io.github.fvrodas.core.domain.entities.EpisodeEntity
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.ActivityShowDetailsBinding
import io.github.fvrodas.tvserieschallenge.features.shows.adapters.EpisodesRecyclerViewAdapter
import io.github.fvrodas.tvserieschallenge.features.shows.fragments.EpisodeBottomSheetFragment
import io.github.fvrodas.tvserieschallenge.features.shows.viewmodels.ShowDetailsUiState
import io.github.fvrodas.tvserieschallenge.features.shows.viewmodels.ShowDetailsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShowDetailsActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityShowDetailsBinding

    private val viewModel: ShowDetailsViewModel by viewModel()

    private val episodesRecyclerViewAdapter = EpisodesRecyclerViewAdapter(object :
        EpisodesRecyclerViewAdapter.Companion.EpisodeRecyclerViewAdapterListener {
        override fun onItemPressed(episode: EpisodeEntity) {
            EpisodeBottomSheetFragment.newInstance(episode).show(supportFragmentManager, null)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityShowDetailsBinding.inflate(layoutInflater, null, false)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.showDetailsToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val show = intent.getSerializableExtra(EXTRA_SHOW) as ShowEntity

        title = show.name

        show.posterHQ?.let {
            Glide.with(this).load(Uri.parse(it))
                .thumbnail(Glide.with(this).load(Uri.parse(show.poster ?: "")))
                .into(viewBinding.showDetailsPosterImageview)
        }

        show.summary?.let {
            viewBinding.summaryTextview.text =
                HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }

        viewBinding.episodesRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewBinding.episodesRecyclerview.adapter = episodesRecyclerViewAdapter

        lifecycleScope.launch {
            viewModel.showDetailsUiState.collect {
                when (it) {
                    is ShowDetailsUiState.Loading -> viewBinding.progressIndicator.visibility =
                        View.VISIBLE
                    is ShowDetailsUiState.Success -> {
                        viewBinding.progressIndicator.visibility =
                            View.GONE
                        it.shows.genres?.let { g ->
                            val spannableStringBuilder = SpannableStringBuilder()
                            g.forEach { s ->
                                spannableStringBuilder.append(
                                    "\u0020$s\u0020",
                                    BackgroundColorSpan(
                                        resources.getColor(
                                            R.color.teal_tv_maze,
                                            null
                                        )
                                    ),
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )
                                spannableStringBuilder.append("\u0020\u0020")
                            }
                            viewBinding.genresTextview.text = spannableStringBuilder.toSpannable()
                        }

                        viewBinding.addToFavoritesFab.setImageResource(if (it.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_empty)

                        viewBinding.addToFavoritesFab.isClickable = !it.isFavorite

                        viewBinding.addToFavoritesFab.visibility = View.VISIBLE

                        it.shows.schedule?.let { sc ->
                            var days = sc.days.fold(initial = "", operation = { acc, s ->
                                "$acc$s "
                            })
                            days += "at ${sc.time}"
                            viewBinding.scheduleTextview.text = days
                        }

                        it.shows.episodes?.let { episodes ->
                            episodesRecyclerViewAdapter.submitList(episodes)
                        }

                    }
                    is ShowDetailsUiState.Message -> {
                        viewBinding.progressIndicator.visibility =
                            View.GONE
                        Toast.makeText(this@ShowDetailsActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

        viewBinding.addToFavoritesFab.setOnClickListener {
            viewModel.addShowToFavorites(show)
        }

        viewModel.retrieveShowDetailsById(show.id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_SHOW = "extra_show"
    }
}