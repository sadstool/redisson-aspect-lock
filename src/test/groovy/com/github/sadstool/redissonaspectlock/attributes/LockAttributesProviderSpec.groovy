package com.github.sadstool.redissonaspectlock.attributes

import com.github.sadstool.redissonaspectlock.annotation.LockKey
import com.github.sadstool.redissonaspectlock.annotation.Lockable
import com.github.sadstool.redissonaspectlock.attributes.configuration.LockConfiguration
import com.github.sadstool.redissonaspectlock.attributes.configuration.LockConfigurationProvider
import com.github.sadstool.redissonaspectlock.attributes.key.LockKeyProvider
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.reflect.MethodSignature
import spock.lang.Specification

class LockAttributesProviderSpec extends Specification {

    private static final LOCK_NAME = 'lock'
    private static final WAIT_TIME = 1000L
    private static final LEASE_TIME = 2000L
    private static final METHOD_NAME = 'test'
    private static final CONFIGURATION = new LockConfiguration(WAIT_TIME, LEASE_TIME)
    private static final Class[] PARAMETER_TYPES = [String, String]

    def lockKeyProvider = Mock(LockKeyProvider)
    def lockConfigurationProvider = Mock(LockConfigurationProvider)
    LockAttributesProvider lockAttributesProvider

    def joinPoint = Mock(ProceedingJoinPoint)
    def methodSignature = Mock(MethodSignature)

    def setup() {
        lockAttributesProvider = new LockAttributesProvider(lockKeyProvider, lockConfigurationProvider)
    }

    def 'should return attributes'() {
        given:
        def method = Test.getMethod(METHOD_NAME, PARAMETER_TYPES)
        Object[] arguments = ['arg1', 'arg2']

        and:
        1 * joinPoint.signature >> methodSignature
        1 * methodSignature.method >> method

        and:
        1 * lockConfigurationProvider.getConfiguration(LOCK_NAME) >> CONFIGURATION
        1 * lockKeyProvider.get(null, method, arguments) >> arguments
        1 * joinPoint.args >> arguments

        when:
        def attributes = lockAttributesProvider.get(joinPoint)

        then:
        1 == attributes.size()

        and:
        with(attributes.first()) {
            LOCK_NAME == name
            "lock.$LOCK_NAME.${arguments[0]}.${arguments[1]}" == path
            WAIT_TIME == waitTime
            LEASE_TIME == leaseTime
            arguments == keys
        }
    }

    def 'should return multiple attributes'() {
        given:
        def waitTime = 3000L
        def leaseTime = 4000L

        and:
        def method = TestMultiple.getMethod(METHOD_NAME, PARAMETER_TYPES)
        def className = TestMultiple.simpleName
        Object[] arguments = ['arg1', 'arg2']
        def key1 = 'key1'
        def key2 = 'key2'

        and:
        1 * joinPoint.signature >> methodSignature
        2 * methodSignature.method >> method
        1 * methodSignature.declaringTypeName >> className

        and:
        1 * lockConfigurationProvider.getConfiguration("$className.$METHOD_NAME") >> CONFIGURATION
        1 * lockKeyProvider.get('#arg1', method, arguments) >> [key1]
        1 * lockKeyProvider.get('#arg2', method, arguments) >> [key2]
        1 * joinPoint.args >> arguments

        when:
        def attributes = lockAttributesProvider.get(joinPoint)

        then:
        2 == attributes.size()

        and:
        with(attributes.first()) {
            "$className.$METHOD_NAME" == name
            "lock.$className.$METHOD_NAME.$key1" == path
            waitTime == it.waitTime
            leaseTime == it.leaseTime
            [key1] == keys
        }

        and:
        with(attributes.last()) {
            "$className.$METHOD_NAME" == name
            "lock.$className.$METHOD_NAME.$key2" == path
            waitTime == waitTime
            leaseTime == it.leaseTime
            [key2] == keys
        }
    }

    class Test {
        @Lockable('lock')
        def test(@LockKey String arg1, @LockKey String arg2) {
        }
    }

    class TestMultiple {
        @Lockable(key = ['#arg1', '#arg2'], waitTime = 3000L, leaseTime = 4000L)
        def test(String arg1, String arg2) {
        }
    }
}
