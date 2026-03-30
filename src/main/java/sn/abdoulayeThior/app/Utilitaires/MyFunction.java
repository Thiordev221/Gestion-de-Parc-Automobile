package sn.abdoulayeThior.app.Utilitaires;

@FunctionalInterface
public interface MyFunction<T> {
    String apply(T t);
}
