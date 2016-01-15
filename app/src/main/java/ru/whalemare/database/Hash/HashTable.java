package ru.whalemare.database.Hash;

/**
 * Базовый интерфейс HashTable. То, что должна делать каждая операция очевидно.
 * Для хранения элементов будет использоваться обычный массив и структура ArrayList.
 * @param <T1> - тип ключа.
 * @param <T2> - тип значения.
 */
public interface HashTable<T1,T2> {
    boolean  push(T1 x, T2 y);
    boolean delete(T1 x);
    T2  get (T1 x);
}
