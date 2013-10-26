package fr.utc.lo23.sharutc.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 */
public class ErrorBus {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private ErrorMessage errorMessage;

    /**
     *
     * @param errorMessage
     */
    public void pushErrorMessage(ErrorMessage errorMessage) {
        ErrorMessage oldErrorMessage = this.errorMessage;
        this.errorMessage = errorMessage;
        propertyChangeSupport.firePropertyChange(
                Property.APPLICATION_ERROR_MESSAGE.name(), oldErrorMessage,
                errorMessage);
    }

    /**
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     *
     * @param listener
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
