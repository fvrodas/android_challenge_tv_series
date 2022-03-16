package io.github.fvrodas.tvserieschallenge.features.people.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.fvrodas.core.domain.entities.PersonEntity
import io.github.fvrodas.core.domain.entities.ShowEntity
import io.github.fvrodas.tvserieschallenge.databinding.ActivityPersonDetailsBinding
import io.github.fvrodas.tvserieschallenge.features.people.adapters.CrewCreditsListRecyclerViewAdapter
import io.github.fvrodas.tvserieschallenge.features.people.viewmodels.PersonDetailsUiState
import io.github.fvrodas.tvserieschallenge.features.people.viewmodels.PersonDetailsViewModel
import io.github.fvrodas.tvserieschallenge.features.shows.activities.ShowDetailsActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PersonDetailsActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityPersonDetailsBinding

    private val viewModel: PersonDetailsViewModel by viewModel()

    private val showsRecyclerViewAdapter = CrewCreditsListRecyclerViewAdapter(object :
        CrewCreditsListRecyclerViewAdapter.Companion.CrewCreditsRecyclerViewAdapterListener {
        override fun onItemPressed(show: ShowEntity) {
            Intent(this@PersonDetailsActivity, ShowDetailsActivity::class.java).apply {
                putExtra(ShowDetailsActivity.EXTRA_SHOW, show)
                startActivity(this)
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPersonDetailsBinding.inflate(layoutInflater, null, false)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.personDetailsToolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val person = intent.getSerializableExtra(EXTRA_PERSON) as PersonEntity

        title = person.name

        person.imageHQ?.let {
            Glide.with(this).load(Uri.parse(it))
                .thumbnail(
                    Glide.with(this).load(Uri.parse(person.image ?: "")).apply(
                        RequestOptions.circleCropTransform()
                    )
                ).apply(
                    RequestOptions.circleCropTransform()
                )
                .into(viewBinding.personDetailsPosterImageview)
        }


        viewBinding.showsRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewBinding.showsRecyclerview.adapter = showsRecyclerViewAdapter

        lifecycleScope.launch {
            viewModel.personDetailsUiState.collect {
                when (it) {
                    is PersonDetailsUiState.Loading -> viewBinding.progressIndicator.visibility =
                        View.VISIBLE
                    is PersonDetailsUiState.Success -> {
                        viewBinding.progressIndicator.visibility =
                            View.GONE
                        it.crewCredits.let { e ->
                            showsRecyclerViewAdapter.submitList(e)
                        }
                    }
                    is PersonDetailsUiState.Message -> {
                        viewBinding.progressIndicator.visibility =
                            View.GONE
                        Toast.makeText(this@PersonDetailsActivity, it.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
        viewModel.retrievePersonDetailsById(person.id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_PERSON = "extra_person"
    }
}