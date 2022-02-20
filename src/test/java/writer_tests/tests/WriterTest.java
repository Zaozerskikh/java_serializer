package writer_tests.tests;

import implementation.Writer;
import org.junit.jupiter.api.*;
import writer_tests.classes_for_tests.test1.Person1;
import writer_tests.classes_for_tests.test10.Person10;
import writer_tests.classes_for_tests.test11.Person11;
import writer_tests.classes_for_tests.test12.Person12;
import writer_tests.classes_for_tests.test13.Person13;
import writer_tests.classes_for_tests.test2.Person2;
import writer_tests.classes_for_tests.test3.Person3;
import writer_tests.classes_for_tests.test4.Person4;
import writer_tests.classes_for_tests.test5.Person5;
import writer_tests.classes_for_tests.test6.Person6;
import writer_tests.classes_for_tests.test7.Person7;
import writer_tests.classes_for_tests.test8.Person8;
import writer_tests.classes_for_tests.test9.Person9;

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

    @Test
    public void dateFormat_YYYY_MM_DD_byAnnotation() {
        assertEquals(Writer.getWriter().write(new Person7(), new StringBuilder(), null),
                "{date = \"2022-02-20\"; }; ");
    }

    @Test
    public void dateFormat_DD_MM_YYYY_byAnnotation() {
        assertEquals(Writer.getWriter().write(new Person8(), new StringBuilder(), null),
                "{date = \"20-02-2022\"; }; ");
    }

    @Test
    public void testSimpleList() {
        assertEquals(Writer.getWriter().write(new Person9(), new StringBuilder(), null),
                "{list = [ 1; 2; ]; }; ");
    }

    @Test
    public void testSimpleSet() {
        assertEquals(Writer.getWriter().write(new Person10(), new StringBuilder(), null),
                "{set = [ 1.3; 0.7; ]; }; ");
    }

    @Test
    public void testComplexList() {
        assertEquals(Writer.getWriter().write(new Person11(), new StringBuilder(), null),
                "{name = \"Sergey\"; petList = [ {name = \"first_name\"; }; {name = \"second_name\"; }; ]; }; ");
    }

    @Test
    public void testComplexSet() {
        assertEquals(Writer.getWriter().write(new Person12(), new StringBuilder(), null),
                "{name = \"Sergey\"; laptopSet = [ {name = \"Macbook\"; cost = \"150.0\"; }; {name = \"Huawei\"; cost = \"55.0\"; }; ]; }; ");
    }

    @Test
    public void nullHandlingIncludeComplexObjTest() {
        assertEquals(Writer.getWriter().write(new Person13(), new StringBuilder(), null),
                "{name = \"Sergey\"; pet = \"{}\"; }; ");
    }

    @Test
    public void serializeNull() {
        assertEquals(Writer.getWriter().write(null, new StringBuilder(), null),
                "{}");
    }
}
