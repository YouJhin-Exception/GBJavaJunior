package ru.geekbrains.lesson2.hw2;

import java.lang.reflect.*;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        //Реализуйте два класса-наследника от "Animal" (например, "Dog" и "Cat") с уникальными полями и методами.
        Cat cat1 = new Cat("Boris", 3);
        Dog dog1 = new Dog("Skif",5);


        //Создайте массив объектов типа "Animal"
        Animal[] animals = new Animal[]{cat1,dog1};

        /* с использованием Reflection API выполните следующие действия:
        Выведите на экран информацию о каждом объекте.
        Вызовите метод "makeSound()" у каждого объекта */

        for (Animal animal : animals) {

            Class<?> animalClazz = animal.getClass();

            // поля объектов
            Field[] fields = animalClazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true); // Разрешаем доступ к закрытому полю
                System.out.printf("%s %s %s: %s\n",Modifier.toString(field.getModifiers()), field.getType(), field.getName(), field.get(animal));


            }

            // конструкторы
            Constructor<?>[] constructors = animalClazz.getConstructors();
            for (Constructor<?> constructor: constructors) {
                System.out.printf("%s: количество параметров %s\n", constructor.getName(), constructor.getParameterCount() );
            }

            //методы
            Method[] methods = animalClazz.getDeclaredMethods();

            for (Method method:methods) {
                System.out.printf("Метод: %s, тип возвращаемого значения: %s, модификатор доступа: %s,\n", method.getName(),method.getReturnType(), Modifier.toString(method.getModifiers()));

            }


            // вызов методов
            Method makeSound = animalClazz.getMethod("makeSound");
            makeSound.invoke(animal);



        }


    }
}
