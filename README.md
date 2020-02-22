#### Sheet Sharing

###### Frameworks Used
* Kotlin
* Ktor (Framework for building asynchronous servers and clients in connected systems using kotlin)
* Koin (Dependency Injection)
* jetbrains exposed
* H2 db as inmemory data storage
* uchuhimo (for Config)
* Flyway (For database migration)

###### Data Model Concept
 write something here

###### How to start?
* Please Generate self contained Jar (**gradle clean build** is good option :D )
* Start the server by java -jar build/libs/Layer.jar
* Use Curl or Insomnia (it's cool) to trigger your calls
* Code is covered by Tests (Please approach me for clarifications)-(hit **./gradlew test** will not bite :D)
* Hint , Use the following Endpoints to navigate (Don't worry you are almost covered)

      GET   "/api/init"             -> Get All bla bla bla
      POST  "/api/init"             -> doing bla bla bla
      
##### For Concurrency : 
     https://blog.mindorks.com/mastering-kotlin-coroutines-in-android-step-by-step-guide 
               (It's very cool illustration of concurrency in kotlin coroutines)






