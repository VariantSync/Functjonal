# Functjonal - Functional Programming Patterns for Java

This is a work-in-progress java maven library to bring some of our beloved functional programming concepts to Java.
Feel free to contribute. :)

Currently, the major features of Functjonal are:

- Simple Algebraic Structures: Functjonal provides interfaces for **[semigroups]()** and **[monoids]()**.
  These structures operate on a specific domain. For example, a `Semigroup<A>` is a binary composition operator for values of type `A`.
  By convention, any type that is a semigroup or monoid should provide a respective implementation as a `public static final` field with the name of the structure in capslock. For example:
  ```java
  import de.variantsync.functjonal.category.Monoid;
  
  public record PassedHours(int hours, int minutes) {
    public static final Monoid<PassedHours> MONOID = Monoid.From(
      () -> new PassedHours(0, 0),
      (a, b) -> new PassedHours(
          a.hours() + b.hours() + ((a.minutes() + b.minutes()) / 60),
          (a.minutes() + b.minutes()) % 60
      )
    );
  }
  ```
  Then, you can use the monoid as such:
  ```java
  List<PassedHours> hours = ...; // let's say you have a list of passed hours
  Monoid<PassedHours> m = PassedHours.MONOID;
  PassedHours result = hours.stream().reduce(m.neutral(), m::append);
  ```
  As java does not feature type classes and inheritance is insufficient (as for instance getting the neutral element from a monoid would require an instance of the monoid's data type), we decided to adopt this convention as it is also flexible to define multiple monoids over the same datatype (e.g., `(0, +)` and `(1, *)` for `int`).
- [Results](src/main/java/de/variantsync/functjonal/Result.java), also known as `Either` from Haskell. A `Result<S, F>` is a sum type that either has a value of type `S` (indicating success) or a value of type `F` (indicating failure).
- [Product](src/main/java/de/variantsync/functjonal/Product.java) as a simple pair.
- [Unit](src/main/java/de/variantsync/functjonal/Unit.java) as an explicit representation for `void`.
- [Lazy evaluation](src/main/java/de/variantsync/functjonal/Lazy.java).  A `Lazy<A>` lazily encapsulates a value of type `A` that can be accessed with `lazy.run()`. The first, time `run` is invoked, the value will be computed and cached. Subsequent calls of `run`, directly return the cached value. In the background, `Lazy<A>` is wraps a `Supplier<A>`.

Other features include non-empty lists as well as further quality-of-life utilities for lists, maps, and iterators.