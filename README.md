# SecureKeys

[![CircleCI](https://circleci.com/gh/saantiaguilera/android-api-SecureKeys/tree/develop.svg?style=svg)](https://circleci.com/gh/saantiaguilera/android-api-SecureKeys/tree/develop)

A tiny lib (Less than 10 methods) to store constants where attackers will have a harder time to find.

### Description

This library uses an annotationProcessor to store the constants in a new file (where the constants are encrypted), and via JNI it will later retrieve them decoding them inside the `.o` file.

This way the attackers cant know the encoding system (because its inside the annotation processor), neither the decoding (unless they read the assembly code of the `.o` file). 

**Note:** They can still "find" the class with the crypted constants or do a heapdump of the map inside the `.o` file. But since its encrypted they will have a harder time figuring the constants out.

### Relevant notes

- The annotations used for the processor are removed in compile time, so they wont be shipped to the apk :)
- The generated class by the apt will be shipped inside your apk, but all the constants will be already encrypted. (attacker could still do a heapdump to know the encrypted constants)
- Current encryption system is AES (CBC + Padding5) + Base64. AES key and vector are private and local to the repository (planning to make them customizable)

### Usage

**I havent uploaded yet to bintray, so currently theres no way :poop:** -> Soon will be :)

Annotate secure stuff wherever you like as:
```Java
@SecureKeys({
    @SecureKey(key = "client_secret", value = "my_client_secret..."),
    @SecureKey(key = "another_one_here", value = "...")
})
class MyClass {
  
  @SecureKey(key = "or_here_a_single_one", value = "...")
  public void myMethod() {}
  
}
```
This annotations wont be shipped with the apk, so fear not my friend :)

Possible places for annotating are:
- Classes
- Constructors
- Fields
- Methods

Thats all. Whenever you plan on using them simply call one of:
```Java
SecureEnvironment.getString("client_secret");
SecureEnvironment.getLong("crash_tracking_system_user_id");
SecureEnvironment.getDouble("time_for_destroying_the_world");
```

### Code generation

Generated code for this 2 annotations:
```Java
@SecureKey(key = "client-secret", value = "aD98E2GEk23TReYds9Zs9zdSdDBi23EAsdq29fXkpsDwp0W+h")
@SecureKey(key = "key22", value = "value2")
```
Will look like this:
```Java
   ...
   L1
    LINENUMBER 9 L1
    ALOAD 0
    ICONST_0
    // This string is "client-secret"
    LDC "c+Ciy4ZAfaCkSK3aBgVCDg==;;;;jUvAlWYtbJJXOB5PWy1NMsgtAjOcBYdZpSgWcvBjnfwXtmyCsMFnPHeM4CrLdYPO2xmk2IAnOGhlsVn55eV6wA=="
    AASTORE
   L2
    LINENUMBER 10 L2
    ALOAD 0
    ICONST_1
    // This string is "key22"
    LDC "zNgp44YJZLppymmiBdEL8A==;;;;Hk6WGAWtAPtVZYENYVUhkg=="
    AASTORE
   L3
   ...
```

### Proguard

Currently the library supports transitively Proguard, so just by adding the aar you should be safe :)

### Benchmarks

Benchmark was ran on a Samsung Fame Lite (pretty old phone):
 * Android 4.1.2
 * CPU 1Ghz Single-Core ARM Cortex-A9
 * 512MB RAM
 * ISA ARMv7
 
There were 5000 different keys encoded, and was tested 100 times the initializing of the library and the retrieval of a key (the 5000th)

**Time to initialize the library** (ms): 209

**Time to retrieve the 5000th key** (ms): 144

### Missing features:
- [x] Change the encrypt/decrypt system, currently its base64, but we could start using aes or some more sophisticated crypto.
- [ ] Let the consumer set their own AES key (this is tricky, key shouldnt be exposed to APK but should be visible for apt AND JNI)
- [ ] Do tests
- [x] Set a CI for the repository
- [x] Post benchmarks