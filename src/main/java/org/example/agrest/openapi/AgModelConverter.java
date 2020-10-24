package org.example.agrest.openapi;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.core.util.RefUtils;
import io.swagger.v3.oas.models.media.Schema;

import java.util.Iterator;

public abstract class AgModelConverter implements ModelConverter {

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {

        Schema existing = context.resolve(type);
        if (existing != null) {
            return existing;
        }

        AgModelType wrapped = AgModelType.forType(type.getType());
        return willResolve(type, context, wrapped)
                ? doResolve(type, context, wrapped)
                : defaultResolve(type, context, chain);
    }

    protected abstract boolean willResolve(AnnotatedType type, ModelConverterContext context, AgModelType wrapped);

    protected abstract Schema doResolve(AnnotatedType type, ModelConverterContext context, AgModelType wrapped);

    protected Schema defaultResolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
    }

    protected Schema onSchemaResolved(AnnotatedType type, ModelConverterContext context, Schema resolved) {
        context.defineModel(resolved.getName(), resolved);
        return type.isResolveAsRef()
                ? new Schema().$ref(RefUtils.constructRef(resolved.getName()))
                : resolved;
    }
}
