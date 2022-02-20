package writer_tests.classes_for_tests.test12;

import ru.hse.homework4.Exported;

import java.util.LinkedHashSet;
import java.util.Set;

@Exported
public class Person12 {
    public Person12() {
        name = "Sergey";
        laptopSet = new LinkedHashSet<>();
        laptopSet.add(new Laptop("Macbook", 150));
        laptopSet.add(new Laptop("Huawei", 55));
    }

    private final String name;

    private final Set<Laptop> laptopSet;

    public Set<Laptop> getLaptopSet() {
        return laptopSet;
    }

    public String getName() {
        return name;
    }
}
