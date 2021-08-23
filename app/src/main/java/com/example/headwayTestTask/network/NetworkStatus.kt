package com.example.headwayTestTask.network

/**
 * Network status represented class
 *
 * @property status status code
 * @property showMessage Boolean flag, which is responsible for displaying errors in the UI
 * @property message error massage string
 */
data class NetworkStatus(
    var status: Int,
    var showMessage: Boolean = false,
    var message: String = ""
) {
    companion object {
        const val SUCCESS = 2
        const val NOT_FOUND = 3
        const val ERROR = -1
        const val LOADING = 1
    }
}