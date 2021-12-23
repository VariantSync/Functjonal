package de.variantsync.functjonal.error;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
        super();
    }

    public NotImplementedException(final String message) {
        super(message);
    }
}
