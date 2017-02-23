package com.github.sadstool.redissonaspectlock.attributes.key;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

public class SpelLockKeyProvider {

    private ExpressionParser parser;
    private ParameterNameDiscoverer nameDiscoverer;

    public SpelLockKeyProvider() {
        this.parser = new SpelExpressionParser();
        this.nameDiscoverer = new DefaultParameterNameDiscoverer();
    }

    public String get(String keyDefinition, Method method, Object[] parameterValues) {
        if (keyDefinition != null && !keyDefinition.isEmpty()) {
            EvaluationContext context = getContext(method, parameterValues);

            return parser.parseExpression(keyDefinition)
                    .getValue(context)
                    .toString();
        }

        return null;
    }

    private EvaluationContext getContext(Method method, Object[] parameterValues) {
        return new MethodBasedEvaluationContext(null, method, parameterValues, nameDiscoverer);
    }
}
