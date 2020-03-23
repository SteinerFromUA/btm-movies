package com.steiner.btmmovies.core.ui

import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.steiner.btmmovies.core.Notice
import com.steiner.btmmovies.core.ViewModelFactory
import com.steiner.btmmovies.core.extension.exhaustive
import com.steiner.btmmovies.core.extension.showSnackbar
import com.steiner.btmmovies.core.extension.toast
import javax.inject.Inject

abstract class BaseFragment @ContentView constructor(
    @LayoutRes contentLayoutId: Int = ResourcesCompat.ID_NULL
) : Fragment(contentLayoutId) {

    @Inject
    lateinit var viewModelFactoryCreator: ViewModelFactory.Creator

    fun showNotice(notice: Notice) {
        when (notice) {
            is Notice.Toast -> {
                toast(notice.messageId); Unit
            }
            is Notice.Snackbar -> {
                view?.showSnackbar(notice.messageId)
            }
        }.exhaustive
    }
}