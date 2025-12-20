package com.github.farzadazimipour.kstate.core

/**
 * Simple logger interface for KState
 */
interface Logger {
    fun log(message: String)
    fun error(message: String, throwable: Throwable? = null)
}

/**
 * Default logger implementation that prints to console
 */
object DefaultLogger : Logger {
    @Synchronized
    override fun log(message: String) {
        println("[KState] $message")
    }
    
    @Synchronized
    override fun error(message: String, throwable: Throwable?) {
        println("[KState ERROR] $message")
        throwable?.printStackTrace()
    }
}

/**
 * No-op logger for production or when logging is disabled
 */
object NoOpLogger : Logger {
    override fun log(message: String) {}
    override fun error(message: String, throwable: Throwable?) {}
}