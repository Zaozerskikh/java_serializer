package reader_tests.classes_for_tests.test8;

import ru.hse.homework4.Exported;
import ru.hse.homework4.NullHandling;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Exported(nullHandling = NullHandling.INCLUDE)
public class Person8 {
    public Person8() { }

    public Person8(String name) {
        intList.add(1);
        intList.add(2);
        longList.add(0L);
        longList.add(3L);
        doubleList.add(0.0);
        doubleList.add(1.1);
        floatList.add(4.0f);
        boolList.add(true);
        charList.add('j');
        this.name = name;
    }

    private String name;

    private final List<Integer> intList = new LinkedList<>();

    private final List<Double> doubleList = new ArrayList<>();

    private final List<Float> floatList = new LinkedList<>();

    private final List<Character> charList = new ArrayList<>();

    private final List<Long> longList = new LinkedList<>();

    private final List<Boolean> boolList = new ArrayList<>();

    public List<Integer> getIntList() {
        return intList;
    }

    public List<Double> getDoubleList() {
        return doubleList;
    }

    public List<Float> getFloatList() {
        return floatList;
    }

    public List<Character> getCharList() {
        return charList;
    }

    public List<Long> getLongList() {
        return longList;
    }

    public List<Boolean> getBoolList() {
        return boolList;
    }

    public String getName() {
        return name;
    }
}
