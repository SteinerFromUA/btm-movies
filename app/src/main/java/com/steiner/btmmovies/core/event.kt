@file:Suppress("NOTHING_TO_INLINE")

package com.steiner.btmmovies.core

import androidx.annotation.MainThread
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 *
 * [Read more about this.](https://medium.com/google-developers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150)
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Consumes the content if it's not been consumed yet and prevents its use again.
     * @return The unconsumed content or `null` if it was consumed already.
     */
    @UiThread
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Event<*>

        if (content != other.content) return false
        if (hasBeenHandled != other.hasBeenHandled) return false

        return true
    }

    override fun hashCode(): Int {
        var result = content?.hashCode() ?: 0
        result = 31 * result + hasBeenHandled.hashCode()
        return result
    }

    override fun toString(): String {
        return "Event(content=$content, hasBeenHandled=$hasBeenHandled)"
    }
}

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s content has
 * already been hasBeenHandled.
 *
 * [onEventUnhandledContent] is *only* called if the [Event]'s contents has not been hasBeenHandled.
 */
open class EventObserver<T>(
    private val onEventUnhandledContent: (T) -> Unit
) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>?) {
        event?.getContentIfNotHandled()?.run(onEventUnhandledContent)
    }
}

/**
 *
 */
@MainThread
inline fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline onEventUnhandledContent: (T) -> Unit
): EventObserver<T> {
    val wrappedObserver = EventObserver<T> {
        onEventUnhandledContent(it)
    }
    observe(owner, wrappedObserver)
    return wrappedObserver
}

/**
 * Used for easy observe events for general LifecycleOwner.
 */
@MainThread
inline fun <T : Any, L : LiveData<Event<T>>> LifecycleOwner.observeEvent(
    liveData: L,
    noinline onEventUnhandledContent: (T) -> Unit
): EventObserver<T> = liveData.observeEvent(this, onEventUnhandledContent)

/**
 * Used for easy observe events for Fragment view.
 */
@MainThread
inline fun <T : Any, L : LiveData<Event<T>>> Fragment.observeEventForView(
    liveData: L,
    noinline onEventUnhandledContent: (T) -> Unit
): EventObserver<T> = liveData.observeEvent(this.viewLifecycleOwner, onEventUnhandledContent)

/**
 *
 */
@MainThread
inline fun <T> LiveData<Event<T>>.observeEventWhenResumed(
    owner: LifecycleOwner,
    noinline onEventUnhandledContent: (T) -> Unit
): EventObserver<T> {
    val wrappedObserver =
        EventObserver(onEventUnhandledContent)
    owner.lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_RESUME) {
                observe(owner, wrappedObserver)
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                removeObserver(wrappedObserver)
            }
        }
    })
    return wrappedObserver
}

/**
 *
 */
@MainThread
inline fun <T : Any, L : LiveData<Event<T>>> LifecycleOwner.observeEventWhenResumed(
    liveData: L,
    noinline onEventUnhandledContent: (T) -> Unit
): EventObserver<T> = liveData.observeEventWhenResumed(this, onEventUnhandledContent)

/**
 *
 */
@MainThread
inline fun <T : Any, L : LiveData<Event<T>>> Fragment.observeEventForViewWhenResumed(
    liveData: L,
    noinline onEventUnhandledContent: (T) -> Unit
): EventObserver<T> = liveData.observeEvent(this.viewLifecycleOwner, onEventUnhandledContent)

/**
 *
 */
open class EventLiveData<T> : MediatorLiveData<Event<T>>() {

    inline fun setContent(content: T) {
        value = Event(content)
    }

    inline fun postContent(content: T) {
        postValue(Event(content))
    }
}

/**
 *
 */
class ActionEventLiveData : MediatorLiveData<Event<Unit>>() {

    inline fun setAction() {
        value = Event(Unit)
    }

    inline fun postAction() {
        postValue(Event(Unit))
    }
}
