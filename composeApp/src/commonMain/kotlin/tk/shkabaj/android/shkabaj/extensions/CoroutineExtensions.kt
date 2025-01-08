package tk.shkabaj.android.shkabaj.extensions

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Extensions, which obliges to handle the error, error handling occurs through CoroutineExceptionHandler,
 *  which allows you to catch an error in child coroutines, if any.
 * @param onAction - safe action callback
 * @param onError - error callback
 * @param dispatcher - dispatcher in which the coroutine will be executed. Default is IO.
 * @param errorDispatcher - dispatcher for CoroutineExceptionHandler, in which the error will be processed. Default is Main.
 */
inline fun CoroutineScope.launchSafe(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    errorDispatcher: CoroutineDispatcher = Dispatchers.Main,
    coroutineName: String? = null,
    crossinline onError: suspend (Throwable) -> Unit,
    crossinline onAction: suspend CoroutineScope.(job: Job) -> Unit
): Job {
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        launch(context = errorDispatcher) {
            onError.invoke(throwable)
        }
    }

    var job: Job? = null

    val block: suspend CoroutineScope.() -> Unit = {
        job?.let { onAction(it) }
    }

    var coroutineContext = exceptionHandler + dispatcher

    coroutineName?.let { name ->
        coroutineContext += CoroutineName(name = name)
    }

    job = this.launch(context = coroutineContext, block = block)

    return job
}

fun CoroutineScope.launchPeriodAsync(repeatMillis: Long, force: Boolean = false,
                                     action: suspend () -> Unit) = launch {
    if (force) action()
    while (isActive) {
        delay(repeatMillis)
        if (isActive) action()
    }
}