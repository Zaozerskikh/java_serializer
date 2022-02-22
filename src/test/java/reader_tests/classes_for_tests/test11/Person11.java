package reader_tests.classes_for_tests.test11;

import ru.hse.homework4.Exported;
import ru.hse.homework4.NullHandling;

import java.util.*;

@Exported(nullHandling = NullHandling.INCLUDE)
public class Person11 {
    public Person11() { }

    public Person11(String name) {
        integerSet.add(1);
        integerSet.add(2);
        longSet.add(0L);
        longSet.add(3L);
        doubleSet.add(0.0);
        doubleSet.add(1.1);
        floatSet.add(4.0f);
        boolSet.add(true);
        characterSet.add('j');
        this.name = name;
    }

    private String name;

    private final Set<Integer> integerSet = new HashSet<>();

    private final Set<Double> doubleSet = new HashSet<>();

    private final Set<Float> floatSet = new LinkedHashSet<>();

    private final Set<Character> characterSet = new LinkedHashSet<>();

    private final Set<Long> longSet = new LinkedHashSet<>();

    private final Set<Boolean> boolSet = new HashSet<>();

    public String getName() {
        return name;
    }

    public Set<Integer> getIntegerSet() {
        return integerSet;
    }

    public Set<Double> getDoubleSet() {
        return doubleSet;
    }

    public Set<Float> getFloatSet() {
        return floatSet;
    }

    public Set<Character> getCharacterSet() {
        return characterSet;
    }

    public Set<Long> getLongSet() {
        return longSet;
    }

    public Set<Boolean> getBoolSet() {
        return boolSet;
    }
}
