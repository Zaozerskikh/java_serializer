package reader_tests.tests;

import implementation.Reader;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import reader_tests.classes_for_tests.test1.Person1;
import reader_tests.classes_for_tests.test2.Person2;
import reader_tests.classes_for_tests.test3.Person3;
import reader_tests.classes_for_tests.test4.Person4;
import reader_tests.classes_for_tests.test5.Person5;
import reader_tests.classes_for_tests.test6.Person6;
import java.time.LocalDate;

public class ReaderTest {

    @Test
    public void simpleReaderTest() {
        String serialized_obj = "{field = \"5\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person1.class, serialized_obj);
        assertEquals(deserialized_obj.getField(), 5);
    }

    @Test
    public void complexReaderTest() {
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
    public void nullFieldParsingTest() {
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
    public void dateFormat_DD_MM_YYYY_ParsingTest() {
        String serialized_obj = "{date = \"20-02-2022\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person6.class, serialized_obj);
        assertEquals(deserialized_obj.getDate(), LocalDate.parse("2022-02-20"));
    }

    @Test
    public void dateFormat_YYYY_MM_DD_ParsingTest() {
        String serialized_obj = "{date = \"2022-02-20\"; }";
        var deserialized_obj = Reader.getReader().readFromString(Person6.class, serialized_obj);
        assertEquals(deserialized_obj.getDate(), LocalDate.parse("2022-02-20"));
    }
}
