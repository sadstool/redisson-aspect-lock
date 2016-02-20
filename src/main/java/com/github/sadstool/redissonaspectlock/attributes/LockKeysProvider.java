package com.github.sadstool.redissonaspectlock.attributes;

import com.github.sadstool.redissonaspectlock.annotation.LockKey;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LockKeysProvider {

    private ExpressionParser parser;

    public LockKeysProvider() {
        this.parser = new SpelExpressionParser();
    }

    public List<String> get(Parameter[] parameters, Object[] parameterValues) {
        return IntStream.range(0, parameters.length)
                .filter(i -> parameters[i].getAnnotation(LockKey.class) != null)
                .mapToObj(i -> getValue(parameters[i], parameterValues[i]))
                .collect(Collectors.toList());
    }

    private String getValue(Parameter parameter, Object parameterValue) {
        LockKey keyAnnotation = parameter.getAnnotation(LockKey.class);

        if (keyAnnotation.value().isEmpty()) {
            return parameterValue.toString();
        } else {
            StandardEvaluationContext context = new StandardEvaluationContext(parameterValue);
            return parser.parseExpression(keyAnnotation.value()).getValue(context).toString();
        }
    }
}
