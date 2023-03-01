# GMAF App

This app is the result of the course **Fachpraktikum Multimedia Information Retrieval (01519)**
The topic is **Interaction concepts for Smart MMIR**
The corresponding website can be found [here](https://moodle-wrm.fernuni-hagen.de/course/view.php?id=4851)
The app can be tested as member of a test group on Google Play: https://play.google.com/apps/internaltest/4700014159958227024 

## Project requirements

The following is needed to build and run the app:

* [AndroidStudio](https://developer.android.com/studio/index.html)
    * **Note:** The latest stable version is recommended.
* At least Android SDK version 26
    * **Note:** Version 33 is recommended.
* A device with at least Android version 8 (SDK version 26) or an Emulator
    * **Note:** An installation guide can be found [here](https://developer.android.com/studio/run/emulator)
* SDK-Tools
    * Use the from Google provided SDK-Manager to download the Android SDK and SDK tools:
      1. Open Android Studio
      2. Select **Tools**, **Android** and **SDK-Manager**
      3. Install the desired Android SDK
      4. Click **SDK Tools** and install the tools from above
* Optional the Google Assistant-Plugin to test voice commands without voice
    * **Note:** An installation guide can be found [here](https://developer.android.com/guide/app-actions/test-tool#get_the_plugin)

--------------------------------------------

## Build

1. Open Android Studio
2. Click **File**, **Open** and select `$GMAFClient/` to open the project.
3. Select **Build** on the menu, then **Build APK** 

## Run

You can copy the previous built APK on your device and install it via a file explorer after 
allowing installations from unknown sources.
Alternatively you can use the green **Play-Button**, select **Run** or **Run 'app'** to build and 
install the app on a connected device or an emulator.

To connect the app to a GMAF Api instance, start the modified Rest Client with Eclipse and enter 
the corresponding url on the settings page. 
Otherwise a very small local mock of example media will be displayed and only an example query can be executed!

## Test voice commands

To test how the app handles various voice commands before it was submitted for play store 
deployment, the Google Assistant-Plugin can be used.

1. Open Android Studio
2. Click **App Actions**, **Update** and select an **App Action**
3. Select a connected device or a running emulator
4. Click **Run App Action**

To test voice commands, ensure that the Google Assistant is installed and setup properly. 
The device language and google assistant language should be set to english (us).

## Project structure

The application consists of the 5 main packages app, contextprovider, data, di, ui and viewmodel, 
whereby data is subdivided into the packages io, model, network and repository and ui into the 
packages adapter and fragment. In addition, the models for GraphCode and MMFG were taken from the 
GMAF repository, since these are used in the communication with the rest API. 

In the app package, basic mechanisms for configuring the application setup, interfaces to the OS 
and extension functions of OS components are implemented. Extension functions can be used under 
Kotlin to extend classes without having to define new classes that inherit from a base class.  
This is also where the MainActivity is located - the component of every Android application that is 
automatically created at every app start and implements all callback methods that correspond to 
certain phases of the lifecycle. This is also where the Google Assistant's intents are received.
In contextprovider the different dispatchers are implemented, which are needed for the assignment 
of a thread during the execution of coroutines. 
In data are the various data structures (model), the input/output management for data transfers 
(io and network), and the various shared repositories (repository) for holding specific data.
The di package contains the Koin modules needed for dependency injection. Koin does not use 
reflection, but provides the various dependencies at runtime via a service allocator. 
In ui all contents are implemented, which concern the user interface. This includes all screens 
and their components. The package util contains auxiliary functions for copying test files. 
In viewmodel the logical processes are mapped to handle user interactions and to interact with the 
different repositories.