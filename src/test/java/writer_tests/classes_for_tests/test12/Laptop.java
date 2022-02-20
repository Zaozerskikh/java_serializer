package writer_tests.classes_for_tests.test12;

import ru.hse.homework4.Exported;

@Exported
public class Laptop {
    public Laptop() { }

    public Laptop(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    private String name;

    private double cost;

    public String getName() {
        return name;
    }

    public double getCost() {
        return cost;
    }
}
