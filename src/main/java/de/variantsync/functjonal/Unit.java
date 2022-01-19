package de.variantsync.functjonal;

import de.variantsync.functjonal.category.Monoid;

/**
 * Unit represents a type that has exactly one value (Instance()).
 */
public final class Unit {
    private static final Unit INSTANCE = new Unit();
    public static final Monoid<Unit> MONOID = Monoid.from(() -> INSTANCE, (a, b) -> INSTANCE);

    private Unit() {
    }

    public static Unit instance() {
        return INSTANCE;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof Unit;
    }

    @Override
    public int hashCode() {
        return 1; // all instances are the same as there exists just one.
    }

    @Override
    public String toString() {
        return "()";
    }
}
