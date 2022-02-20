package reader_tests.classes_for_tests.test7;

import ru.hse.homework4.DateEnum;
import ru.hse.homework4.DateFormat;
import ru.hse.homework4.Exported;

import java.time.LocalDate;

@Exported
public class Person7 {
    public Person7() { }

    public Person7(LocalDate date) {
        this.date = date;
    }

    @DateFormat(dateFormat = DateEnum.YYYY_MM_DD)
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }
}
