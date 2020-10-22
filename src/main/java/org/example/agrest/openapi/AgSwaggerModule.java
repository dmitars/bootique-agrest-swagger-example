package org.example.agrest.openapi;

import org.apache.cayenne.di.Binder;
import org.apache.cayenne.di.Module;
import org.example.agrest.persistent.Category;

public class AgSwaggerModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bindList(String.class, AgEntityModelConverter.BINDING_ENTITY_PACKAGES);
        binder.bind(AgEntityModelConverter.class).to(AgEntityModelConverter.class);

        // TODO: this must be dynamic and managed by extender
        binder.bindList(String.class, AgEntityModelConverter.BINDING_ENTITY_PACKAGES)
                .add(Category.class.getPackage().getName());
    }
}
