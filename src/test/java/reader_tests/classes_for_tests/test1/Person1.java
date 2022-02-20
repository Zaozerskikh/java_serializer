package reader_tests.classes_for_tests.test1;

import ru.hse.homework4.Exported;

@Exported
public class Person1 {
    public Person1() { }

    public Person1(int field) {
        this.field = field;
    }

    private int field;

    public int getField() {
        return field;
    }
}

