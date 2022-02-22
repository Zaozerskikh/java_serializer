package reader_tests.classes_for_tests.test10;

import ru.hse.homework4.Exported;

@Exported
public class Pet10 {
    public Pet10() { }

    public Pet10(String petName) {
        this.petName = petName;
    }

    private String petName;

    public String getName() {
        return petName;
    }
}
