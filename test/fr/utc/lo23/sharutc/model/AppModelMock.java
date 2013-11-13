package fr.utc.lo23.sharutc.model;

import com.google.inject.Singleton;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@Singleton
public class AppModelMock extends AppModelImpl implements AppModel, Serializable {

    private static final long serialVersionUID = 55716808016478282L;
    private static final Logger log = LoggerFactory
            .getLogger(AppModelMock.class);
}