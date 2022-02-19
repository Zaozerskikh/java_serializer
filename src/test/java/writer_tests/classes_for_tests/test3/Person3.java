package writer_tests.classes_for_tests.test3;

import ru.hse.homework4.Exported;

@Exported
public class Person3 {
    public Person3() {
        pet = new Pet3();
    }

    private final Pet3 pet;

    public Pet3 getField() {
        return pet;
    }
}
