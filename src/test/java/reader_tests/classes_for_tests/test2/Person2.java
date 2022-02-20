package reader_tests.classes_for_tests.test2;

import ru.hse.homework4.Exported;

@Exported
public class Person2 {
    public Person2() { }

    public Person2(Pet2 pet, String name) {
        this.pet = pet;
        this.name = name;
    }

    private Pet2 pet;

    private String name;

    public Pet2 getPet() {
        return pet;
    }

    public String getName() {
        return name;
    }
}
