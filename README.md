# SecureKeys

### Description

Tiny lib to store constants where attackers will have a harder time to find.

This library uses an annotationProcessor to store the constants in a new file (where the constants are crypted), and via JNI it will later retrieve them decoding them inside the `.o` file.

This way the attackers cant know the encoding system (because its inside the annotation processor), neither the decoding (unless they read the assembly code of the `.o` file). 

**Note:** They can still "find" the class with the crypted constants or do a heapdump of the map inside the `.o` file. But since its crypted they will have a harder time figuring the constants out.

The annotations used for the processor are removed in compile time, so they wont be shipped to the apk :)

### Usage

WIP.
