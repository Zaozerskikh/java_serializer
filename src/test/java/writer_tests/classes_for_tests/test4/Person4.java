package writer_tests.classes_for_tests.test4;

import ru.hse.homework4.Exported;
import ru.hse.homework4.Ignored;

@Exported
public class Person4 {
    public Person4() {
        pet = new Pet4();
        name = "Sergey";
    }

    @Ignored
    private final Pet4 pet;

    private final String name;

    public Pet4 getField() {
        return pet;
    }

    public String getName() {
        return this.name;
    }
}

