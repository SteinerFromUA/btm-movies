package com.steiner.btmmovies.core.widget

import android.content.Context
import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 *
 */
abstract class ExtendedViewHolder(itemView: View) : ViewHolder(itemView) {

    /**
     *
     */
    inline val context: Context
        get() = itemView.context

    /**
     *
     */
    inline val resources: Resources
        get() = itemView.context.resources
}

/**
 *
 */
abstract class BindableViewHolder<T : Any>(itemView: View) : ExtendedViewHolder(itemView) {

    /**
     *
     */
    abstract fun bind(item: T)
}
