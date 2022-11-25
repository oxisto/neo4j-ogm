/*
 * Copyright (c) 2002-2022 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4j.ogm.metadata.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.neo4j.ogm.exception.core.MappingException;
import org.neo4j.ogm.metadata.ClassInfo;
import org.neo4j.ogm.metadata.FieldInfo;
import org.neo4j.ogm.metadata.MetaData;
import org.neo4j.ogm.session.EntityInstantiator;

/**
 * Simple instantiator that uses either the no-arg constructor, without using property values,
 * or creates an instance with property population.
 *
 */
public class ReflectionEntityInstantiator implements EntityInstantiator {

    private final MetaData metadata;

    public ReflectionEntityInstantiator(MetaData metadata) {
        this.metadata = metadata;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T createInstance(Class<T> clazz, Map<String, Object> propertyValues) {
        try {
            Constructor<T> defaultConstructor = clazz.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            return defaultConstructor.newInstance();
        } catch (SecurityException | IllegalArgumentException | ReflectiveOperationException e) {
            throw new MappingException("Unable to find default constructor to instantiate " + clazz, e);
        }
    }

    public <T> T createInstanceWithConstructorArgs(Class<T> clazz, Map<String, Object> propertyValues) {
        try {
            ClassInfo classInfo = metadata.classInfo(clazz);

            Constructor<T> instantiatingConstructor = determineConstructor(clazz, propertyValues);
            instantiatingConstructor.setAccessible(true);

            Parameter[] parameters = instantiatingConstructor.getParameters();
            if (parameters.length == 0) {
                return instantiatingConstructor.newInstance();
            }
            Object[] values = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                String parameterName = parameter.getName();
                FieldInfo fieldInfo = classInfo.getFieldInfo(parameterName);
                Object value = fieldInfo.convert(propertyValues.get(parameterName));
                values[i] = value;
                propertyValues.remove(parameterName);
            }
            return instantiatingConstructor.newInstance(values);

        } catch (SecurityException | IllegalArgumentException | ReflectiveOperationException e) {
            throw new MappingException("Unable to find default constructor to instantiate " + clazz, e);
        }
    }

    private <T> Constructor<T> determineConstructor(Class<T> clazz, Map<String, Object> propertyValue) {
        Constructor<T>[] constructors = (Constructor<T>[]) clazz.getConstructors();
        Constructor<T> instantiatingConstructorCandidate = null;
        Set<String> availableProperties = propertyValue.keySet();

        int parameterMatchCount = -1;

        for (Constructor<T> constructor : constructors) {
            Parameter[] constructorParameters = constructor.getParameters();
            // if there is no chance that this parameter match count will be higher, dismiss this candidate
            if (constructorParameters.length < parameterMatchCount) {
                continue;
            }

            List<String> constructorParameterNames = Arrays.stream(constructorParameters)
                .map(Parameter::getName).collect(Collectors.toList());

            int intersectionAmount = calculateIntersectionAmount(constructorParameterNames, availableProperties);
            if (intersectionAmount > parameterMatchCount) {
                instantiatingConstructorCandidate = constructor;
                parameterMatchCount = intersectionAmount;
            }
        }
        return instantiatingConstructorCandidate;
    }

    private int calculateIntersectionAmount(Collection<String> constructorParameterNames, Collection<String> availableProperties) {
        Collection<String> availablePropertiesCopy = new HashSet<>(availableProperties);
        int existingPropertiesAmount = availablePropertiesCopy.size();
        availablePropertiesCopy.removeAll(constructorParameterNames);
        int leftOverPropertiesAmount = availablePropertiesCopy.size();

        return existingPropertiesAmount - leftOverPropertiesAmount;

    }
}
