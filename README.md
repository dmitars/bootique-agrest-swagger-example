# bootique-agrest-swagger-example

Agrest OpenAPI / Swagger integration example. Requires:

* Agrest 4.1 or newer
* Bootique 2.0.B1 or newer

## Integration approach

Include `bootique-agrest-cayenne42-swagger` dependency:

```xml
<dependency>
    <groupId>io.bootique.agrest</groupId>
    <artifactId>bootique-agrest-cayenne42-swagger</artifactId>
</dependency>
```
And Swagger UI console if needed:

```xml
<dependency>
    <groupId>io.bootique.swagger</groupId>
    <artifactId>bootique-swagger-ui</artifactId>
</dependency>
```

Explicitly specify which packages are a part of the Agrest model:
```java
// important to specify Agrest model package(s) explicitly. Classes referenced from API resources that are
// not in these packages will be exposed as OpenAPI schemas generated via a default reflection mechanism,
// and will not align with Agrest model
AgrestSwaggerModule.extend(binder).addModelPackage(Book.class);
```

Configure Swagger and Swagger UI (see ["bootique-swagger" docs](https://github.com/bootique/bootique-swagger) ):
```yaml
swagger:
  specs:
    default:
      pathJson: "model/openapi.json"
      pathYaml: "model/openapi.yaml"
      overrideSpec: "classpath:openapi-header.yml"
      resourcePackages:
        - "org.example.agrest.api"

swaggerui:
  default:
    specPath: "api/model/openapi.json"
```


## App URLs

* Categories endpoint: http://127.0.0.1:8080/api/category
* Swagger model: http://127.0.0.1:8080/api/model/openapi.json
* Swagger UI: http://127.0.0.1:8080/swagger-ui/
