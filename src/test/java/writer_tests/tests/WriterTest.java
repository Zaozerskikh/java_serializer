package writer_tests.tests;

import implementation.Writer;
import org.junit.jupiter.api.*;
import writer_tests.classes_for_tests.test1.Person1;
import writer_tests.classes_for_tests.test2.Person2;
import writer_tests.classes_for_tests.test3.Person3;
import writer_tests.classes_for_tests.test4.Person4;
import writer_tests.classes_for_tests.test5.Person5;
import writer_tests.classes_for_tests.test6.Person6;

import static org.junit.jupiter.api.Assertions.*;

public class WriterTest {

    @Test
    public void normalWriting() {
        assertEquals(Writer.getWriter().write(new Person1(), new StringBuilder(), null),
                "{field = \"5\"; }; ");
    }

    @Test
    public void changeFieldNameByAnnotation() {
        assertEquals(Writer.getWriter().write(new Person2(), new StringBuilder(), null),
                "{not_a_field_lol = \"dark magic\"; }; ");
    }

    @Test
    public void serializeComplexObj() {
        assertEquals(Writer.getWriter().write(new Person3(), new StringBuilder(), null),
                "{pet = \"{name = \"SomeNameLol\"; }\"; }; ");
    }

    @Test
    public void ignoreFieldByAnnotation() {
        assertEquals(Writer.getWriter().write(new Person4(), new StringBuilder(), null),
                "{name = \"Sergey\"; }; ");
    }

    @Test
    public void nullHandlingIncludeTest() {
        assertEquals(Writer.getWriter().write(new Person5(), new StringBuilder(), null),
                "{name = \"null\"; age = \"19\"; }; ");
    }

    @Test
    public void nullHandlingExcludeTest() {
        assertEquals(Writer.getWriter().write(new Person6(), new StringBuilder(), null),
                "{age = \"19\"; }; ");
    }
}
