package ru.geekbrains.lesson3.hw3;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/*
Разработайте класс Student с полями String name, int age, transient double GPA (средний балл).
Обеспечьте поддержку сериализации для этого класса.
Создайте объект класса Student и инициализируйте его данными.
Сериализуйте этот объект в файл.
Десериализуйте объект обратно в программу из файла.
Выведите все поля объекта, включая GPA, и обсудите, почему значение GPA не было сохранено/восстановлено.

 * Выполнить задачу 1 используя другие типы сериализаторов (в xml и json документы).

*/
public class Student implements Externalizable {

    private String name;

    private int age;

    private transient double gpa;


    public Student() {
    }

    public Student(String name, int age, double gpa) {
        this.name = name;
        this.age = age;
        this.gpa = gpa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", GPA=" + gpa +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

        out.writeObject(name);
        out.writeInt(age);
        out.writeDouble(gpa);

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        name = (String) in.readObject();
        age = in.readInt();
        gpa = in.readDouble();

    }
}
