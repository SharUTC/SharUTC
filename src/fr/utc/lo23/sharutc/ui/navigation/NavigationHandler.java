package fr.utc.lo23.sharutc.ui.navigation;

/**
 * A simple class to handle the main navigation.
 */
public interface NavigationHandler {

    /**
     * Load the login page.
     */
    public void goToLoginPage();

    /**
     * Load the registration page.
     */
    public void goToRegistrationPage();

    /**
     * Load the main page.
     */
    public void goToMainPage();
}
