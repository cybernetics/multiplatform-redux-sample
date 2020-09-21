package ch.dreipol.multiplatform.reduxsample.shared.redux

import ch.dreipol.dreimultiplatform.defaultDispatcher
import ch.dreipol.dreimultiplatform.reduxkotlin.presenterEnhancer
import ch.dreipol.dreimultiplatform.uiDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.reduxkotlin.*
import kotlin.coroutines.CoroutineContext

/*
 * Middleware that moves rest of the middleware/reducer chain to a coroutine using the given context.
 */
fun <State> coroutineDispatcher(context: CoroutineContext): Middleware<State> {
    val scope = CoroutineScope(context)
    return middleware { _, next, action ->
        scope.launch {
            next(action)
        }
    }
}

class ReduxSampleApp {
    val store = createThreadSafeStore(
        rootReducer,
        AppState.INITIAL_STATE,
        compose(
            listOf(
                presenterEnhancer(uiDispatcher),
                applyMiddleware(
                    coroutineDispatcher(defaultDispatcher),
                    loggerMiddleware,
                    createThunkMiddleware(),
                    disposalsMiddleware(),
                ),
            )
        )
    )
}