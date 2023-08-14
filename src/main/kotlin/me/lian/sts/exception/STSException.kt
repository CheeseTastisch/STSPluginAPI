package me.lian.sts.exception

/**
 * An exception that is thrown when an error in this api occurs.
 */
sealed class STSException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)