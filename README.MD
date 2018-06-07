# learning-gRPC

This project is a POC for learning how to use *gRPC* and *protocol buffer* library from google, since both technologies go together.

References:
* [Protocol Buffers - Development Guide](https://developers.google.com/protocol-buffers/docs/overview)
* [gRPC Basics - Java](https://grpc.io/docs/tutorials/basic/java.html)

## Structure of the examples

### AddressBook
this is an only-protoc example. It follows the guidelines from this website:
[https://developers.google.com/protocol-buffers/docs/javatutorial]

* The *protoc* files are stored in the *src/test/resources* folder
* The generated code is in *Java*, in the "src/main/java* package.
* There are test class both in *Java* and *Kotlin*, in the packages *src/test/java* and *src/test/kotlin* packages

#### Source code structure

* The *proto* file defining the  messages is in *src/test/resources/addressbook.proto*.
* The generated files are distributed in different packages (more about the generating proccess later):
  * */src/generated/java/com/ilozanof/learning/proto/addressBook*:
    Contains the generated classes for the Messages only.

##### Generating the Code from the *proto* file

To generate the Java files from the *protoc* files:
1. Open a *Command Line*
2. Goto to the project folder
3. for example, if the *protoc* file is *addressbook.proto", and if you run this command: 

` protoc --java_out=src/main/java src/test/resources/addressbook.proto`

, then a new Java file will be generated in 'src/main/java'. There might be also a package, depending on the content in the *protoc* file.


### RouteGuide

This example includes the definition of a gRPC Service, with several methods and all the possible combinations: Stream in the client-size, streaming in the Server-side, etc.
The example is based on a fictional Touristic Guide Application. It's based on the work done by the team in this url:



[gRPC Basics - Java. RouteGuide example Application](https://grpc.io/docs/tutorials/basic/java.html)

#### Source code structure

* The *proto* file defining the Services and the messages is in *src/test/resources/routeGuide.proto*.
* The generated files are distributed in different packages (more about the generating proccess later):
  * */src/generated/java/com/ilozanof/learning/gRPC/routeGuide*:
    Contains the generated classes for the Messages only.
  * */src/generated/main/grpc/com/ilozanof/learning/gRPC/routeGuide*:
  Contains the Generated classes for the Service, and the Sub implementations.
* Apart from the generated sources, we have some custom code in the Server and the Client Side. All of the following classes are in the */src/main/java/com/ilozanof/learning/gRPC/roputeGuide*:
  * Server side: *RouteGuideService*: Implementation of the Business logic of our Service.
  * Server side: *RouteGuideServer*: Implementation of the Server process which is responsible for running in the background accepting request for clients.
  * Server side: *RouteGuideUtil*: Utility class
  * Client side: *RouteGuideclient*: a client ready to use.
 
##### Generating the Code from the *proto* file

In order to generate the Java files from the *proto* file, we have to options:
   * a) Using the command line
   * b) Defining a *plugin* in *Gradle* so they are generated as part of the build process.
  
  By using the command line, the command should be something like this:
  
  ```
  $ protoc --plugin=protoc-gen-grpc-java \
    --grpc-java_out="$OUTPUT_FILE" --proto_path="$DIR_OF_PROTO_FILE" "$PROTO_FILE"
  ```

  But for the previous command to work, we need to have the *protoc-gen-grpc-java* installed in our system. Apart from that, having the *generating* process as part of the build process is always a better idea, so in this case we went for the option b), explained in the next chapter.
  
###### Defining the generating process as part of the *Gradle* build

This is well documented on the internet. In the *build.gradle* file we need to make some changes in oprder to use tjis plugin, and to configure it:

we add the dependency in our script:
```
buildscript {
    ...
    dependencies {
        ...
        classpath 'com.google.protobuf:protobuf-gradle-plugin:0.8.0'
    }
}
```

we apply the plugin:
```
apply plugin: 'com.google.protobuf'
```
We configure the 'src' folder for the *proto* plugin:
```
sourceSets {
    main {
        proto {
            srcDir 'src/test/resources'
            include '**/*.proto'
        }
        ...
    }
}
```

we configure the plugin execution:
```
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.2.0"
    }
    generateProtoTasks.generatedFilesBaseDir = 'src/generated'
    plugins {
        grpc {
            artifact = 'io.grpc:protoc-gen-grpc-java:1.4.0'
        }
    }
    generateProtoTasks {
        all()*.plugins {
            grpc {}
        }
    }
}
```
and that's it. We also made some other changes, like including the generated sources in the list of source folders, etc. See the *build.gradle* file for more details.



