package anonymized.functjonal.functions;

@FunctionalInterface
public interface FragileProcedure<E extends Exception> {
    void run() throws E;
}
