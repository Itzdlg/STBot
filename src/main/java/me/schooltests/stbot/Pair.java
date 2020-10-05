package me.schooltests.stbot;

public class Pair<K, V> {
    private final K first;
    private final V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    public static <K, V> Pair<K, V> empty() {
        return new Pair<>(null, null);
    }

    public static <K> Pair<K, K> single(K obj) {
        return new Pair<>(obj, obj);
    }
}
