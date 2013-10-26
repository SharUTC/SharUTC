package fr.utc.lo23.sharutc.injection;

import com.google.inject.AbstractModule;
import fr.utc.lo23.sharutc.model.AppModel;
import fr.utc.lo23.sharutc.model.AppModelImpl;

/**
 *
 */
public class ModelTestModule extends AbstractModule {

    /**
     *
     */
    @Override
    protected void configure() {
        bind(AppModel.class).to(AppModelImpl.class);
    }
}
