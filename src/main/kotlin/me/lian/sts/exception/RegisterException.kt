package me.lian.sts.exception

/**
 * Some error happened while registering the plugin.
 */
class RegisterException(message: String, cause: Throwable? = null) :
    STSException("Could not register plugin: $message", cause)