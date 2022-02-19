package writer_tests.classes_for_tests.test6;

import ru.hse.homework4.Exported;
import ru.hse.homework4.NullHandling;

@Exported(nullHandling = NullHandling.EXCLUDE)
public class Person6 {
    public Person6() {
        age = 19;
    }

    private final String name = null;

    private final int age;

    public int getAge() {
        return age;
    }
}
