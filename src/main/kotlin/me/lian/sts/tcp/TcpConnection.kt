package me.lian.sts.tcp

import me.lian.sts.data.DataRequest
import me.lian.sts.data.DataResponse
import me.lian.sts.data.Status
import me.lian.sts.exception.TcpException
import me.lian.sts.parsing.XmlParser
import java.io.IOException
import java.net.Socket
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * A tcp connection to the plugin api, that uses a blocking way to communicate.
 */
internal class TcpConnection(private val host: String) {

    private var socket: Socket? = null

    private var currentResponse: DataResponse? = null
    private var listeners = mutableListOf<(DataResponse) -> Unit>()

    private val lock = ReentrantLock()
    private val newResponse = lock.newCondition()

    private var listener: Thread? = null

    /**
     * Connects to the plugin api.
     *
     * In addition, this will start a listener thread that will listen for incoming data and
     * await the first response from the plugin api, which is just a welcome message.
     *
     * @throws TcpException If an error occurs while connecting.
     */
    fun connect() {
        try {
            socket = Socket(host, 3691)
        } catch (e: UnknownHostException) {
            throw TcpException("Unknown host.", e)
        } catch (e: IOException) {
            throw TcpException("IO error.", e)
        } catch (e: Exception) {
            throw TcpException("Unknown error.", e)
        }

        listen()

        val status = receive() as? Status
        if (status?.code != 300) throw TcpException("The server did not respond with a welcome message.")
    }

    /**
     * Listens to incoming data from the plugin api,
     * parse it and notify all listeners.
     */
    private fun listen() {
        listener = Thread {
            val lineBuffer = mutableListOf<String>()
            try {
                socket!!.getInputStream().bufferedReader().forEachLine {
                    lineBuffer.add(it)

                    // Check if all buffered lines combined can be parsed to a DataResponse, if so, parse it and notify all listeners.
                    val response = try {
                        XmlParser.parseResponse(lineBuffer.joinToString(""))
                    } catch (ex: Exception) { // If an exception is thrown, the response is illegal.
                        lineBuffer.clear()
                        null
                    }

                    if (response != null) { // If the response is null, it is illegal or not complete yet.
                        // If a response was parsed, notify all listeners and clear the buffer.
                        lock.withLock {
                            currentResponse = response
                            newResponse.signalAll()
                        }

                        listeners.forEach { it(response) }
                        lineBuffer.clear()
                    }
                }
            } catch (_: SocketException) {
                disconnect()
            }
        }.apply {
            isDaemon = true
            start()
        }
    }

    /**
     * Disconnects from the plugin api.
     */
    fun disconnect() {
        try {
            socket?.close()
            listener?.interrupt()
        } catch (_: Exception) {
        } finally {
            socket = null
            listener = null
        }
    }

    /**
     * Sends a request to the plugin api.
     *
     * @param request The request to send.
     */
    fun send(request: DataRequest) {
        val xml = XmlParser.parseRequest(request) ?: return // Null if the request is illegal.

        socket?.getOutputStream()?.writer()?.apply {
            write("$xml\n")
            flush()
        }
    }

    /**
     * Blocks the thread until a response is received and returns it.
     *
     * @return The received response.
     * @throws IllegalStateException If no response was received.
     */
    fun receive(): DataResponse {
        lock.withLock {
            newResponse.await()
            return currentResponse ?: throw IllegalStateException()
        }
    }

    /**
     * Blocks the thread until a response is received that matches the given predicate and returns it.
     *
     * If the given predicate does not match the received response, this method will call itself recursively.
     * If it fails 5 times, it will throw an [IllegalStateException].
     *
     * @param predicate The predicate that the response should match.
     * @return The received response.
     * @throws IllegalStateException If no received response matches the given predicate.
     */
    fun receive(predicate: (DataResponse) -> Boolean): DataResponse {
        repeat(5) {
            val response = receive()
            if (predicate(response)) return response
        }

        throw IllegalStateException("Could not receive a response that matches the given predicate.")
    }

    /**
     * Adds a listener that will be notified when a response is received.
     */
    fun onReceive(listener: (DataResponse) -> Unit) {
        listeners.add(listener)
    }


}