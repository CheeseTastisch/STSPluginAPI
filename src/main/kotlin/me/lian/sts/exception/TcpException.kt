package me.lian.sts.exception

/**
 * An exception that is thrown when an error in the tcp connection occurs.
 */
class TcpException(message: String, cause: Throwable? = null) :
    STSException("Could not connect to tcp: $message", cause)