package sn.abdoulayeThior.l2gl.app.model;

@FunctionalInterface
public interface MyPredicate<T> {
    boolean test(T o);
}