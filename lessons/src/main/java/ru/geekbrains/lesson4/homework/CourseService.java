package ru.geekbrains.lesson4.homework;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CourseService {
    private final CourseRepository<Course, Integer> repository;

    public CourseService(CourseRepository<Course, Integer> repository) {
        this.repository = repository;
    }

    public void add(Course course) {
        repository.add(course);
    }

    public List<Course> getAll() {
        return repository.getAll();
    }

    public void update(Course course) {
        repository.update(course);
    }

    public void delete(Course course) {
        repository.delete(course);
    }

    public Optional<Course> getById(int id) {
        return repository.getById(id);
    }

}
