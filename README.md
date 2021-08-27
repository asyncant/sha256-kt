sha256-kt
=========

Pure Kotlin SHA-256 hash algorithm implementation that can be used on any Kotlin target platform (js, jvm, native).

Ported from C implementation by Brad Conte, <https://github.com/B-Con/crypto-algorithms>.

Like the original C implementation, this code is released into the public domain.

Usage
-----

```kotlin
import com.asyncant.crypto.sha256

val input = "Hello world!".encodeToByteArray()
val hash = sha256(input)
```

Download
--------

The library can be found on maven central [here](https://search.maven.org/artifact/com.asyncant.crypto/sha256-kt).

### Gradle

```kotlin
implementation("com.asyncant.crypto:sha256-kt:1.0")
```

### Maven

```xml
<dependency>
    <groupId>com.asyncant.crypto</groupId>
    <artifactId>sha256-kt</artifactId>
    <version>1.0</version>
</dependency>
```

Contributing || Contact
-----------------------

For public matters, feel free to create issues or pull requests on the GitHub project.

For other matters, e.g. security issues, you can e-mail me at `<project>@<github-username>.com`.
