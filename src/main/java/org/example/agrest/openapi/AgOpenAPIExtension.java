package org.example.agrest.openapi;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.type.SimpleType;
import io.agrest.base.protocol.Dir;
import io.swagger.v3.jaxrs2.ResolvedParameter;
import io.swagger.v3.jaxrs2.ext.AbstractOpenAPIExtension;
import io.swagger.v3.jaxrs2.ext.OpenAPIExtension;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

public class AgOpenAPIExtension extends AbstractOpenAPIExtension {

    private static final String QUERY_PARAM = "query";

    // TODO: Agrest should define these publicly somewhere
    private static final String PROTOCOL_CAYENNE_EXP = "cayenneExp";
    private static final String PROTOCOL_DIR = "dir";
    private static final String PROTOCOL_EXCLUDE = "exclude";
    private static final String PROTOCOL_INCLUDE = "include";
    private static final String PROTOCOL_LIMIT = "limit";
    private static final String PROTOCOL_MAP_BY = "mapBy";
    private static final String PROTOCOL_SORT = "sort";
    private static final String PROTOCOL_START = "start";

    @Override
    public ResolvedParameter extractParameters(
            List<Annotation> annotations,
            Type type,
            Set<Type> typesToSkip,
            Components components,
            Consumes classConsumes,
            Consumes methodConsumes,
            boolean includeRequestBody,
            JsonView jsonViewAnnotation,
            Iterator<OpenAPIExtension> chain) {

        ResolvedParameter resolved = null;
        if (type instanceof SimpleType && UriInfo.class.equals(((SimpleType) type).getRawClass())) {
            resolved = extractParametersForUriInfo(annotations);
        }

        return resolved != null ? resolved : super.extractParameters(
                annotations,
                type,
                typesToSkip,
                components,
                classConsumes,
                methodConsumes,
                includeRequestBody,
                jsonViewAnnotation,
                chain);
    }

    protected ResolvedParameter extractParametersForUriInfo(List<Annotation> annotations) {

        for (Annotation a : annotations) {
            if (a instanceof Context) {

                // "@Context UriInfo" resolves to the full set of agrest protocol keys
                ResolvedParameter resolved = new ResolvedParameter();

                Parameter include = new Parameter();
                // TODO: schema
                include.setIn(QUERY_PARAM);
                include.setName(PROTOCOL_INCLUDE);
                include.setDescription("Property path to include in the response");
                resolved.parameters.add(include);

                Parameter exclude = new Parameter();
                exclude.setIn(QUERY_PARAM);
                exclude.setName(PROTOCOL_EXCLUDE);
                exclude.setDescription("Property path to exclude from the response");
                resolved.parameters.add(exclude);

                Parameter sort = new Parameter();
                // TODO: schema
                sort.setIn(QUERY_PARAM);
                sort.setName(PROTOCOL_SORT);
                sort.setDescription("Defines result sorting. May be used in conjunction with 'dir' parameter");
                resolved.parameters.add(sort);

                Schema dirSchema = new Schema();
                dirSchema.setType("string");
                dirSchema.setEnum(asList(Dir.ASC.name(), Dir.ASC_CI.name(), Dir.DESC.name(), Dir.DESC_CI.name()));
                Parameter dir = new Parameter();
                dir.setIn(QUERY_PARAM);
                dir.setName(PROTOCOL_DIR);
                dir.setDescription("Defines result sort direction. Must be used in conjunction with 'sort' parameter");
                dir.setSchema(dirSchema);
                resolved.parameters.add(dir);

                Parameter cayenneExp = new Parameter();
                // TODO: schema
                cayenneExp.setIn(QUERY_PARAM);
                cayenneExp.setName(PROTOCOL_CAYENNE_EXP);
                cayenneExp.setDescription("Expression for result filtering");
                resolved.parameters.add(cayenneExp);

                Parameter mapBy = new Parameter();
                mapBy.setIn(QUERY_PARAM);
                mapBy.setName(PROTOCOL_MAP_BY);
                mapBy.setDescription("Property path to use as a key, turning result into a map");
                resolved.parameters.add(mapBy);

                Parameter start = new Parameter();
                start.setIn(QUERY_PARAM);
                start.setName(PROTOCOL_START);
                start.setDescription("How many objects to skip from the beginning of the result list. Used to control pagination");
                resolved.parameters.add(start);

                Parameter limit = new Parameter();
                limit.setIn(QUERY_PARAM);
                limit.setName(PROTOCOL_LIMIT);
                limit.setDescription("Max objects to include in the result list. Used to control pagination");
                resolved.parameters.add(limit);

                return resolved;
            }
        }

        return null;
    }
}
