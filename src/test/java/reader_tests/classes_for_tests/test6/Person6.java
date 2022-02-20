package reader_tests.classes_for_tests.test6;

import ru.hse.homework4.DateEnum;
import ru.hse.homework4.DateFormat;
import ru.hse.homework4.Exported;

import java.time.LocalDate;

@Exported
public class Person6 {
    public Person6() { }

    public Person6(LocalDate date) {
        this.date = date;
    }

    @DateFormat(dateFormat = DateEnum.DD_MM_YYYY)
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }
}
