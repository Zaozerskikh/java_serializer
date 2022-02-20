package main;

import ru.hse.homework4.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Exported(unknownPropertiesPolicy = UnknownPropertiesPolicy.IGNORE)
public class Person {

    public Person() { }

    public Person(Byte money, double  xyz, Pet pet) {
        this.money = money;
        this.Xyz = xyz;
        this.pet = pet;
        date = LocalDate.now();
        pets = new ArrayList<>();
        pets.add(new Pet());
        pets.add(new Pet());
        pets.add(new Pet());
    }

    @PropertyName(value = "noooo")
    public Byte money;

    public Pet pet;

    @Ignored
    private String name;

    @DateFormat(dateFormat = DateEnum.DD_MM_YYYY)
    private LocalDate date;

    @DateFormat(dateFormat = DateEnum.YYYY_MM_DD)
    private List<Pet> pets;

    private double Xyz;
}