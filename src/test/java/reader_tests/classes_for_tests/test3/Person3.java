package reader_tests.classes_for_tests.test3;

import ru.hse.homework4.Exported;

@Exported
public class Person3 {
    public Person3() { }

    public Person3(Pet3 pet, String name) {
        this.pet = pet;
        this.name = name;
    }

    private Pet3 pet;

    private String name;

    public Pet3 getPet() {
        return pet;
    }

    public String getName() {
        return name;
    }
}
