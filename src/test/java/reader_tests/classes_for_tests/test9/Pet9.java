package reader_tests.classes_for_tests.test9;

import ru.hse.homework4.Exported;

@Exported
public class Pet9 {
    public Pet9() { }

    public Pet9(String petName) {
        this.petName = petName;
    }

    private String petName;

    public String getName() {
        return petName;
    }
}
