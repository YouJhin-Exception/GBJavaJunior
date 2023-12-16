package ru.geekbrains.lesson4.homework;



import java.util.List;
import java.util.Optional;

public class Program {

    /**
     * Задание
     * =======
     * Создайте базу данных (например, SchoolDB).
     * В этой базе данных создайте таблицу Courses с полями id (ключ), title, и duration.
     * Настройте Hibernate для работы с вашей базой данных.
     * Создайте Java-класс Course, соответствующий таблице Courses, с необходимыми аннотациями Hibernate.
     * Используя Hibernate, напишите код для вставки, чтения, обновления и удаления данных в таблице Courses.
     * Убедитесь, что каждая операция выполняется в отдельной транзакции.
     */

    public static void main(String[] args) {

        // Создаем репозиторий и сервис для работы с курсами
        CourseRepository repository = new CourseRepositoryImpl();
        CourseService service = new CourseService(repository);

        // Создаем тестовые курсы
        Course javaCourse = new Course("Java", 10);
        Course hibernateCourse = new Course("Hibernate", 20);
        Course springCourse = new Course("Spring", 30);

        // Добавляем курсы в БД
        service.add(javaCourse);
        service.add(hibernateCourse);
        service.add(springCourse);

        // Получаем список всех курсов
        List<Course> courses = service.getAll();
        System.out.println("All courses: " + courses);

        // Получаем курс по идентификатору
        Optional<Course> course = service.getById(hibernateCourse.getId());
        System.out.println("Course by Id: " + course);


        // Обновляем данные одного из курсов
        Course courseToUpdate = courses.get(0);
        courseToUpdate.setTitle("Updated Course");
        service.update(courseToUpdate);


        // Снова получаем список курсов
        courses = service.getAll();
        System.out.println("All courses after update: " + courses);


        // Удаляем один из курсов
        Course courseToDelete = courses.get(0);
        service.delete(courseToDelete);


        // Выводим оставшиеся курсы
        courses = service.getAll();
        System.out.println("All courses after delete: " + courses);

  }

}
