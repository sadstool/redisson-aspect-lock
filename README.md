# redisson-aspect-lock

## How to start

```groovy
repositories {
    mavenCentral()
}
dependencies {
	compile('com.github.sadstool:redisson-aspect-lock:0.1.0')
	compile('org.springframework.boot:spring-boot-starter-aop')
}
```

RedissonClient bean will be registered with standard spring `spring.redis` properties.

## Lock by method

```java
    @Lockable
    public void someImportantFeature() {
        ...
    }
```

## Lock by name

```java
    @Lockable("oneAtTime")
    public void someImportantFeature() {
        ...
    }

    @Lockable("oneAtTime")
    public void otherDependentFeature() {
        ...
    }
```

## Lock by keys

```java
    @Lockable
    public void someImportantFeature(@LockKey int someId, @LockKey int otherId) {
        ...
    }
```

## Lock by key with SpEL

```java
    public class Entity {
        String field1;
        String field2;
        ...
    }

    @Lockable
    public void someImportantFeature(@LockKey("field1 +'.'+ field2") Entity entity) {
        ...
    }
```

## Lock with wait and lease time

If the lock is currently held by another process, this method keeps trying to acquire it for up to `waitTime` (millis) before giving up and throwing an exception. If the lock is acquired, it is held until method ends or until `leaseTime` (millis) have passed since the lock was granted - whichever comes first.
Default `waitTime` is 0 ms. Default `leaseTime` - 10 seconds.

```java
    @Lockable(waitTime = 1000, leaseTime = 30000)
    public void someImportantFeature() {
        ...
    }
```
