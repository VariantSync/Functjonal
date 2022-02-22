package vevos.functjonal.functions;

@FunctionalInterface
public interface FragileFunction<A, B, E extends Exception> {
    B run(A a) throws E;
    
    default <Input, ExceptionBefore extends Exception> FragileFunction<Input, B, Exception> compose(final FragileFunction<Input, A, ExceptionBefore> before) {
        return (Input input) -> this.run(before.run(input)); 
    }
    
    default <Output, ExceptionAfter extends Exception> FragileFunction<A, Output, Exception> andThen(final FragileFunction<B, Output, ExceptionAfter> after) {
        return (A input) -> after.run(this.run(input));
    }
}
