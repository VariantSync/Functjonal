package org.variantsync.functjonal.functions;

@FunctionalInterface
public interface FragileProcedure<E extends Exception> {
    void run() throws E;
}
