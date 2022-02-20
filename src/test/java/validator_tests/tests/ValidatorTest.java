package validator_tests.tests;

import implementation.Validator;
import validator_tests.classes_for_tests.test1.Person;
import validator_tests.classes_for_tests.test10.Person10;
import validator_tests.classes_for_tests.test11.Person11;
import validator_tests.classes_for_tests.test12.Person12;
import validator_tests.classes_for_tests.test2.Person2;
import validator_tests.classes_for_tests.test3.Person3;
import validator_tests.classes_for_tests.test4.Person4;
import validator_tests.classes_for_tests.test5.Person5;
import validator_tests.classes_for_tests.test6.Person6;
import validator_tests.classes_for_tests.test7.Person7;
import validator_tests.classes_for_tests.test8.Person8;
import validator_tests.classes_for_tests.test9.Person9;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    @Test
    public void normalValidation() {
        Validator.validateClass(Person.class);
    }

    @Test
    public void exportedAnnotationMissed() {
        assertEquals(assertThrows(UnsupportedOperationException.class, () -> Validator.validateClass(Person2.class)).getMessage(),
                "Class " + Person2.class.getSimpleName() + " not annotated \"@Exported\"");
    }

    @Test
    public void parameterlessConstructorMissed() {
        assertEquals(assertThrows(UnsupportedOperationException.class, () -> Validator.validateClass(Person3.class)).getMessage(),
                "Parameterless constructor in class \"" + Person3.class.getSimpleName() + "\" not found");
    }

    @Test
    public void notObjectSuperclassDeclared() {
        assertEquals(assertThrows(UnsupportedOperationException.class, () -> Validator.validateClass(Person4.class)).getMessage(),
                "Non Object superclass detected in class: \"" + Person4.class.getSimpleName() + "\"");
    }

    @Test
    public void unknownFieldFailed() {
        assertEquals(assertThrows(UnsupportedOperationException.class, () -> Validator.validateClass(Person5.class)).getMessage(),
                "Unknown field \"whatIsItLol\" in class: \"Person5\"");
    }

    @Test
    public void unknownFieldIgnored() {
        Validator.validateClass(Person6.class);
    }

    @Test
    public void SimpleCyclicReferences() {
        assertEquals(assertThrows(UnsupportedOperationException.class, () -> Validator.validateClass(Person7.class)).getMessage(),
                "Cyclic references found");
    }

    @Test
    public void listsAndSetsNormal() {
        Validator.validateClass(Person8.class);
    }

    @Test
    public void unknownGenericIgnore() {
        Validator.validateClass(Person9.class);
    }

    @Test
    public void unknownGenericFail() {
        assertEquals(assertThrows(UnsupportedOperationException.class, () -> Validator.validateClass(Person10.class)).getMessage(),
                "Unknown generic type in field \"badRawList\" in class: \"Person10\"");
    }

    @Test
    public void listCyclicReferences() {
        assertEquals(assertThrows(UnsupportedOperationException.class, () -> Validator.validateClass(Person11.class)).getMessage(),
                "Cyclic references found");
    }

    @Test
    public void listCyclicReferencesItself() {
        assertEquals(assertThrows(UnsupportedOperationException.class, () -> Validator.validateClass(Person12.class)).getMessage(),
                "Cyclic references found");
    }

    @Test
    public void nullObjectValidation() {
        assertEquals(assertThrows(UnsupportedOperationException.class, () -> Validator.validateObject(null)).getMessage(),
                "Unable to serialize null object");
    }
}
