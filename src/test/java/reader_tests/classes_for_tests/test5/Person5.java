package reader_tests.classes_for_tests.test5;

import ru.hse.homework4.Exported;

@Exported
public class Person5 {
    public Person5() { }

    public Person5(String stringField, Character charField, Boolean boolField, long longField, Float floatField) {
        this.stringField = stringField;
        this.charField = charField;
        this.boolField = boolField;
        this.longField = longField;
        this.floatField = floatField;
    }

    private String stringField;

    private Character charField;

    private Boolean boolField;

    private long longField;

    private Float floatField;

    public String getStringField() {
        return stringField;
    }

    public Character getCharField() {
        return charField;
    }

    public Boolean getBoolField() {
        return boolField;
    }

    public long getLongField() {
        return longField;
    }

    public Float getFloatField() {
        return floatField;
    }
}
