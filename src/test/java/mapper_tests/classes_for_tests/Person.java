package mapper_tests.classes_for_tests;

import ru.hse.homework4.Exported;

@Exported
public class Person {
    public Person() { }

    public Person(String name, int age, Pet pet) {
        this.pet = pet;
        this.name = name;
        this.age = age;
    }

    private Pet pet;

    private String name;

    private int age;

    public Pet getPet() {
        return pet;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return age;
    }
}
