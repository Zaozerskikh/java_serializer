package writer_tests.classes_for_tests.test8;

import ru.hse.homework4.DateEnum;
import ru.hse.homework4.DateFormat;
import ru.hse.homework4.Exported;

import java.time.LocalDate;

@Exported
public class Person8 {
    public Person8() {
        date = LocalDate.parse("2022-02-20");
    }

    @DateFormat(dateFormat = DateEnum.DD_MM_YYYY)
    private final LocalDate date;

    public LocalDate getDate() {
        return date;
    }
}
