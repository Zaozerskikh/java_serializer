package writer_tests.classes_for_tests.test2;

import ru.hse.homework4.Exported;
import ru.hse.homework4.PropertyName;

@Exported
public class Person2 {
    public Person2() {
        field = "dark magic";
    }

    @PropertyName(value = "not_a_field_lol")
    private final String field;

    public String getField() {
        return field;
    }
}
