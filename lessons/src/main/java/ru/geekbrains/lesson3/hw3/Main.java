package ru.geekbrains.lesson3.hw3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Главный класс, демонстрирующий сохранение и загрузку данных студентов в различных форматах файлов.
 */
public class Main {

    // Константы для имен файлов
    public static final String FILE_JSON = "saveStudentJson.json";
    public static final String FILE_BIN = "saveStudentBin.bin";
    public static final String FILE_XML = "saveStudentXml.xml";

    // Константы для расширений файлов
    public static final String JSON_EXTENSION = ".json";
    public static final String XML_EXTENSION = ".xml";
    public static final String BIN_EXTENSION = ".bin";

    // Объект ObjectMapper для работы с JSON, XmlMapper для работы с XML
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();

    /**
     * Метод main, в котором создаются объекты Student и производится их сохранение в различные файлы.
     */
    public static void main(String[] args) {
        // Создание студентов
        Student studentOne = new Student("Jhin", 333, 7.7);
        Student studentTwo = new Student("Mary", 222, 7.8);
        Student studentThree = new Student("Jane", 111, 7.9);

        // Сохранение студентов в файлы различных форматов
        saveStudentToStorage(FILE_JSON, studentOne);
        saveStudentToStorage(FILE_JSON, studentTwo);
        saveStudentToStorage(FILE_JSON, studentThree);

        saveStudentToStorage(FILE_XML, studentOne);
        saveStudentToStorage(FILE_XML, studentTwo);
        saveStudentToStorage(FILE_XML, studentThree);

        saveStudentToStorage(FILE_BIN, studentOne);
        saveStudentToStorage(FILE_BIN, studentTwo);
        saveStudentToStorage(FILE_BIN, studentThree);
    }

    /**
     * Метод для сохранения объекта Student в файл заданного формата.
     *
     * @param fileName Имя файла, в который будет производиться сохранение.
     * @param student  Объект Student, который необходимо сохранить.
     */
    public static void saveStudentToStorage(String fileName, Student student) {
        try {
            // Загрузка списка студентов из файла
            List<Student> students = loadStudentsFromFile(fileName);
            // Добавление нового студента в список
            students.add(student);

            // Выбор метода сохранения в зависимости от расширения файла
            switch (getFileExtension(fileName)) {
                case JSON_EXTENSION:
                    // Конфигурирование ObjectMapper для красивого форматирования JSON
                    objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
                    // Сохранение в формате JSON
                    objectMapper.writeValue(new File(fileName), students);
                    break;
                case XML_EXTENSION:
                    // Сохранение в формате XML
                    xmlMapper.writeValue(new File(fileName), students);
                    break;
                case BIN_EXTENSION:
                    // Сохранение в формате бинарных данных
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
                        oos.writeObject(students);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Неподдерживаемое расширение файла");
            }
        } catch (Exception e) {
            handleException(e);
        }
    }

    /**
     * Метод для загрузки списка студентов из файла заданного формата.
     *
     * @param fileName Имя файла, из которого будет производиться загрузка.
     * @return Список объектов Student, загруженных из файла.
     * @throws IOException            Исключение, связанное с работой с файлами.
     * @throws ClassNotFoundException Исключение, связанное с загрузкой класса из файла.
     */
    private static List<Student> loadStudentsFromFile(String fileName) throws IOException, ClassNotFoundException {
        List<Student> students = new ArrayList<>();
        File file = new File(fileName);
        // Проверка существования файла
        if (file.exists()) {
            // Выбор метода загрузки в зависимости от расширения файла
            switch (getFileExtension(fileName)) {
                case JSON_EXTENSION:
                    // Загрузка из формата JSON
                    students = objectMapper.readValue(file, new TypeReference<List<Student>>() {});
                    break;
                case XML_EXTENSION:
                    // Загрузка из формата XML
                    students = xmlMapper.readValue(file, new TypeReference<List<Student>>() {});
                    break;
                case BIN_EXTENSION:
                    // Загрузка из формата бинарных данных
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                        students = (List<Student>) ois.readObject();
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Неподдерживаемое расширение файла");
            }
        }
        return students;
    }

    /**
     * Метод для получения расширения файла из его имени.
     *
     * @param fileName Имя файла.
     * @return Расширение файла.
     */
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("Имя файла не содержит расширения: " + fileName);
        }
        return fileName.substring(lastDotIndex);
    }

    /**
     * Метод для обработки исключений. В данном случае, просто выводит трассировку стека в консоль.
     *
     * @param e Исключение, которое необходимо обработать.
     */
    private static void handleException(Exception e) {
        // Вывод трассировки стека исключения в консоль
        e.printStackTrace();
    }
}
