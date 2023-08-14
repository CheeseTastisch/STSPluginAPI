# STSPluginAPI
The Stellwerk Simulator Plugin API is a Java and Kotlin API for the Stellwerk Simulator. 
It uses the [specification](https://doku.stellwerksim.de/doku.php?id=stellwerksim:plugins:spezifikation#simzeit)
of the Stellwerk Simulator to provide a simple way of communicating with it.

## Table of contents
- [How to use](#how-to-use)
- [Methods](#methods)
- [But why?](#but-why)
- [Sorry](#sorry)
- [Contributing & License](#contributing--license)

## How to use
To use the api, there are three possible ways:
- **BLOCKING**, use this if you just don't want to think match about how and where the request is sent. 
But remember, if you call a method with the blocking api that your programm will stop (just pause) until the request is
finished and the result is returned.
- **COMPLETABLE**, a little bit more complicated and has the big advantage that you can use it in a non-blocking way.
This way will not block your programm but instead return a `CompletableFuture`, that you can use to get the result.
- **SUSPENDING**, my favourite way, sadly only available for Kotlin. With the suspending api you have the same advantages
as with the completable api, but you don't have to work with the `CompletableFuture` directly, instead you can just
use the result.

Any way, you need to specific some basic information about the connection to the simulator, witch are
- the host,
- the name of your plugin,
- the author of your plugin (YOU),
- the version of your plugin and
- a description of your plugin.

### Blocking
To create a blocking api, just create a new instance of the `BlockingSTS` class and pass the information to it,
then call the `connect` method, and you are ready to go.

```java
class YouPlugin {

    public static void main(String[] args) {
        BlockingSTS blocking = new BlockingSTS("localhost", "A cool plugin", "you", "1.0", "A very cool plugin, that does cool things.");
        blocking.connect();
        
        // Now you can use the blocking api
        // For example:
        List<Train> trains = blocking.getTrains();
        
        // Don't forget to disconnect
        blocking.disconnect();
    }
    
}
```

We will take a look on the different methods later.

### Completable
To use the completable api, you need to create a new instance of the `CompletableSTS` class and pass the information to it,
then call the `connect` method, and you are ready to go.

```java
class YouPlugin {

    public static void main(String[] args) {
        CompletableSTS completable = new CompletableSTS("localhost", "A cool plugin", "you", "1.0", "A very cool plugin, that does cool things.");
        
        completable.connect().get(); // Call the get method to await the connection
        // OR:
        completable.connect().thenRun(() -> { // This will wait for the connection and then run the given code
            // Do something when the connection is established
        });
        
        // Now you can use the completable api
        // For example:
        completable.getTrains().thenAccept(trains -> {
            // Do something with the trains
        });
        
        // Don't forget to disconnect
        completable.disconnect().thenRun(() -> {
            // This will shut down the executor service, 
            // which is important because otherwise your programm will not stop
            completable.shutdown();
        });
    }
    
}
```

### Suspending
To use the suspending api, you need to create a new instance of the `SuspendingSTS` class and pass the information to it,
then call the `connect` method, and you are ready to go.

As already mentioned, this api is only available for Kotlin, so the example will be in Kotlin.

```kotlin
val suspending = SuspendingSTS("localhost", "A cool plugin", "you", "1.0", "A very cool plugin, that does cool things.")
suspending.connect() // This will suspend until the connection is established

val trains = suspending.getTrains() // This will suspend until the result is returned

suspending.disconnect() // This will suspend until the connection is closed
```

## Methods
There are multiple methods available, you can use all methods with all three apis,
just the way of how you handle the result and how your call is processed is different.

### connect
For all three apis, the `connect` method is the same,
it will create a connection to the simulator and send the information about your plugin to it.

### disconnect
For all three apis, the `disconnect` method is the same,
it will close the connection to the simulator.

### shutdown
This method is only available for the completable api, it will shut down the executor service,
which is important because otherwise your programm will not stop.

### getTime
This method will get the time of the simulator.
This will return a `Time`, witch contains the time in milliseconds since 0 o'clock,
however it is not advisable to use this method, look at the [getTimeHandler](#gettimehandler) method instead.

### getTimeHandler
The [getTime](#gettime) method should not always be used when you want to get the time in the simulation,
because this will create some heavy load for the simulator, instead you should use the `getTimeHandler` method.

The `getTimeHandler` returns a `TimeHandler`, witch is a class that will requre the time once from the simulation and
then count on the client side with it.

The `TimeHandler` itself has multiple methods, being
- `getMillis`, to get the time in milliseconds since 0 o'clock,
- `getTotalSeconds`, to get the time in seconds since 0 o'clock,
- `getTime`, to get the time as a `Triple` of hours, minutes and seconds,
- `getHours`, to get the hours,
- `getMinutes`, to get the minutes and
- `getSeconds`, to get the seconds.

### getSystemInformation
This method will get information about the simulator,
like the builder number, the facility the player is currently playing
and some more.

### getPlatforms
Gets all platforms of the current facility and their neighbours.

### getTrains
Gets all trains currently in the facility or which will be in the facility soon.

### getTrainDetails
Gets detailed information about a single train, like the name, delay and current platform, by its id.

### getTimetable
Gets the timetable of a single train by its id.

### getFacilityLayout
Gets the layout of the current facility.

This method is not perfectly document, because the documentation of the Stellwerk Simulator does not provide a
detailed documentation on what the different type ids are.

### onTrainEvent
With this method you can register a listener, that listens for some events of a single train.

## But why?
The Stellwerk Simulator provides its own Java API.

As I lately started to look into plugin development, I didn't really like the API.
There are multiple reasons for this, I will list some of them here.

### German and conventions
The api (to be fair, this is a more general problem of the api and not really the java api) uses a lot
of german words, like `Zug` (train) and `Gleis` (platform), witch is not really a problem but is not the
optimal way either.

I know the Stellwerk Simulator community is only german, but in programming english is the standard language,
and I think it would be better to use english words. In addition, with the german words, there are `ä`, `ö` and `ü`,
witch can not be used in variable names and so on, so the api uses `ae`, `oe` and `ue` instead, witch is not really
nice to read.

Also, it's not really constant throughout the api, somtimes its english (`PluginClient`, `request`), sometimes
its german (`Zug`, `Gleis`). The most annoying methods are the combined ones (e.g. `request_zugdetail`).

Some classe even don't comply to the Java conversions, witch should be standard for any API 
(e.g. `flagdata` or `request_...`).

### Call methods as callbacks
The current api has a design, where you execute a method (e.g. `request_zugdetail`), than the
api does its magic and makes the call, but then the result is not returned, instead a method
(e.g. `response_zugdetail`) in the `PluginClient` is called, where you get the result.

There you don't have any connection to the requesting method anymore, or anything similar,
you just get the result.

But what do to with the result?

### Extending
The current api requires you to extend the `PluginClient` class, but why?

I know, to call the callback method you need something, but why not just return the result,
or use a callback interface?

I would get the extending thing, when the api would be loaded by the simulator, but all plugins are
standalone applications.

### No documentation
The current api is not very well documents. You can somewhat guess what some things do,
but not all things. I know there is a java doc, but having
`PluginClient verlangt 4 Parameter im Konstruktor:` as a description for the `PluginClient`
doesn't tell me what a `PluginClient` is, it just tells me that I need 4 parameters.

## Sorry
I know that the official api had to take a beating from me,
at this point I just want to say 

**THANK YOU**

to the developers of the Stellwerk Simulator, it is a great simulator after all,
and I really enjoy playing it.

If you want to check out the Stellwerk Simulator, you can do so [here](https://stellwerksim.de/).

Everything I said is just my opinion, I don't want to offend or insult anyone.
Also, I'm happy to discuss my points, so if you have a different opinion, feel free to contact me!

## Contributing & License
If you want to contribute to this project, you can do so by creating a pull request,
if you have any questions, feel free to contact me.

This project is licensed under the MIT licence 
- see the [LICENSE](https://github.com/CheeseTastisch/STSPluginAPI/blob/main/LICENSE.md) file for details.