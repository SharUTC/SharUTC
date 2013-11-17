package fr.utc.lo23.sharutc.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ErrorBus is a conveninent way to inform the view of errors relative to user,
 * as an error at login. The aim is to avoid the Service layer to depend on the
 * View layer and respect the MVC schema
 */
public class ErrorBus {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private ErrorMessage mErrorMessage;

    /**
     * Inform the view of an error
     *
     * @param errorMessage the typed message about the error
     */
    public void pushErrorMessage(ErrorMessage errorMessage) {
        ErrorMessage oldErrorMessage = this.mErrorMessage;
        this.mErrorMessage = errorMessage;
        propertyChangeSupport.firePropertyChange(
                Property.APPLICATION_ERROR_MESSAGE.name(), oldErrorMessage,
                errorMessage);
    }

    /**
     * Add the listener in parameter to the list of listeners that may be
     * notified
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes the listener in parameter to the list of listeners that may be
     * notified
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     *
     */
    public enum Property {

        /**
         *
         */
        APPLICATION_ERROR_MESSAGE;
    }
}
