package de.variantsync.functjonal.category;

import de.variantsync.functjonal.Functjonal;
import de.variantsync.functjonal.Result;

import java.util.Optional;

public class Traversable {
    public static <S, F> Result<Optional<S>, F> sequence(final Optional<Result<S, F>> o) {
        return Functjonal.match(o,
                just -> just.map(Optional::of),
                () -> Result.Success(Optional.empty()));
    }
}
