package sn.abdoulayeThior.app.Utilitaires;

@FunctionalInterface
public interface MyComparator<T> {
    int compare(T t1, T t2);
}
