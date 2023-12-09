package ru.geekbrains.lesson2.hw2;

public class Dog extends Animal implements CanMakeSound{


    private int hiddenBones = 7;

    public Dog(String name, int age) {
        super(name, age);
    }

    @Override
    public void makeSound() {
        System.out.println("Woof!");
    }
}
