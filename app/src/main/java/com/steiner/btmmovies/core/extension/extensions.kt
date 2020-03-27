@file:Suppress("NOTHING_TO_INLINE")

package com.steiner.btmmovies.core.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.content.res.use
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar

/**
 * Helper to force a when statement to assert all options are matched in a when statement.
 *
 * By default, Kotlin doesn't care if all branches are handled in a when statement. However, if you
 * use the when statement as an expression (with a value) it will force all cases to be handled.
 *
 * This helper is to make a lightweight way to say you meant to match all of them.
 *
 * Usage:
 *
 * ```
 * when(sealedObject) {
 *     is OneType -> //
 *     is AnotherType -> //
 * }.exhaustive
 */
val <T> T.exhaustive: T
    get() = this

/**
 * Convenience for callbacks/listeners whose return value indicates an event was hasBeenHandled.
 */
inline fun consume(action: () -> Unit): Boolean {
    action()
    return true
}

/**
 * Implementation of lazy that is not thread safe. Useful when you know what thread you will be
 * executing on and are not worried about synchronization.
 */
inline fun <T> lazyFast(crossinline action: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE) {
    action()
}

/**
 *
 */
inline val <reified T : ViewGroup> T.inflater
    get() = LayoutInflater.from(context)!!

/**
 * Creates and shows a [Toast] with the given [text]
 *
 * @param duration Toast duration, defaults to [Toast.LENGTH_SHORT]
 */
inline fun Fragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(requireContext(), text, duration).apply { show() }
}

/**
 * Creates and shows a [Toast] with text from a resource
 *
 * @param resId Resource id of the string resource to use
 * @param duration Toast duration, defaults to [Toast.LENGTH_SHORT]
 */
inline fun Fragment.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(requireContext(), resId, duration).apply { show() }
}

/**
 * Creates new fragment for class of type [T] with arguments across [argsBuilder]
 */
inline fun <reified T : Fragment> newFragment(argsBuilder: Bundle.() -> Unit = {}): T {
    return T::class.java.newInstance().apply {
        arguments = Bundle().apply(argsBuilder)
    }
}

/**
 *
 */
fun View.isRtl() = layoutDirection == View.LAYOUT_DIRECTION_RTL

/**
 *
 */
inline fun Context.dpToPx(dp: Float): Int {
    val displayMetrics = this.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics).toInt()
}

/**
 * Extends [SwipeRefreshLayout] to support non-direct descendant scrolling views.
 *
 * [SwipeRefreshLayout] works as expected when a scroll view is a direct child: it triggers
 * the refresh only when the view is on top. This adds a way (@link #setScrollUpChild} to
 * define which view controls this behavior.
 */
inline fun SwipeRefreshLayout.setRefreshLock(crossinline canRefresh: () -> Boolean) {
    setOnChildScrollUpCallback { _, target ->
        when {
            !canRefresh.invoke() -> true
            else -> target?.canScrollVertically(-1) ?: false
        }
    }
}

/**
 *
 */
@SuppressLint("Recycle")
fun Context.resolveThemeDimensionPixelSize(@AttrRes resId: Int): Int {
    return obtainStyledAttributes(intArrayOf(resId)).use {
        it.getDimensionPixelSizeOrThrow(0)
    }
}

/**
 *
 */
fun MenuItem.loadImageUrl(context: Context, url: String, circleCrop: Boolean = true) {
    Glide.with(context)
        .asDrawable()
        .load(url)
        .apply(
            RequestOptions
                .circleCropTransform()
                .override(context.resolveThemeDimensionPixelSize(android.R.attr.actionBarSize))
        )
        .into(object : CustomTarget<Drawable>() {

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                icon = resource
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
}

/**
 *
 */
@JvmOverloads
fun View.showSnackbar(@StringRes id: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, context.getString(id), duration).apply {
        setActionTextColor(Color.WHITE)
        show()
    }
}

/**
 *
 */
@JvmField
val DEFAULT_TRANSITION = AutoTransition().apply {
    ordering = TransitionSet.ORDERING_SEQUENTIAL
    interpolator = FastOutSlowInInterpolator()
}

/**
 *
 */
@JvmOverloads
inline fun ViewGroup.beginDelayedTransition(transition: androidx.transition.Transition? = DEFAULT_TRANSITION) {
    TransitionManager.beginDelayedTransition(this, transition)
}
