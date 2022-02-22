package reader_tests.classes_for_tests.test9;

import ru.hse.homework4.Exported;
import java.util.ArrayList;
import java.util.List;

@Exported
public class Person9 {
    public Person9() {
        list = new ArrayList<>();
    }

    public Person9(String name) {
        list = new ArrayList<>();
        list.add(new Pet9("koshka"));
        list.add(new Pet9("sobaka"));
        this.name = name;
    }

    private final List<Pet9> list;

    String name;

    public List<Pet9> getList() {
        return list;
    }

    public String getName() {
        return name;
    }
}
