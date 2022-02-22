package de.variantsync.functjonal.category;

class MonoidCollector<M> {
    private final Monoid<M> monoid;
    private M val;

    public MonoidCollector(final Monoid<M> m) {
        this.val = m.neutral();
        this.monoid = m;
    }

    void accumulate(final M other) {
        val = monoid.append(val, other);
    }

    MonoidCollector<M> combine(final MonoidCollector<M> other) {
        val = monoid.append(val, other.val);
        return this;
    }

    M finish() {
        return val;
    }
}
