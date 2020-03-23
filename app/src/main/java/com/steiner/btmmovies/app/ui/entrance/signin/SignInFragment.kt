package com.steiner.btmmovies.app.ui.entrance.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginBehavior
import com.steiner.btmmovies.app.R
import com.steiner.btmmovies.app.databinding.FragmentSignInBinding
import com.steiner.btmmovies.app.ui.entrance.EntranceActivity
import com.steiner.btmmovies.core.observeEvent
import com.steiner.btmmovies.core.ui.BaseFragment

/**
 *
 */
class SignInFragment : BaseFragment(R.layout.fragment_sign_in) {

    private val viewModel: SignInViewModel by activityViewModels {
        viewModelFactoryCreator.create(this, requireActivity().intent.extras)
    }

    private lateinit var binding: FragmentSignInBinding
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        (activity as EntranceActivity).guestSubComponent
            .inject(this)

        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSignInBinding.bind(view)

        navController = findNavController()

        binding.fbLogin.apply {
            fragment = this@SignInFragment
            loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK
            setPermissions(LOGIN_PERMISSIONS)
        }

        binding.goToHelpPage.setOnClickListener {
            navController.navigate(
                SignInFragmentDirections.actionSignInToEntranceHelp()
            )
        }

        subscribeToViewModel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        lifecycleScope.launchWhenStarted {
            viewModel.fbCbManager.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun subscribeToViewModel() {
        /**  */
        viewModel.anyNoticeEvent.observeEvent(viewLifecycleOwner) { notice ->
            showNotice(notice)
        }

        /**  */
        viewModel.signedInEvent.observeEvent(viewLifecycleOwner) {
            navController.navigate(
                SignInFragmentDirections.actionSignInToActivityMain()
            )
            requireActivity().finish()
        }
    }

    companion object {
        private val LOGIN_PERMISSIONS = listOf("email", "public_profile")
    }
}