package fr.utc.lo23.sharutc.ui.navigation;

/**
 * A controller with a {@link NavigationHandler}
 */
public class NavigationController {
    protected NavigationHandler mNavigationHandler;
    
    /**
     * Set the {@link NavigationHandler}
     * 
     * @param navigationHandler the {@link NavigationHandler} to be set.
     */
    public void setNavigationHandler(NavigationHandler navigationHandler) {
        mNavigationHandler = navigationHandler;
    }
}
