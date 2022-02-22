package reader_tests.tests;

import implementation.Mapper;
import implementation.Reader;
import implementation.Writer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import reader_tests.classes_for_tests.test1.Person1;
import reader_tests.classes_for_tests.test10.Person10;
import reader_tests.classes_for_tests.test11.Person11;
import reader_tests.classes_for_tests.test2.Person2;
import reader_tests.classes_for_tests.test3.Person3;
import reader_tests.classes_for_tests.test4.Person4;
import reader_tests.classes_for_tests.test5.Person5;
import reader_tests.classes_for_tests.test6.Person6;
import reader_tests.classes_for_tests.test8.Person8;
import reader_tests.classes_for_tests.test9.Person9;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class ReaderTest {

    @Test
    public void simpleObjReadingTest() {
        String serialized_obj = "{field = \"5\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person1.class, serialized_obj);
        assertEquals(deserialized_obj.getField(), 5);
    }

    @Test
    public void complexObjReadingTest() {
        String serialized_obj = "{name = \"Sergey\"; pet = \"{name = \"PetName\"; }\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person2.class, serialized_obj);
        assertAll(
                () -> assertEquals("PetName", deserialized_obj.getPet().getName()),
                () -> assertEquals("Sergey", deserialized_obj.getName())
        );
    }

    @Test
    public void changedFieldNameByAnnotationReadingTest() {
        String serialized_obj = "{name = \"Sergey\"; pet = \"{not_a_name_lol = \"PetName\"; }\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person3.class, serialized_obj);
        assertAll(
                () -> assertEquals("PetName", deserialized_obj.getPet().getName()),
                () -> assertEquals("Sergey", deserialized_obj.getName())
        );
    }

    @Test
    public void nullFieldReadingTest() {
        String serialized_obj = "{id = \"4\"; surname = \"null\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person4.class, serialized_obj);
        assertAll(
                () -> assertNull(deserialized_obj.getSurname()),
                () -> assertEquals(4, deserialized_obj.getId())
        );
    }

    @Test
    public void manySimpleTypesReadingTest() {
        String serialized_obj = "{longField = \"50000000\"; charField = \"s\"; floatField = \"4\"; stringField = \"some_string\"; boolField = \"false\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person5.class, serialized_obj);
        assertAll(
                () -> assertEquals("some_string", deserialized_obj.getStringField()),
                () -> assertEquals('s', deserialized_obj.getCharField()),
                () -> assertFalse(deserialized_obj.getBoolField()),
                () -> assertEquals(4, deserialized_obj.getFloatField()),
                () -> assertEquals(50000000, deserialized_obj.getLongField())
        );
    }

    @Test
    public void dateFormat_DD_MM_YYYY_ReadingTest() {
        String serialized_obj = "{date = \"20-02-2022\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person6.class, serialized_obj);
        assertEquals(deserialized_obj.getDate(), LocalDate.parse("2022-02-20"));
    }

    @Test
    public void dateFormat_YYYY_MM_DD_ReadingTest() {
        String serialized_obj = "{date = \"2022-02-20\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person6.class, serialized_obj);
        assertEquals(deserialized_obj.getDate(), LocalDate.parse("2022-02-20"));
    }

    @Test
    public void listsOfPrimitiveTypesReading() {
        var mapper = new Mapper(true);
        var serialized_obj = Writer.getWriter().write(new Person8("Sergey"));
        var deserialized_obj = Reader.getReader().readFromString(Person8.class, serialized_obj);
        assertAll(
                () -> assertEquals("Sergey", deserialized_obj.getName()),
                () -> assertEquals(LinkedList.class, deserialized_obj.getIntList().getClass()),
                () -> assertEquals(ArrayList.class, deserialized_obj.getDoubleList().getClass()),
                () -> assertEquals(LinkedList.class, deserialized_obj.getLongList().getClass()),
                () -> assertEquals(ArrayList.class, deserialized_obj.getCharList().getClass()),
                () -> assertEquals(ArrayList.class, deserialized_obj.getBoolList().getClass()),
                () -> assertEquals(LinkedList.class, deserialized_obj.getFloatList().getClass()),
                () -> assertEquals(2, deserialized_obj.getDoubleList().size()),
                () -> assertEquals(1, deserialized_obj.getBoolList().size()),
                () -> assertEquals(2, deserialized_obj.getLongList().size()),
                () -> assertEquals(1, deserialized_obj.getFloatList().size()),
                () -> assertEquals(1, deserialized_obj.getCharList().size()),
                () -> assertEquals('j', deserialized_obj.getCharList().get(0)),
                () -> assertEquals(1, deserialized_obj.getIntList().get(0)),
                () -> assertEquals(2, deserialized_obj.getIntList().get(1)),
                () -> assertEquals(0.0, deserialized_obj.getDoubleList().get(0)),
                () -> assertEquals(1.1, deserialized_obj.getDoubleList().get(1)),
                () -> assertEquals(4.0f, deserialized_obj.getFloatList().get(0)),
                () -> assertEquals(true, deserialized_obj.getBoolList().get(0))
        );
    }

    @Test
    public void listOfComplexObjectsReading() {
        String serialized_obj = "{name = \"Sergo\"; list = \"[ {petName = \"dog\"; }; {petName = \"cat\"; }; ]\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person9.class, serialized_obj);
        assertAll(
                () -> assertEquals("Sergo", deserialized_obj.getName()),
                () -> assertEquals(2, deserialized_obj.getList().size()),
                () -> assertEquals(ArrayList.class, deserialized_obj.getList().getClass()),
                () -> assertEquals("dog", deserialized_obj.getList().get(0).getName()),
                () -> assertEquals("cat", deserialized_obj.getList().get(1).getName())
        );
    }

    @Test
    public void setOfComplexObjectsReading() {
        String serialized_obj = "{name = \"Sergo\"; set = \"[ {petName = \"dog\"; }; {petName = \"cat\"; }; ]\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person10.class, serialized_obj);
        assertAll(
                () -> assertEquals("Sergo", deserialized_obj.getName()),
                () -> assertEquals(2, deserialized_obj.getSet().size()),
                () -> assertEquals(LinkedHashSet.class, deserialized_obj.getSet().getClass()),
                () -> assertTrue(deserialized_obj.getSet().stream().anyMatch(x -> x.getName().equals("cat"))),
                () -> assertTrue(deserialized_obj.getSet().stream().anyMatch(x -> x.getName().equals("dog")))
        );
    }

    @Test
    public void setsOfPrimitiveTypesReading() {
        var mapper = new Mapper(true);
        var serialized_obj = Writer.getWriter().write(new Person11("Sergey"));
        var deserialized_obj = Reader.getReader().readFromString(Person11.class, serialized_obj);
        assertAll(
                () -> assertEquals("Sergey", deserialized_obj.getName()),
                () -> assertEquals(HashSet.class, deserialized_obj.getBoolSet().getClass()),
                () -> assertEquals(LinkedHashSet.class, deserialized_obj.getCharacterSet().getClass()),
                () -> assertEquals(HashSet.class, deserialized_obj.getIntegerSet().getClass()),
                () -> assertEquals(LinkedHashSet.class, deserialized_obj.getLongSet().getClass()),
                () -> assertEquals(HashSet.class, deserialized_obj.getDoubleSet().getClass()),
                () -> assertEquals(LinkedHashSet.class, deserialized_obj.getFloatSet().getClass()),
                () -> assertEquals(2, deserialized_obj.getDoubleSet().size()),
                () -> assertEquals(1, deserialized_obj.getBoolSet().size()),
                () -> assertEquals(2, deserialized_obj.getLongSet().size()),
                () -> assertEquals(1, deserialized_obj.getFloatSet().size()),
                () -> assertEquals(1, deserialized_obj.getCharacterSet().size()),
                () -> assertTrue(deserialized_obj.getCharacterSet().contains('j')),
                () -> assertTrue(deserialized_obj.getIntegerSet().contains(1)),
                () -> assertTrue(deserialized_obj.getIntegerSet().contains(2)),
                () -> assertTrue(deserialized_obj.getDoubleSet().contains(1.1)),
                () -> assertTrue(deserialized_obj.getDoubleSet().contains(0.0)),
                () -> assertTrue(deserialized_obj.getFloatSet().contains(4.0f)),
                () -> assertTrue(deserialized_obj.getBoolSet().contains(true))
        );
    }
}
