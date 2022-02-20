package writer_tests.classes_for_tests.test11;

import ru.hse.homework4.Exported;

@Exported
public class Pet11 {
    public Pet11() { }

    public Pet11(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }
}
