package de.variantsync.functjonal;

import de.variantsync.functjonal.category.Monoid;
import de.variantsync.functjonal.category.Semigroup;
import de.variantsync.functjonal.functions.FragileSupplier;
import de.variantsync.functjonal.functions.FragileProcedure;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Type to capture results of computations that might fail.
 * A result reflects either the return value of a successful computation or a failure state.
 * @param <SuccessType> Type for values in case of success.
 * @param <FailureType> Type for values in case of failure.
 */
public class Result<SuccessType, FailureType> {
    /**
     * Combines two results.
     * Returns failure if at least one of the given results is a failure.
     */
    public static <S, F> Semigroup<Result<S, F>> SEMIGROUP(final Semigroup<S> sg, final Semigroup<F> fg) {
        return (a, b) -> {
            final Result<S, F> resultWithPotentialFailure = a.isFailure() ? a : b;
            final Result<S, F> other = a.isFailure() ? b : a;
            return resultWithPotentialFailure.bimap(
                    s -> other.isSuccess() ? sg.append(s, other.getSuccess()) : s,
                    f -> other.isFailure() ? fg.append(f, other.getFailure()) : f
            );
        };
    }

    /**
     * Combines two results over monoidal values.
     * Returns failure if at least one of the given results is a failure.
     * The neutral value is success with the neutral value of sm.
     */
    public static <S, F> Monoid<Result<S, F>> MONOID(final Monoid<S> sm, final Semigroup<F> fm) {
        return Monoid.From(
                () -> Result.Success(sm.neutral()),
                SEMIGROUP(sm, fm)
        );
    }

    public static final boolean HARD_CRASH_ON_TRY = false;

    private final SuccessType result;
    private final FailureType failure;

    /**
     * Creates a new Result where exactly one of the arguments is excepted to be null.
     * @param result Success value or null
     * @param failure Failure value or null
     */
    protected Result(final SuccessType result, final FailureType failure) {
        this.result = result;
        this.failure = failure;
    }

    /// Constructors

    /**
     * Creates a successful result with the given value.
     * @param s Return value of the result.
     * @return Success value.
     */
    public static <S, F> Result<S, F> Success(final S s) {
        return new Result<>(s, null);
    }


    /**
     * Creates a failed result with the given error value.
     * @param f Value indicating failure.
     * @return Failure result.
     */
    public static <S, F> Result<S, F> Failure(final F f) {
        return new Result<>(null, f);
    }

    /**
     * Runs the given computation that indicates success by returning a boolean.
     * Running f will be interpreted as a success iff f returns true.
     * Running f will be interpreted as a failure iff f returns false.
     * In case of failure, a failure value will be produced with the given failure supplier.
     * @param f Computation to run that indicates success with a boolean return value.
     * @param failure Factory for failure message in case f returned false.
     * @return Success iff f returned true, Failure otherwise.
     */
    public static <F> Result<Unit, F> FromFlag(final Supplier<Boolean> f, final Supplier<F> failure) {
        if (f.get()) {
            return Success(Unit.Instance());
        } else {
            return Failure(failure.get());
        }
    }

    /**
     * Runs the given computation that indicates success by returning a boolean and that may throw an exception.
     * Running f will be interpreted as a success iff f returns true and throws no exception.
     * Running f will be interpreted as a failure iff f returns false or throws an exception.
     * If f did not throw an exception but returned false, a failure value will be produced with the given failure supplier.
     * @param f Computation to run that indicates success with a boolean return value or an exception.
     * @param failure Factory for failure message in case f returned false.
     * @return Success iff f returned true and did not throw an exception, Failure otherwise.
     */
    public static <E extends Exception> Result<Unit, E> FromFlag(final FragileSupplier<Boolean, E> f, final Supplier<E> failure) {
        return Try(f).bibind(
                Functjonal.when(
                        () -> Success(Unit.Instance()),
                        () -> Failure(failure.get())
                ),
                Result::Failure
        );
    }

    /**
     * Runs the given computation that may throw an exception.
     * @param s Computation to run.
     * @param <E> The type of exception that may be thrown by s.
     * @return A result containing the result of the given computation or the exception in case it was thrown.
     */
    @SuppressWarnings("unchecked")
    public static <S, E extends Exception> Result<S, E> Try(final FragileSupplier<S, E> s) {
        try {
            final S result = s.get();
            return Result.Success(result);
        } catch (final Exception e) { // We cannot catch E directly.
            if (HARD_CRASH_ON_TRY) {
                throw new RuntimeException(e);
            } else {
                // TODO: This cast might be impossible!
                return Result.Failure((E) e);
            }
        }
    }


    /**
     * Runs the given computation that may throw an exception.
     * @param s Computation to run.
     * @param <E> The type of exception that may be thrown by s.
     * @return A result containing the result of the given computation or the exception in case it was thrown.
     */
    public static <E extends Exception> Result<Unit, E> Try(final FragileProcedure<E> s) {
        return Try(Functjonal.LiftFragile(s));
    }

    /// Operations

    /**
     * Map over success type.
     */
    public <S2> Result<S2, FailureType> map(final Function<SuccessType, S2> successCase) {
        return bimap(successCase, Function.identity());
    }

    /**
     * Map over failure type.
     */
    public <F2> Result<SuccessType, F2> mapFail(final Function<FailureType, F2> failureCase) {
        return bimap(Function.identity(), failureCase);
    }

    /**
     * Result is a bifunctor.
     */
    public <S2, F2> Result<S2, F2> bimap(final Function<SuccessType, S2> successCase, final Function<FailureType, F2> failureCase) {
        if (isSuccess()) {
            return Success(successCase.apply(result));
        } else {
            return Failure(failureCase.apply(failure));
        }
    }

    public <T> T match(final Function<SuccessType, T> successCase, final Function<FailureType, T> failureCase) {
        if (isSuccess()) {
            return successCase.apply(result);
        } else {
            return failureCase.apply(failure);
        }
    }

    public <S2> Result<S2, FailureType> bind(final Function<SuccessType, Result<S2, FailureType>> successCase) {
        if (isSuccess()) {
            return successCase.apply(result);
        } else {
            return Failure(getFailure());
        }
    }

    public <F2> Result<SuccessType, F2> bindFail(final Function<FailureType, Result<SuccessType, F2>> failureCase) {
        if (isFailure()) {
            return failureCase.apply(failure);
        } else {
            return Success(getSuccess());
        }
    }

    public <S2, F2> Result<S2, F2> bibind(final Function<SuccessType, Result<S2, F2>> successCase, final Function<FailureType, Result<S2, F2>> failureCase) {
        if (isSuccess()) {
            return successCase.apply(result);
        } else {
            return failureCase.apply(failure);
        }
    }

    /**
     * Flattens a nested Result.
     * @param r A nested Result that should be flattened to a single Result.
     * @return Flattened result.
     */
    public static <A, B> Result<A, B> join(final Result<Result<A, B>, B> r) {
        if (r.isSuccess()) {
            return r.getSuccess();
        } else {
            return Failure(r.getFailure());
        }
    }

    public boolean isSuccess() {
        return result != null;
    }

    public boolean isFailure() {
        return failure != null;
    }

    public SuccessType getSuccess() {
        return expect("Tried to retrieve the success value of a Failure result!");
    }

    public FailureType getFailure() {
        return failure;
    }

    public void assertSuccess() {
        if (isFailure()) {
//            Logger.error(getFailure().toString());
        }
        assert isSuccess();
    }

    public SuccessType expect(final String message) {
        if (isFailure()) {
//            Logger.error(message);
            throw new RuntimeException(message);
        }
        return result;
    }

    public static <S> S expect(final Result<S, ? extends Throwable> result) {
        return result.match(Function.identity(), e -> {throw new RuntimeException(e);});
    }

    public void ifSuccess(final Consumer<SuccessType> f) {
        if (isSuccess()) {
            f.accept(getSuccess());
        }
    }

    public void ifFailure(final Consumer<FailureType> f) {
        if (isFailure()) {
            f.accept(getFailure());
        }
    }

    @Override
    public String toString() {
        if (isSuccess()) {
            return "(Success " + this.result + ")";
        } else {
            return "(Failure " + this.failure + ")";
        }
    }
}
