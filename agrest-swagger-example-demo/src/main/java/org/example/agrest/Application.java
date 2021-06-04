package org.example.agrest;

import io.bootique.BQCoreModule;
import io.bootique.BaseModule;
import io.bootique.Bootique;
import io.bootique.agrest.cayenne42.swagger.AgrestSwaggerModule;
import io.bootique.di.Binder;
import io.bootique.jersey.JerseyModule;
import org.example.agrest.api.CategoryApi;
import org.example.agrest.persistent.Book;

public class Application extends BaseModule {

    public static void main(String[] args) {
        Bootique.app(args)
                .autoLoadModules()
                .module(Application.class)
                .exec()
                .exit();
    }

    @Override
    public void configure(Binder binder) {
        BQCoreModule.extend(binder).addConfig("classpath:default.yml");
        JerseyModule.extend(binder).addPackage(CategoryApi.class);

        // important to specify Agrest model package(s) explicitly. Classes referenced from API resources that are
        // not in these packages will be exposed as OpenAPI schemas generated via a default reflection mechanism,
        // and will not align with Agrest model
        AgrestSwaggerModule.extend(binder).addModelPackage(Book.class);
    }
}
