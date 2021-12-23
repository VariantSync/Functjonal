package de.variantsync.functjonal;

public abstract class Cast {
    private Cast() {}

    @SuppressWarnings("unchecked")
    public static <A, B> A unchecked(final B b) {
        return (A) b;
    }
}
