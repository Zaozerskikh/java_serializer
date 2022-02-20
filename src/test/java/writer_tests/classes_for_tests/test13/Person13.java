package writer_tests.classes_for_tests.test13;

import ru.hse.homework4.Exported;
import ru.hse.homework4.NullHandling;

@Exported(nullHandling = NullHandling.INCLUDE)
public class Person13 {
    public Person13() {
        this.name = "Sergey";
    }

    private final String name;

    private Pet13 pet;

    public String getName() {
        return name;
    }

    public Pet13 getPet() {
        return pet;
    }
}
