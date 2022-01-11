package de.variantsync.functjonal;

import de.variantsync.functjonal.category.Monoid;
import de.variantsync.functjonal.category.Semigroup;

/**
 * Unit represents a type that has exactly one value (Instance()).
 */
public class Unit {
    private static final Unit instance = new Unit();
    public static final Semigroup<Unit> SEMIGROUP = (a, b) -> instance;
    public static final Monoid<Unit> MONOID = Monoid.From(() -> instance, SEMIGROUP);

    private Unit() {}

    public static Unit Instance() {
        return instance;
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
