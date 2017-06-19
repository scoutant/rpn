package org.scoutant.rpn

/**
 * Improving findViewById with Kotlin
 * https://medium.com/@quiro91/improving-findviewbyid-with-kotlin-4cf2f8f779bb
 * Usage :
 * private val display: TextView by bind(R.id.display)
 */
//fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
//    @Suppress("UNCHECKED_CAST")
//    return unsafeLazy { findViewById(idRes) as T }
//}
//
//fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
//    @Suppress("UNCHECKED_CAST")
//    return unsafeLazy { findViewById(idRes) as T }
//}

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)
