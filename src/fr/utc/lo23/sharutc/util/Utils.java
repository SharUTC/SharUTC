package fr.utc.lo23.sharutc.util;

import org.slf4j.Logger;

public class Utils {

    public static void throwMissingParameter(Logger log, Throwable throwable) {
        log.error("Missing parameter(s)");
        throw new IllegalArgumentException(throwable != null ? throwable : new Throwable());
    }
}
