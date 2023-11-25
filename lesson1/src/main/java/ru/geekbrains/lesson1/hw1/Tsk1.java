package ru.geekbrains.lesson1.hw1;

import java.util.Arrays;
import java.util.List;



/*
Напишите программу, которая использует Stream API для обработки списка чисел.
Программа должна вывести на экран среднее значение всех четных чисел в списке.
 */
public class Tsk1 {
    public static void main(String[] args) {
        // Создаем список чисел
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Используем Stream API для обработки списка
        double averageOfEvenNumbers = numbers.stream()
                .filter(n -> n % 2 == 0) // четные
                .mapToDouble(Integer::doubleValue) // в даблу
                .average() // находим среднее
                .orElse(0.0); // дефолтное или пустое значение
        System.out.println("Среднее значение четных чисел в массиве:" + averageOfEvenNumbers);
    }
}
