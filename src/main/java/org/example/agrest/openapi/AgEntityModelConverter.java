package org.example.agrest.openapi;

import com.fasterxml.jackson.databind.type.SimpleType;
import io.agrest.meta.AgAttribute;
import io.agrest.meta.AgEntity;
import io.agrest.runtime.meta.IMetadataService;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.apache.cayenne.di.Inject;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class AgEntityModelConverter implements ModelConverter {

    public static final String BINDING_ENTITY_PACKAGES = "openapi-entity";

    private final IMetadataService metadataService;
    private final List<String> entityPackages;

    public AgEntityModelConverter(
            @Inject IMetadataService metadataService,
            @Inject(BINDING_ENTITY_PACKAGES) List<String> entityPackages) {
        this.metadataService = Objects.requireNonNull(metadataService);
        this.entityPackages = entityPackages;
    }

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {


        if (type.getType() instanceof SimpleType) {

            // Since IMetadataService would lazily compile an entity from any Java class,
            // we need to start with a more deterministic filter for the model classes

            Class<?> maybeEntity = ((SimpleType) type.getType()).getRawClass();
            if (entityPackages.contains(maybeEntity.getPackage().getName())) {
                return resolve(maybeEntity, type, context);
            }
        }

        return chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
    }

    protected <T> Schema<T> resolve(Class<T> entityType, AnnotatedType type, ModelConverterContext context) {
        Schema<T> existing = context.resolve(type);
        if(existing != null) {
            return existing;
        }

        Schema<T> schema = new Schema<>();

        AgEntity<?> agEntity = metadataService.getAgEntity(entityType);
        schema.setName(agEntity.getName());

        for (AgAttribute a : agEntity.getAttributes()) {

        }

        return schema;
    }

    // implementing equals/hashCode to be able to detect previously installed converters in the static context

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgEntityModelConverter that = (AgEntityModelConverter) o;
        return metadataService.equals(that.metadataService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadataService);
    }
}
