package ru.geekbrains.lesson4.homework;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CourseRepository<T, TId> {

    void add(T item);

    List<T> getAll();

    void update(T item);

    void delete(T item);

    Optional<T> getById(TId id);

}
