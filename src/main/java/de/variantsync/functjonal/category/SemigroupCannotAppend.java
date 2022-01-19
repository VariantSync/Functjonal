package de.variantsync.functjonal.category;

public class SemigroupCannotAppend extends RuntimeException {
    public SemigroupCannotAppend(final String message) {
        super(message);
    }
}
