package com.github.sadstool.redissonaspectlock.attributes.key

import com.github.sadstool.redissonaspectlock.annotation.LockKey
import spock.lang.Specification

class LockKeyComponentsProviderSpec extends Specification {
    LockKeyComponentsProvider lockKeyComponentsProvider

    def setup() {
        lockKeyComponentsProvider = new LockKeyComponentsProvider()
    }

    def 'should return keys'() {
        given:
        Class[] parameterTypes = [String, Integer, Double]
        def method = Test.getDeclaredMethod('test', parameterTypes)

        and:
        def argument1 = 'hello'
        def argument2 = 2
        def argument3 = 3.0
        Object[] arguments = [argument1, argument2, argument3]

        when:
        def keys = lockKeyComponentsProvider.get(method.parameters, arguments)

        then:
        [argument1, argument3.toString()] == keys
    }

    class Test {
        def test(@LockKey String arg1, Integer arg2, @LockKey Double arg3) {
        }
    }
}
