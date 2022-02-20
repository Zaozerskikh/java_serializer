package reader_tests.classes_for_tests.test4;

import ru.hse.homework4.Exported;

@Exported
public class Person4 {
    public Person4() {
        this.surname = "not_null_hehehe";
    }

    public Person4(byte id) {
        this.id = id;
    }

    private byte id;

    private String surname;

    public byte getId() {
        return id;
    }

    public String getSurname() {
        return surname;
    }
}
