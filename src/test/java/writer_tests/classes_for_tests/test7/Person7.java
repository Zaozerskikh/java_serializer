package writer_tests.classes_for_tests.test7;

import ru.hse.homework4.Exported;

import java.time.LocalDate;

@Exported
public class Person7 {
    public Person7() {
        date = LocalDate.parse("2022-02-20");
    }

    private final LocalDate date;

    public LocalDate getDate() {
        return date;
    }
}
