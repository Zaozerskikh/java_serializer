package main;

import ru.hse.homework4.*;

import java.time.LocalDate;

@Exported(unknownPropertiesPolicy = UnknownPropertiesPolicy.IGNORE)
public class Person {

    public Person() { }

    public Person(Byte money, double  xyz, Pet pet) {
        this.money = money;
        this.Xyz = xyz;
        this.pet = pet;
        date = LocalDate.now();
    }

    @PropertyName(value = "noooo")
    public Byte money;

    public Pet pet;

    @Ignored
    private String name;

    @DateFormat(dateFormat = DateEnum.YYYY_MM_DD)
    private LocalDate date;

    private double Xyz;
}