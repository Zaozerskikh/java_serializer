package reader_tests.classes_for_tests.test10;

import ru.hse.homework4.Exported;
import java.util.LinkedHashSet;

@Exported
public class Person10 {
    public Person10() {
        set = new LinkedHashSet<>();
    }

    public Person10(String name) {
        set = new LinkedHashSet<>();
        set.add(new Pet10("koshka"));
        set.add(new Pet10("sobaka"));
        this.name = name;
    }

    private final LinkedHashSet<Pet10> set;

    String name;

    public LinkedHashSet<Pet10> getSet() {
        return set;
    }

    public String getName() {
        return name;
    }
}
