package writer_tests.classes_for_tests.test10;

import ru.hse.homework4.Exported;
import java.util.LinkedHashSet;
import java.util.Set;

@Exported
public class Person10 {
    public Person10() {
        set = new LinkedHashSet<>();
        set.add(1.3);
        set.add(0.7);
    }

    private final Set<Double> set;

    public Set<Double> getSet() {
        return set;
    }
}
