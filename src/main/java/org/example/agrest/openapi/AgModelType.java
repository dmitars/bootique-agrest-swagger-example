package org.example.agrest.openapi;

import com.fasterxml.jackson.databind.JavaType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Helper class, as Swagger may return annotated types as one of Jackson JavaType, ParameterizedType or Class
 */
interface AgModelType {

    static AgModelType forType(Type type) {

        if (type instanceof JavaType) {
            return new JacksonAgModelType((JavaType) type);
        } else if (type instanceof ParameterizedType) {
            return new ParameterizedAgModelType((ParameterizedType) type);
        } else if (type instanceof Class) {
            return new ClassAgModelType((Class<?>) type);
        } else {
            return null;
        }
    }

    Type getType();

    Class<?> getRawClass();

    AgModelType containedType(int index);

    class JacksonAgModelType implements AgModelType {

        private final JavaType type;

        public JacksonAgModelType(JavaType type) {
            this.type = type;
        }

        @Override
        public JavaType getType() {
            return type;
        }

        @Override
        public Class<?> getRawClass() {
            return type.getRawClass();
        }

        @Override
        public AgModelType containedType(int index) {
            return AgModelType.forType(type.containedType(index));
        }

        @Override
        public String toString() {
            return type.toString();
        }
    }

    class ParameterizedAgModelType implements AgModelType {

        private final ParameterizedType type;

        public ParameterizedAgModelType(ParameterizedType type) {
            this.type = type;
        }

        @Override
        public ParameterizedType getType() {
            return type;
        }

        @Override
        public Class<?> getRawClass() {
            return (Class<?>) type.getRawType();
        }

        @Override
        public AgModelType containedType(int index) {
            return AgModelType.forType(type.getActualTypeArguments()[index]);
        }

        @Override
        public String toString() {
            return type.toString();
        }
    }

    class ClassAgModelType implements AgModelType {

        private final Class<?> type;

        public ClassAgModelType(Class<?> type) {
            this.type = type;
        }

        @Override
        public Class<?> getType() {
            return type;
        }

        @Override
        public Class<?> getRawClass() {
            return type;
        }

        @Override
        public AgModelType containedType(int index) {
            throw new UnsupportedOperationException("Class has no contained types");
        }

        @Override
        public String toString() {
            return type.toString();
        }
    }

}
