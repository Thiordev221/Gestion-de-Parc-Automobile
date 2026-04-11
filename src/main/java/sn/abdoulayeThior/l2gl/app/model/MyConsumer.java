package sn.abdoulayeThior.l2gl.app.model;

@FunctionalInterface
public interface MyConsumer<T> {
    void accept(T o);
}