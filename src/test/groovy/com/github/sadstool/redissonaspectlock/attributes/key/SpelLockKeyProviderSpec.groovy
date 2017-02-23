package com.github.sadstool.redissonaspectlock.attributes.key

import spock.lang.Specification

class SpelLockKeyProviderSpec extends Specification {
    SpelLockKeyProvider spelKeyProvider

    def setup() {
        spelKeyProvider = new SpelLockKeyProvider()
    }

    def 'should return key'() {
        given:
        Class[] parameterTypes = [String, Integer]
        def method = Test.getDeclaredMethod('test', parameterTypes)

        and:
        def argument1 = 'hello'
        def argument2 = 'world'
        Object[] arguments = [argument1, argument2]

        when:

        def key = spelKeyProvider.get('#arg1 + " " + #arg2', method, arguments)

        then:
        "$argument1 $argument2" == key
    }

    class Test {
        def test(String arg1, Integer arg2) {
        }
    }
}
