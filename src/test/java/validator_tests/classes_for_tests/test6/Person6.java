package validator_tests.classes_for_tests.test6;

import ru.hse.homework4.Exported;
import ru.hse.homework4.UnknownPropertiesPolicy;

import java.util.LinkedHashMap;

@Exported(unknownPropertiesPolicy = UnknownPropertiesPolicy.IGNORE)
public class Person6 {
    LinkedHashMap<?, ?> whatIsItLol;
}
