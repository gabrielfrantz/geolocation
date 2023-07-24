package com.example.geolocation.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

sealed class ActionState<P, R>
data class ActionRunning<P, R>(val progress: P) : ActionState<P, R>()
data class ActionError<P, R>(val error: Throwable) : ActionState<P, R>()
data class ActionComplete<P, R>(val result: R) : ActionState<P, R>()

typealias ActionObserver<P, R> = (ActionState<P, R>) -> Boolean

class ActionLiveData<P, R> : MutableLiveData<ActionState<P, R>>() {
    private var handled = false

    override fun setValue(value: ActionState<P, R>?) {
        handled = false
        super.setValue(value)
    }

    fun observeAction(owner: LifecycleOwner, observer: ActionObserver<P, R>): Unit =
            observe(owner) { if (!handled) handled = observer(it) }
}

fun <P, R> CoroutineScope.launchAction(
        liveData: ActionLiveData<P, R>,
        initialProgress: P,
        coroutineContext: CoroutineContext = EmptyCoroutineContext,
        action: suspend (setProgress: (P) -> Unit) -> R
) {
    if (liveData.value is ActionRunning)
        return

    liveData.postValue(ActionRunning(initialProgress))
    launch(coroutineContext) {
        try {
            val setProgress = { progress: P -> liveData.postValue(ActionRunning(progress)) }
            val result = action(setProgress)
            liveData.postValue(ActionComplete(result))
        } catch (ex: Exception) {
            liveData.postValue(ActionError(ex))
        }
    }
}