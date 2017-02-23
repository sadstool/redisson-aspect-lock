package com.github.sadstool.redissonaspectlock.attributes.key

import spock.lang.Specification

class LockKeyProviderSpec extends Specification {
    def spelLockKeyProvider = Mock(SpelLockKeyProvider)
    def parameterLockKeyProvider = Mock(LockKeyComponentsProvider)
    LockKeyProvider lockKeyProvider

    def setup() {
        lockKeyProvider = new LockKeyProvider(spelLockKeyProvider, parameterLockKeyProvider)
    }

    def 'should return key'() {
        given:
        def keyDefinition = 'definition'
        def method = Test.getMethod('test')
        Object[] arguments = []

        and:
        1 * spelLockKeyProvider.get(keyDefinition, method, arguments) >> spelKey
        1 * parameterLockKeyProvider.get(method.getParameters(), arguments) >> parameterKeys

        when:
        def key = lockKeyProvider.get(keyDefinition, method, arguments)

        then:
        expectedKey == key

        where:
        spelKey | parameterKeys    | expectedKey
        ''      | ['arg1', 'arg2'] | ['arg1', 'arg2']
        null    | ['arg1', 'arg2'] | ['arg1', 'arg2']
        'spel'  | []               | ['spel']
        'spel'  | ['arg1', 'arg2'] | ['spel', 'arg1', 'arg2']
    }

    class Test {
        def test() {
        }
    }
}
