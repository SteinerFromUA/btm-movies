@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.steiner.btmmovies.core.widget

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.Adapter

/**
 *
 */
typealias Payloads = MutableList<Any>

/**
 *
 */
abstract class ExtendedAdapter<EVH : ExtendedViewHolder> : Adapter<EVH>() {

    override fun onBindViewHolder(holder: EVH, position: Int, payloads: Payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolderWithoutPayloads(holder, position)
        } else {
            onBindViewHolderWithPayloads(holder, position, payloads)
        }
    }

    /**
     *
     */
    open fun onBindViewHolderWithoutPayloads(vh: EVH, position: Int) {
    }

    /**
     *
     */
    open fun onBindViewHolderWithPayloads(vh: EVH, position: Int, payloads: Payloads) {
    }

    final override fun onBindViewHolder(holder: EVH, position: Int) {
        throw IllegalAccessException("Do not used, we prior version with payloads defining")
    }
}

/**
 *
 */
abstract class ExtendedListAdapter<T, VH : ExtendedViewHolder> : ListAdapter<T, VH> {

    constructor(config: AsyncDifferConfig<T>) : super(config)

    constructor(diffCallback: ItemCallback<T>) : super(diffCallback)

    override fun onBindViewHolder(holder: VH, position: Int, payloads: Payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolderWithoutPayloads(holder, position)
        } else {
            onBindViewHolderWithPayloads(holder, position, payloads)
        }
    }

    /**
     *
     */
    open fun onBindViewHolderWithoutPayloads(vh: VH, position: Int) {
    }

    /**
     *
     */
    open fun onBindViewHolderWithPayloads(vh: VH, position: Int, payloads: Payloads) {
    }

    public override fun getItem(position: Int): T? = super.getItem(position)

    final override fun onBindViewHolder(holder: VH, position: Int) {
        throw IllegalAccessException("Do not used, we prior version with payloads defining")
    }
}

/**
 *
 */
abstract class ExtendedPagedListAdapter<T, VH : ExtendedViewHolder> : PagedListAdapter<T, VH> {

    inline val snapshotList: List<T>
        get() = currentList?.snapshot() ?: emptyList()

    constructor(config: AsyncDifferConfig<T>) : super(config)

    constructor(diffCallback: ItemCallback<T>) : super(diffCallback)

    override fun onBindViewHolder(holder: VH, position: Int, payloads: Payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolderWithoutPayloads(holder, position)
        } else {
            onBindViewHolderWithPayloads(holder, position, payloads)
        }
    }

    /**
     *
     */
    open fun onBindViewHolderWithoutPayloads(vh: VH, position: Int) {
    }

    /**
     *
     */
    open fun onBindViewHolderWithPayloads(vh: VH, position: Int, payloads: Payloads) {
    }

    public override fun getItem(position: Int): T? = super.getItem(position)

    final override fun onBindViewHolder(holder: VH, position: Int) {
        throw IllegalAccessException("Do not used, we prior version with payloads defining")
    }
}
