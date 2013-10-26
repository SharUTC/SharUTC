package fr.utc.lo23.sharutc.model;

import java.io.Serializable;

/**
 *
 */
public class AppModelMock extends AppModelImpl implements AppModel, Serializable {

    private static final long serialVersionUID = 55716808016478282L;

    /**
     *
     */
    public AppModelMock() {
        /*
         * mock data in constructor, so we won't have to set them before each test
         */
    }
}
