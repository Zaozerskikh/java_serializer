package validator_tests.classes_for_tests.test9;

import ru.hse.homework4.Exported;
import ru.hse.homework4.UnknownPropertiesPolicy;

import java.util.List;

@Exported(unknownPropertiesPolicy = UnknownPropertiesPolicy.IGNORE)
public class Person9 {
    List<?> badList;
}
