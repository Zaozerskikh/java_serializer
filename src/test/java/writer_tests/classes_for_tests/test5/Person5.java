package writer_tests.classes_for_tests.test5;

import ru.hse.homework4.Exported;
import ru.hse.homework4.NullHandling;

@Exported(nullHandling = NullHandling.INCLUDE)
public class Person5 {
    public Person5() {
        age = 19;
    }

    private final String name = null;

    private final int age;

    public int getAge() {
        return age;
    }
}
