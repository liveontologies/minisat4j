# minisat4j

Accessing the native MiniSat solver through IPASIR interface and its Java binding

This project packages native shared libraries of the [MiniSat solver](http://minisat.se) 
so that they can be used in Apache Maven projects.

## Overview and usage

This project consists of two modules:

- **minisat**: This module contains precompiled native shared libraries that implement the 
  [Reentrant Incremental Sat solver API (reverse: IPASIR)](https://github.com/biotomas/ipasir)
  used in [SAT competitions](http://www.satcompetition.org).
  The native functions can be accessed, e.g., using the 
  [Java Native Access (JNA)](https://github.com/java-native-access/jna).
  To use this library from Apache Maven, add the following dependency to your project, 
  where `<classifier>` should be replaced with JNA canonical prefix for your platform
  `{OS}-{ARCH}` (see https://github.com/java-native-access/jna/blob/master/www/GettingStarted.md).
  
  ```
	<dependency>
		<groupId>com.github.liveontologies</groupId>
		<artifactId>minisat</artifactId>
		<version>...</version>
		<classifier>linux-x86-64</classifier>
	</dependency>
  ```
  
- **minisat4j**: This module provides Java bindings for the native library using the 
[IPASIR4J library](https://github.com/liveontologies/ipasir4j) -- a Java version of
the [IPASIR C library](https://github.com/biotomas/ipasir). To use this library
add the following maven dependency:
	```
	<dependency>
		<groupId>com.github.liveontologies</groupId>
		<artifactId>minisat4j</artifactId>
		<version>...</version>
	</dependency>
	```
See `src/test/test` for examples on how to use this library.
The native library `minisat` for the current platform will be automatically added as 
a compile/runtime dependency. To include the library dependencies for all available 
platforms set a system property `multi-platform`, e.g., by adding a switch 
`-Dmulti-platform` to the maven command. This may be desirable, e.g., for creating
platform-independent "stand-alone" jars.

To use snapshots versions of this library (if not compiled from sources), please add
the sonatype snapshot repository either to your `pom.xml` or `settings.xml`:
```
<repositories>
  <repository>
    <id>ossrh</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>			
  </repository>
</repositories>
```

## Compiling from sources

To compile this software, one has to install Apache Maven version 3 or higher
and standard development tools such as Make and GCC. To build all modules, issue
the following command from this folder:  
```
mvn clean install
```

## License

All sources of this project are available under the terms of the 
[Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
(see the file `LICENSE.txt`).

In addition, the sources of the module `minisat` are available 
under the terms of the MIT license, so that it is compatible to the 
license of the minisat solver (see the file `minisat/LICENSE`).
