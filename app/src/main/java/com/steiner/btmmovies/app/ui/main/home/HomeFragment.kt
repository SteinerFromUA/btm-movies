package com.steiner.btmmovies.app.ui.main.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.app.databinding.FragmentHomeBinding
import com.steiner.btmmovies.app.di.account.AccountSubComponent
import com.steiner.btmmovies.app.other.AccountManager
import com.steiner.btmmovies.app.ui.main.MainActivity
import com.steiner.btmmovies.core.extension.consume
import com.steiner.btmmovies.core.extension.exhaustive
import com.steiner.btmmovies.core.extension.loadImageUrl
import com.steiner.btmmovies.core.extension.toast
import com.steiner.btmmovies.core.observeEvent
import com.steiner.btmmovies.core.ui.BaseFragment
import javax.inject.Inject

/**
 *
 */
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    lateinit var accountSubComponent: AccountSubComponent

    @Inject
    lateinit var accountManager: AccountManager

    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        accountSubComponent = (activity as MainActivity).accountSubComponent
        accountSubComponent.inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentHomeBinding.bind(view)

        navController = findNavController()

        binding.tabsMoviesTypes.apply { }

        binding.toolbar.apply {
            setNavigationOnClickListener { }
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_profile -> consume { tryLogout() }
                    else -> false
                }.exhaustive
            }
        }

        binding.vpMovies.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            isUserInputEnabled = true
            adapter = HomeMovieTypesAdapter(this@HomeFragment)
        }

        TabLayoutMediator(binding.tabsMoviesTypes, binding.vpMovies) { tab, position ->
            tab.text = (binding.vpMovies.adapter as? HomeMovieTypesAdapter)?.getPageTitle(position)
        }.attach()

        subscribeToViewModel()
    }

    private fun subscribeToViewModel() {
        /**  */
        viewModel.anyNoticeEvent.observeEvent(viewLifecycleOwner) { notice ->
            showNotice(notice)
        }

        /**  */
        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            binding.toolbar.subtitle = getString(R.string.format_toolbar_sub_home, profile.name)

            profile.picture?.parameters?.url?.let { thumbnailUrl ->
                binding.toolbar.menu.findItem(R.id.menu_profile)?.loadImageUrl(
                    context = requireContext(),
                    url = thumbnailUrl
                )
            }
        }
    }

    private fun tryLogout() {
        runCatching { accountManager.logOut() }.fold(onSuccess = {
            navController.navigate(
                HomeFragmentDirections.actionHomeToActivityEntrance()
            )
            requireActivity().finish()
        }, onFailure = { throwable ->
            toast("Failed logout: ${throwable.localizedMessage}")
        })
    }
}
