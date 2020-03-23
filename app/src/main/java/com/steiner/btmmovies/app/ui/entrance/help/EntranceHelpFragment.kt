package com.steiner.btmmovies.app.ui.entrance.help

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.app.databinding.FragmentPlaceholderBinding
import com.steiner.btmmovies.app.ui.entrance.EntranceActivity
import com.steiner.btmmovies.core.ui.BaseFragment
import timber.log.Timber

/**
 *
 */
class EntranceHelpFragment : BaseFragment(R.layout.fragment_placeholder) {

    private val viewModel: EntranceHelpViewModel by viewModels {
        viewModelFactoryCreator.create(this, arguments)
    }

    private lateinit var binding: FragmentPlaceholderBinding
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        (activity as EntranceActivity).guestSubComponent
            .inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPlaceholderBinding.bind(view)

        navController = findNavController()

        binding.toolbar.apply {
            title = getString(R.string.toolbar_entrance_help)
            setNavigationOnClickListener {
                navController.popBackStack()
            }
        }

        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        // TODO: Just for test!
        Timber.v("Created: $viewModel")
    }
}
