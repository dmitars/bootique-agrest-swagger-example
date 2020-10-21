package org.example.agrest;

import io.agrest.cayenne.AgCayenneBuilder;
import io.agrest.cayenne.AgCayenneModule;
import io.agrest.runtime.AgBuilder;
import io.agrest.runtime.AgRuntime;
import io.bootique.BQCoreModule;
import io.bootique.BaseModule;
import io.bootique.Bootique;
import io.bootique.di.Binder;
import io.bootique.jersey.JerseyModule;
import io.bootique.jetty.JettyModule;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.example.agrest.api.CategoryApi;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

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
        BQCoreModule.extend(binder).addConfig("classpath:config.yml");
        JerseyModule.extend(binder).addPackage(CategoryApi.class);
    }
}
