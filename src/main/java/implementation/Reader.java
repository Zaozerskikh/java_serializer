package implementation;

import ru.hse.homework4.Ignored;
import ru.hse.homework4.PropertyName;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public class Reader {
    private Reader() { }

    private static Reader INSTANCE;

    public static Reader getReader() {
        if (INSTANCE == null) {
            INSTANCE = new Reader();
        }
        return INSTANCE;
    }

    public <T> T readFromString(Class<T> clazz, String input) {
        T outputObj;
        Map<String, Object> fields = new HashMap<>();
        int idx = 1;
        while (input.charAt(idx) != '}') {
            StringBuilder currFieldName = new StringBuilder();
            while (input.charAt(idx) != ' ') {
                currFieldName.append(input.charAt(idx++));
            }
            idx += 4;
            if (input.charAt(idx) != '{' && input.charAt(idx) != '[') {
                StringBuilder currFieldValue = new StringBuilder();
                while ((input.charAt(idx) != '"')){
                    currFieldValue.append(input.charAt(idx++));
                }
                idx += 3;
                fields.put(currFieldName.toString(), currFieldValue.toString());
            } else if (input.charAt(idx) == '{') {
                fields.put(currFieldName.toString(), readFromString(getFieldByKey(clazz, currFieldName.toString()).getType(), input.substring(idx)));
                idx += getIdxShift(input.substring(idx));
            }
        }
        try {
            outputObj = clazz.getConstructor().newInstance();
            getNotIgnoredFields(clazz).forEach(x -> {
                x.setAccessible(true);
                if (x.getType().getClassLoader() == null && x.getType() != List.class && x.getType() != Set.class) {
                    setSimpleValToField(outputObj, fields, x);
                } else if (x.getType().getClassLoader() != null) {
                    if (x.getType() != List.class && x.getType() != Set.class) {
                        setCustomValToField(outputObj, fields, x);
                    }
                }
            });
            return outputObj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Stream<Field> getNotIgnoredFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(x -> Validator.checkType(x.getType()))
                .filter(x -> Arrays.stream(x.getDeclaredAnnotations())
                        .noneMatch(y -> y.annotationType() == Ignored.class));
    }

    private void setSimpleValToField(Object target, Map<String, Object> fields, Field field) {
        var key = getFieldKey(field);
        try {
            if (field.getType() == int.class || field.getType() == Integer.class) {
                field.set(target, Integer.parseInt(fields.get(key).toString()));
            } else if (field.getType() == byte.class || field.getType() == Byte.class) {
                field.set(target, Byte.parseByte(fields.get(key).toString()));
            } else if (field.getType() == short.class || field.getType() == Short.class) {
                field.set(target, Short.parseShort(fields.get(key).toString()));
            } else if (field.getType() == long.class || field.getType() == Long.class) {
                field.set(target, Long.parseLong(fields.get(key).toString()));
            } else if (field.getType() == double.class || field.getType() == Double.class) {
                field.set(target, Double.parseDouble(fields.get(key).toString()));
            } else if (field.getType() == float.class || field.getType() == Float.class) {
                field.set(target, Float.parseFloat(fields.get(key).toString()));
            } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                field.set(target, Boolean.parseBoolean(fields.get(key).toString()));
            } else if (field.getType() == char.class || field.getType() == Character.class) {
                field.set(target, (fields.get(key)).toString().charAt(0));
            } else if (field.getType() == String.class) {
                field.set(target, (fields.get(key)));
            }
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException("unable to desrialize object");
        }
    }

    private void setCustomValToField(Object target, Map<String, Object> fields, Field field) {
        var key = getFieldKey(field);
        try {
            field.set(target, fields.get(key));
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException("unable to desrialize object");
        }
    }

    private String getFieldKey(Field field) {
        String key;
        if (Arrays.stream(field.getDeclaredAnnotations()).anyMatch(x -> x.annotationType() == PropertyName.class)) {
            return field.getDeclaredAnnotation(PropertyName.class).value();
        } else {
            return field.getName();
        }
    }

    private Field getFieldByKey(Class<?> clazz, String key) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(x -> x.getName().equals(key) || Arrays.stream(x.getDeclaredAnnotations())
                        .anyMatch(y -> y.annotationType() == PropertyName.class) &&
                        x.getDeclaredAnnotation(PropertyName.class).value().equals(key)).toList().get(0);
    }

    private int getIdxShift(String inputStr) {
        int answ = 0;
        int leftBracketsCount = 1;
        int rightBracketsCount = 0;
        while (leftBracketsCount != rightBracketsCount) {
            ++answ;
            if (inputStr.charAt(answ) == '}') {
                ++rightBracketsCount;
            }
            if (inputStr.charAt(answ) == '{') {
                ++leftBracketsCount;
            }
        }
        return (answ + 4);
    }
}
