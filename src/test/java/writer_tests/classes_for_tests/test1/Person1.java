package writer_tests.classes_for_tests.test1;

import ru.hse.homework4.Exported;

@Exported
public class Person1 {
    public Person1() {
        field = 5;
    }

    private final int field;

    public int getField() {
        return field;
    }
}
