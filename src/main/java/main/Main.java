package main;

import implementation.Mapper;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Mapper mapper = new Mapper(true);
//        String str = mapper.writeToString(new Person((byte)5, 4.0, new Pet()));
//        System.out.println(str);
//        var x1 = mapper.readFromString(Person.class, str);
//        var x2 = mapper.readFromString(Person.class, str);
//        System.out.println(x1.pet == x2.pet);
        System.out.println(LocalDate.parse("2020-02-20"));
    }
}
