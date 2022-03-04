package mapper_tests.classes_for_tests;

public class BadPerson {
    public BadPerson() { }

    public BadPerson(String name, int age, Pet pet) {
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
