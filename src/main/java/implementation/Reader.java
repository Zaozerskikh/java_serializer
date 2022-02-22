package implementation;

import ru.hse.homework4.Ignored;
import ru.hse.homework4.PropertyName;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

/**
 * Класс, реализующий механику чтения строки сериализованного объекта
 * и преобразования ее обратно в объект.
 */
public class Reader {
    /**
     * Простейшая реализация синглтона.
     * Приватный конструктор.
     */
    private Reader() { }

    /**
     * Объект класса Writer для реализации синглтона.
     */
    private static Reader INSTANCE;

    /**
     * Getter, инициализирующий Reader лишь единожды, и возвращающий его в дальнейшем.
     */
    public static Reader getReader() {
        if (INSTANCE == null) {
            INSTANCE = new Reader();
        }
        return INSTANCE;
    }

    /**
     * Основной метод считывания объекта из строки.
     * @param clazz класс объекта.
     * @param input входная строка.
     * @param <T> типизирующий параметр - тип выходного объекта.
     * @return выходной объект.
     */
    public <T> T readFromString(Class<T> clazz, String input) {
        try {
            T outputObj;
            outputObj = clazz.getConstructor().newInstance();
            Map<String, Object> fields = new HashMap<>();
            int idx = 1;
            while (input.charAt(idx) != '}' && input.charAt(idx) != ']') {
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
                    if (idx + 1 != '}') {
                        fields.put(currFieldName.toString(), readFromString(getFieldByKey(clazz, currFieldName.toString()).getType(), input.substring(idx)));
                    } else {
                        fields.put(currFieldName.toString(), null);
                    }
                    idx += getIdxShift(input.substring(idx), '{', '}');
                } else if (input.charAt(idx) == '[') {
                    if (input.charAt(idx + 1) != ']') {
                        fields.put(currFieldName.toString(), readCollection(getFieldByKey(clazz, currFieldName.toString()), outputObj, input.substring(idx)));
                    } else {
                        fields.put(currFieldName.toString(), null);
                    }
                    idx += getIdxShift(input.substring(idx), '[', ']');
                }
            }
            getNotIgnoredFields(clazz).forEach(x -> {
                x.setAccessible(true);
                if (x.getType().getClassLoader() == null && x.getType() != List.class && x.getType() != Set.class) {
                    setSimpleValToField(outputObj, fields, x);
                } else {
                    setCustomValToField(outputObj, fields, x);
                }
            });
            return outputObj;
        } catch (Exception e) {
            throw new UnsupportedOperationException("unable to desrialize object");
        }
    }

    /**
     * Получение не игнорируемых полей класса.
     * @param clazz класс.
     * @return поток полей, не помеченных @Ignored и валидных по типу.
     */
    private Stream<Field> getNotIgnoredFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(x -> Validator.checkType(x.getType()))
                .filter(x -> Arrays.stream(x.getDeclaredAnnotations())
                        .noneMatch(y -> y.annotationType() == Ignored.class));
    }

    /**
     * Запись значения примитивного типа в поле.
     * @param target объект, в котором находиться поле.
     * @param fields хэш таблица типа (ключ поля - значение поля) для всех актуальных полей класса.
     * @param field поле, в которое необходимо записать примитивное значение.
     */
    private void setSimpleValToField(Object target, Map<String, Object> fields, Field field) {
        String key = getFieldKey(field);
        try {
            if (fields.get(key).toString().equals("null")) {
                field.set(target, null);
                return;
            } if (field.getType() == int.class || field.getType() == Integer.class) {
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
            } else if (field.getType() == LocalDate.class) {
                field.set(target, LocalDate.parse(formatData(fields.get(key).toString())));
            }
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException("unable to desrialize object");
        }
    }

    /**
     * Запись значения кастомного типа в поле.
     * @param target объект, в котором находиться поле.
     * @param fields хэш таблица типа (ключ поля - значение поля) для всех актуальных полей класса.
     * @param field поле, в которое необходимо записать значение.
     */
    private void setCustomValToField(Object target, Map<String, Object> fields, Field field) {
        try {
            field.set(target, fields.get(getFieldKey(field)));
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException("unable to desrialize object");
        }
    }

    /**
     * Получение ключа поля (из аннотации PropertyName, или в случае ее отсутствия - по названию).
     * @param field поле.
     * @return ключ поля в виде строки.
     */
    private String getFieldKey(Field field) {
        String key;
        if (Arrays.stream(field.getDeclaredAnnotations()).anyMatch(x -> x.annotationType() == PropertyName.class)) {
            return field.getDeclaredAnnotation(PropertyName.class).value();
        } else {
            return field.getName();
        }
    }

    /**
     * Получения поля по ключу.
     * @param clazz класс поля.
     * @param key ключ.
     * @return поле.
     */
    private Field getFieldByKey(Class<?> clazz, String key) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(x -> x.getName().equals(key) || Arrays.stream(x.getDeclaredAnnotations())
                        .anyMatch(y -> y.annotationType() == PropertyName.class) &&
                        x.getDeclaredAnnotation(PropertyName.class).value().equals(key)).toList().get(0);
    }

    /**
     * форматирование даты согласно аннотации.
     * @param data дата.
     * @return отформатированная дата в формате строки.
     */
    private String formatData(String data) {
        var newData = data.split("-");
        if (newData[0].length() == 2) {
            var temp = newData[0];
            newData[0] = newData[2];
            newData[2] = temp;
        }
        return newData[0] + "-" + newData[1] + "-" + newData[2];
    }

    /**
     * Чтение коллекции из строки.
     * @param collection поле коллекции.
     * @param target объект, в котором лежит поле коллекции.
     * @param input строка
     * @return считанный объект коллекции.
     * @throws Exception если распарсить не удалось.
     * warning'и подавлены т.к без raw типов, и, как следствие, unchecked add'ов
     * распарсить произвольную коллекцию довольно проблематично.
     */
    @SuppressWarnings("all")
    private Collection<?> readCollection(Field collection, Object target, String input) throws  Exception {
        Collection parsedCollection;
        try {
            collection.setAccessible(true);
            parsedCollection = collectionCast(collection.get(target));
            int idx = 1;
            while (input.charAt(idx) != ']') {
                if (input.charAt(idx) == '{') {
                    parsedCollection.add(readFromString(Validator.getTypeParam(collection), input.substring(idx)));
                    idx += getIdxShift(input.substring(idx), '{', '}');
                    idx--;
                } else {
                    if (input.charAt(idx++) == '"') {
                        StringBuilder sb = new StringBuilder();
                        while (input.charAt(idx) != '"') {
                            sb.append(input.charAt(idx++));
                        }
                        addPrimitiveTypeELementToCollection(parsedCollection, sb.toString(), Validator.getTypeParam(collection));
                        ++idx;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException();
        }
        return parsedCollection;
    }

    /**
     * Добавление в коллекцию элемента встроенного (classLoader == null) типа.
     * @param collection коллекция.
     * @param str строковое представление объекта встроенного типа.
     * @param elementType тип объекта.
     * warning'и подавлены т.к без raw типов, и, как следствие, unchecked add'ов
     * распарсить произвольную коллекцию довольно проблематично.
     */
    @SuppressWarnings("all")
    private void addPrimitiveTypeELementToCollection(Collection collection, String str, Class<?> elementType) {
        if (str.equals("null")) {
            collection.add(null);
            return;
        } if (elementType == int.class || elementType == Integer.class) {
            collection.add(Integer.parseInt(str));
        } else if (elementType == byte.class || elementType == Byte.class) {
            collection.add(Byte.parseByte(str));
        } else if (elementType == short.class || elementType == Short.class) {
            collection.add(Short.parseShort(str));
        } else if (elementType == long.class || elementType == Long.class) {
            collection.add(Long.parseLong(str));
        } else if (elementType == double.class || elementType == Double.class) {
            collection.add(Double.parseDouble(str));
        } else if (elementType == float.class || elementType == Float.class) {
            collection.add(Float.parseFloat(str));
        } else if (elementType == boolean.class || elementType == Boolean.class) {
            collection.add(Boolean.parseBoolean(str));
        } else if (elementType == char.class || elementType == Character.class) {
            collection.add(str.charAt(0));
        } else if (elementType == String.class) {
            collection.add(str);
        } else if (elementType == LocalDate.class) {
            collection.add(formatData(str));
        }
    }

    /**
     * Каст объекта к коллекции.
     * @param obj объект.
     * @param <T> тип коллекции.
     * @return приведенный к интерфейсу коллекции объект.
     * Ошибок из-за unchecked каста не возникает, т к метод вызывается только
     * для объектов, реализующих интерфейс Collection.
     */
    @SuppressWarnings("all")
    private  <T extends Collection> T collectionCast(Object obj) {
        return (T)obj;
    }

    /**
     * Вычисление сдвига индекса строки при считывании объекта.
     * @param inputStr входная строка.
     * @param leftBracket левая скобка ("{" или "]")
     * @param rightBracket левая скобка ("}" или "[")
     * @return сдвиг индекса при считывании объекта.
     */
    private int getIdxShift(String inputStr, char leftBracket, char rightBracket) {
        int answ = 0;
        int leftBracketsCount = 1;
        int rightBracketsCount = 0;
        while (leftBracketsCount != rightBracketsCount) {
            ++answ;
            if (inputStr.charAt(answ) == rightBracket) {
                ++rightBracketsCount;
            }
            if (inputStr.charAt(answ) == leftBracket) {
                ++leftBracketsCount;
            }
        }
        return (answ + 4);
    }
}
