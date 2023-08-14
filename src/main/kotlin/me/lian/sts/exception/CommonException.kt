package me.lian.sts.exception

/**
 * The api is not initialized yet.
 */
data object NotInitializedException : STSException("The api is not initialized yet.") {
    private fun readResolve(): Any = NotInitializedException
}

/**
 * The plugin is not registered, but tried to send a request.
 */
data object NotRegisteredException : STSException("The plugin is not registered.") {
    private fun readResolve(): Any = NotRegisteredException
}

/**
 * The send data is not valid.
 */
class IllegalDataException(message: String) : STSException("Bad request: $message")

/**
 * The given train id is invalid.
 */
class InvalidTrainIdException(trainId: Int) : STSException("Invalid train id: $trainId")

/**
 * The given train id is unknown.
 */
class UnknownTrainIdException(trainId: Int) : STSException("Unknown train id: $trainId")

/**
 * The event type is unknown.
 */
class UnknownEventTypeException(eventType: String) : STSException("Unknown event type: $eventType")

/**
 * Some error occurred with XML.
 */
class XmlException(message: String, cause: Throwable? = null) : STSException("XML error: $message", cause)

/**
 * Some other, not recoverable error on the server side
 */
class ServerException(message: String, cause: Throwable? = null) : STSException("Server error: $message", cause)