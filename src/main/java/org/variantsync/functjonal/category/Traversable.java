package org.variantsync.functjonal.category;

import org.variantsync.functjonal.Functjonal;
import org.variantsync.functjonal.Result;

import java.util.Optional;

public class Traversable {
    public static <S, F> Result<Optional<S>, F> sequence(final Optional<Result<S, F>> o) {
        return Functjonal.match(o,
                just -> just.map(Optional::of),
                () -> Result.Success(Optional.empty()));
    }

//    public static <T> Lazy<Optional<T>> sequence(final Optional<Lazy<T>> o) {
//        return Functjonal.match(o,
//                just -> just.map(Optional::of),
//                Optional::empty);
//    }
}
