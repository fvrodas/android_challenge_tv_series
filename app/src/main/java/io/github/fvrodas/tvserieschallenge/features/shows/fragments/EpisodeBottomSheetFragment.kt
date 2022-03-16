package io.github.fvrodas.tvserieschallenge.features.shows.fragments

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.fvrodas.core.domain.entities.EpisodeEntity
import io.github.fvrodas.tvserieschallenge.R
import io.github.fvrodas.tvserieschallenge.databinding.FragmentEpisodeBottomSheetBinding

private const val ARG_EPISODE = "arg_episode"


class EpisodeBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var episode: EpisodeEntity

    private lateinit var viewBinding: FragmentEpisodeBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            episode = it.getSerializable(ARG_EPISODE) as EpisodeEntity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentEpisodeBottomSheetBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.episodeNameTextview.text = episode.name
        viewBinding.episodeNumberTextview.text =
            resources.getString(R.string.episode_template, episode.number)
        viewBinding.seasonTextview.text =
            resources.getString(R.string.season_template, episode.season)
        episode.summary?.let {
            viewBinding.episodeSummaryTextview.text = HtmlCompat.fromHtml(
                it,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        } ?: kotlin.run {
            viewBinding.episodeSummaryTextview.text = getString(R.string.summary_not_available)
        }


        episode.image?.let {
            Glide.with(this).load(Uri.parse(it)).into(viewBinding.episodePosterImageview)
        } ?: kotlin.run {
            viewBinding.episodePosterImageview.apply {
                setImageResource(R.drawable.ic_shows)
                imageTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.darker_gray, null))
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(episode: EpisodeEntity) =
            EpisodeBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_EPISODE, episode)
                }
            }
    }
}