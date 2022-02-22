package vevos.functjonal.functions;

@FunctionalInterface
public interface FragileSupplier<T, E extends Exception> {
    T get() throws E;
}
