package reader_tests.classes_for_tests.test2;

import ru.hse.homework4.Exported;

@Exported
public class Pet2 {
    public Pet2() { }

    public Pet2(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
