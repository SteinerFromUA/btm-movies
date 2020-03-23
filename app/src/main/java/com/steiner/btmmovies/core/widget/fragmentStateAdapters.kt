package com.steiner.btmmovies.core.widget

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.AsyncListDiffer.ListListener
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.viewpager2.adapter.FragmentStateAdapter

@Suppress("unused")
abstract class ExtendedFragmentStateAdapter : FragmentStateAdapter {

    constructor(activity: FragmentActivity) : super(activity)

    constructor(fragment: Fragment) : super(fragment)

    constructor(fm: FragmentManager, lifecycle: Lifecycle) : super(fm, lifecycle)

    open fun getPageTitle(position: Int): CharSequence? = null

    companion object {
        @JvmStatic
        fun makeFragmentName(id: Long): String = "f$id"
    }
}

@Suppress("unused")
abstract class ListFragmentStateAdapter<T> :  ExtendedFragmentStateAdapter {

    private val differ: AsyncListDiffer<T>

    private val listener = ListListener<T> { previousList, currentList ->
        onCurrentListChanged(previousList, currentList)
    }

    /**
     * Get the current List - any diffing to present this list has already been computed and
     * dispatched via the ListUpdateCallback.
     *
     * If a `null` List, or no List has been submitted, an empty list will be returned.
     *
     * The returned list may not be mutated - mutations to content must be done through
     * [.submitList].
     *
     * @return The list currently being displayed.
     *
     * @see .onCurrentListChanged
     */
    val currentList: List<T>
        get() = differ.currentList

    constructor(activity: FragmentActivity, diffCallback: ItemCallback<T>) :
            this(activity.supportFragmentManager, activity.lifecycle, diffCallback)

    constructor(activity: FragmentActivity, config: AsyncDifferConfig<T>) :
            this(activity.supportFragmentManager, activity.lifecycle, config)

    constructor(fragment: Fragment, diffCallback: ItemCallback<T>) :
            this(fragment.childFragmentManager, fragment.lifecycle, diffCallback)

    constructor(fragment: Fragment, config: AsyncDifferConfig<T>) :
            this(fragment.childFragmentManager, fragment.lifecycle, config)

    constructor(fm: FragmentManager, lifecycle: Lifecycle, diffCallback: ItemCallback<T>) :
            super(fm, lifecycle) {
        @Suppress("LeakingThis")
        differ = AsyncListDiffer(
            AdapterListUpdateCallback(this),
            AsyncDifferConfig.Builder(diffCallback).build()
        )
        differ.addListListener(listener)
    }

    constructor(fm: FragmentManager, lifecycle: Lifecycle, config: AsyncDifferConfig<T>) :
            super(fm, lifecycle) {
        @Suppress("LeakingThis")
        differ = AsyncListDiffer(AdapterListUpdateCallback(this), config)
        differ.addListListener(listener)
    }

    /**
     * Submits a new list to be diffed, and displayed.
     *
     * If a list is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     * @param list The new list to be displayed.
     */
    open fun submitList(list: List<T>?) {
        differ.submitList(list)
    }

    /**
     * Set the new list to be displayed.
     *
     * If a List is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     * The commit callback can be used to know when the List is committed, but note that it
     * may not be executed. If List B is submitted immediately after List A, and is
     * committed directly, the callback associated with List A will not be run.
     *
     * @param list The new list to be displayed.
     * @param commitCallback Optional runnable that is executed when the List is committed, if
     * it is committed.
     */
    open fun submitList(list: List<T>?, commitCallback: (() -> Unit)?) {
        differ.submitList(list, commitCallback)
    }

    override fun getItemCount(): Int = differ.currentList.size

    protected open fun getItem(position: Int): T = differ.currentList[position]

    /**
     * Called when the current List is updated.
     *
     * If a `null` List is passed to [.submitList], or no List has been
     * submitted, the current List is represented as an empty List.
     *
     * @param previousList List that was displayed previously.
     * @param currentList new List being displayed, will be empty if `null` was passed to
     * [.submitList].
     *
     * @see .getCurrentList
     */
    open fun onCurrentListChanged(previousList: List<T>, currentList: List<T>) {}
}
