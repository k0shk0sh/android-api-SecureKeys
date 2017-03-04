# SecureKeys

### Description

Tiny lib (Less than 10 methods) to store constants where attackers will have a harder time to find.

This library uses an annotationProcessor to store the constants in a new file (where the constants are crypted), and via JNI it will later retrieve them decoding them inside the `.o` file.

This way the attackers cant know the encoding system (because its inside the annotation processor), neither the decoding (unless they read the assembly code of the `.o` file). 

**Note:** They can still "find" the class with the crypted constants or do a heapdump of the map inside the `.o` file. But since its crypted they will have a harder time figuring the constants out.

The annotations used for the processor are removed in compile time, so they wont be shipped to the apk :)

### Usage

**I havent uploaded yet to bintray, so currently theres no way :poop:** -> Soon will be :)

Annotate secure stuff wherever you like as:
```Java
@SecureKey(key = "client_secret", value = "my_client_secret...")
class MyClass {
  
  @SecureKey(key = "or_here", value = "00112233")
  public void myMethod() {}
  
}
```
This annotations wont be shipped with the apk, so fear not my friend :)

Possible places for annotating are:
- Classes
- Constructors
- Fields
- Methods

Thats all. Whenever you plan on using them simply call:
```Java
SecureKeys.getString("client_secret");
SecureKeys.getLong("crash_tracking_system_user_id");
SecureKeys.getDouble("time_for_destroying_the_world");
```

### Proguard

Currently the library supports transitively Proguard, so just by adding the aar you should be safe :)

### Known improvements
- Change the encrypt/decrypt system, currently its base64, but we could start using aes or some more sophisticated crypto.