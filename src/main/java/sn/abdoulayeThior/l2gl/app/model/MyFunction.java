package sn.abdoulayeThior.l2gl.app.model;

@FunctionalInterface
public interface MyFunction<T, R> {
    R apply(T t);
}