package reader_tests.classes_for_tests.test3;

import ru.hse.homework4.Exported;
import ru.hse.homework4.PropertyName;

@Exported
public class Pet3 {
    public Pet3() { }

    public Pet3(String name) {
        this.name = name;
    }

    @PropertyName("not_a_name_lol")
    private String name;

    public String getName() {
        return name;
    }
}
