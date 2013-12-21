package fr.utc.lo23.sharutc.ui.util;

public class FormUtils {

    /**
     * Returns true is the {@code ageField} contains a integer between 0 and
     * 120, false otherwise.
     *
     * @return
     */
    public static boolean isAgeValid(final String ageStr) {
        boolean isAgeValid = false;
        try {
            final Integer age = Integer.parseInt(ageStr);
            if (age > 0 && age < 120) {
                isAgeValid = true;
            }
        } catch (NumberFormatException e) {
        }

        return isAgeValid;
    }
}
