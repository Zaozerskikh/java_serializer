package writer_tests.classes_for_tests.test11;

import ru.hse.homework4.Exported;

import java.util.ArrayList;
import java.util.List;

@Exported
public class Person11 {
    public Person11() {
        petList = new ArrayList<>();
        petList.add(new Pet11("first_name"));
        petList.add(new Pet11("second_name"));
        name = "Sergey";
    }

    private final String name;

    private final List<Pet11> petList;

    public List<Pet11> getPetList() {
        return petList;
    }

    public String getName() {
        return name;
    }
}
