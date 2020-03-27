@file:Suppress("NOTHING_TO_INLINE")

package com.steiner.btmmovies.app.ui

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *
 */
inline val Fragment.primaryNavFragment: Fragment?
    get() = if (isAdded) {
        parentFragmentManager.primaryNavigationFragment
    } else null

/**
 *
 */
inline fun Fragment.requireTargetFragment(): Fragment {
    val targetFragment = targetFragment
    if (targetFragment == null) {
        checkNotNull(context) { "Fragment $this is not attached to any Fragment or host" }
        throw IllegalStateException("Fragment $this does not have defined target Fragment")
    }
    return targetFragment
}

/**
 *
 */
inline fun Fragment.requirePrimaryNavFragment(): Fragment {
    val primaryNavigationFragment = primaryNavFragment
    if (primaryNavigationFragment == null) {
        checkNotNull(context) { "Fragment $this is not attached to any Fragment or host" }
        throw IllegalStateException("Fragment $this does not have primary navigation Fragment")
    }
    return primaryNavigationFragment
}

/**
 *
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.parentFragmentViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
) = viewModels<VM>(
    ownerProducer = { requireParentFragment() },
    factoryProducer = factoryProducer ?: { requireParentFragment().defaultViewModelProviderFactory }
)

/**
 *
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.targetFragmentViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
) = viewModels<VM>(
    ownerProducer = { requireTargetFragment() },
    factoryProducer = factoryProducer ?: { requireTargetFragment().defaultViewModelProviderFactory }
)

/**
 *
 */
@MainThread
inline fun <reified VM : ViewModel> Fragment.primaryNavFragmentViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
) = viewModels<VM>(
    ownerProducer = { requirePrimaryNavFragment() },
    factoryProducer = factoryProducer ?: {
        requirePrimaryNavFragment().defaultViewModelProviderFactory
    }
)
