package writer_tests.classes_for_tests.test9;

import ru.hse.homework4.Exported;

import java.util.ArrayList;
import java.util.List;

@Exported
public class Person9 {
    public Person9() {
        list = new ArrayList<>();
        list.add(1);
        list.add(2);
    }

    private final List<Integer> list;

    public List<Integer> getList() {
        return list;
    }
}
