package com.steiner.btmmovies.app.ui.main.home.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.app.databinding.FragmentPlaceholderBinding

/**
 *
 */
class MovieDetailsFragment : Fragment(R.layout.fragment_placeholder) {

    private val navArgs: MovieDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentPlaceholderBinding
    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPlaceholderBinding.bind(view)

        navController = findNavController()

        binding.toolbar.apply {
            title = "Movie: #${navArgs.movieId}"
            setNavigationOnClickListener {
                navController.popBackStack()
            }
        }
    }
}