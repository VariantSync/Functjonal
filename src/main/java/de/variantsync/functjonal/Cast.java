package de.variantsync.functjonal;

public abstract class Cast {
    private Cast() {}

    @SuppressWarnings("unchecked")
    public static <From, To> To unchecked(final From b) {
        return (To) b;
    }
}
