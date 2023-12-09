package ru.geekbrains.lesson2.hw2;

public class Cat extends Animal implements CanMakeSound {

    private int counterCatchingRats = 5;
    public Cat(String name, int age) {
        super(name, age);
    }
    @Override
    public void makeSound() {
        System.out.println("Meow!");
    }


}
