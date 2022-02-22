package implementation;

import ru.hse.homework4.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Класс, реализующий механику записи объекта в строку.
 */
public class Writer {
    /**
     * Простейшая реализация синглтона.
     * Приватный конструктор.
     */
    private Writer() { }

    /**
     * Объект класса Writer для реализации синглтона.
     */
    private static Writer INSTANCE;

    /**
     * Getter, инициализирующий Writer лишь единожды, и возвращающий его в дальнейшем.
     */
    public static Writer getWriter() {
        if (INSTANCE == null) {
            INSTANCE = new Writer();
        }
        return INSTANCE;
    }

    /**
     * Обертка приватного метода write(Object obj, StringBuilder sb, String fieldName),
     * выполняющий роль фасада и отсекающий лишние символы в конце строки.
     * @param obj объект который необходимо записать в строку.
     * @return сериализованный в виде строки объект.
     */
    public String write(Object obj) {
        String output = write(obj, new StringBuilder(), null);
        return output.substring(0, output.length() - 2);
    }

    /**
     * Записть объекта в строку.
     * @param obj записываемый объект
     * @param sb stringBuilder для записи объекта.
     * @param fieldName имя поля, в котором лежит объект (если null, то объект является корнем).
     * @return Строка, содержащая строковое представление объекта.
     */
    private String write(Object obj, StringBuilder sb, String fieldName) {
        if (fieldName != null) {
            sb.append(fieldName).append(" = \"");
        }
        sb.append("{");
        if (obj == null) {
            return sb.append("}\"; ").toString();
        }
        getActualFields(obj).forEach(x -> {
            try {
                x.setAccessible(true);
                if (x.getType().getClassLoader() == null && x.getType() != List.class && x.getType() != Set.class) {
                    simpleTypeWrite(x, obj, sb);
                } else if (x.getType().getClassLoader() != null) {
                    write(x.get(obj), sb, x.getName());
                } else if (x.getType() == List.class || x.getType() == Set.class) {
                    collectionWrite(x, obj, sb);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to serialize. Problem in: " + obj.getClass().getSimpleName());
            }
        });
        return fieldName == null ? sb.append("}; ").toString() : sb.append("}\"; ").toString();
    }

    /**
     * Получение актуальных полей объекта (not ingnored и, если требуется, not null).
     * @param obj объект, из которого надо вытащить поля.
     * @return поток искомых полей объекта.
     */
    private List<Field> getActualFields(Object obj) {
        var fields = Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(x -> Validator.checkType(x.getType()))
                .filter(x -> Arrays.stream(x.getDeclaredAnnotations())
                        .noneMatch(y -> y.annotationType() == Ignored.class));

        // Проверка на включение/исключение null полей.
        if (obj.getClass().getAnnotation(Exported.class).nullHandling() == NullHandling.EXCLUDE) {
            return fields.filter(x -> {
                try {
                    x.setAccessible(true);
                    return x.get(obj) != null;
                } catch (IllegalAccessException e) {
                    return false;
                }
            }).toList();
        }
        return fields.toList();

    }

    /**
     * Запись в строку объекта простого типа (примитивы и их врапперы, строки, даты, время).
     * @param field поле в котором содержится объект.
     * @param obj объект-родитель, в котором это поле находится.
     * @param sb stringBuilder для записи в него информации.
     */
    private void simpleTypeWrite(Field field, Object obj, StringBuilder sb) {
        // Проверка аннотации PropertyName и запись ключа.
        if (Arrays.stream(field.getDeclaredAnnotations()).anyMatch(x -> x.annotationType() == PropertyName.class)) {
            sb.append(field.getDeclaredAnnotation(PropertyName.class).value()).append(" ");
        } else {
            sb.append(field.getName()).append(" ");
        }

        // Запись значения.
        try {
            if (field.getType() == LocalDate.class) {
                sb.append("= \"").append(formatDate(obj, field)).append("\"; ");
                return;
            }
            sb.append("= \"").append(field.get(obj)).append("\"; ");
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException("Unable to serialize object");
        }
    }

    /**
     * Запись в строку объекта типа коллекции (List, Set).
     * @param list поле, в котором содержится коллекция.
     * @param obj объект-родитель, в котором это поле находится.
     * @param sb stringBuilder для записи в него информации.
     */
    private void collectionWrite(Field list, Object obj, StringBuilder sb) {
        // Проверка аннотации PropertyName и запись ключа.
        if (Arrays.stream(list.getDeclaredAnnotations()).anyMatch(x -> x.annotationType() == PropertyName.class)) {
            sb.append(list.getDeclaredAnnotation(PropertyName.class).value());
        } else {
            sb.append(list.getName());
        }

        // Запись значения.
        sb.append(" = \"[ ");
        try {
            if (Validator.getTypeParam(list).getClassLoader() == null) {
                simpleTypeCollectionWrite(list, sb, obj);
            } else {
                collectionCast(list.get(obj)).forEach(x -> write(x, sb, null));
            }
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException("Unable to serialize object");
        }
        sb.append("]\"; ");
    }

    /**
     * Запись в строку коллекции (List, Set) обобщенной примитивным типом или его враппером или строкой или датой/временем.
     * @param field поле где лежит коллекция.
     * @param sb stringBuilder куда происходит запись.
     * @param obj объект в котором лежит поле с коллекцией.
     * @throws IllegalAccessException если не удается прочитать коллекцию из поля.
     */
    private void simpleTypeCollectionWrite(Field field, StringBuilder sb, Object obj) throws IllegalAccessException {
        var collection = collectionCast(field.get(obj));
        collection.forEach(x -> {
            sb.append("\"");
            if (x.getClass() == LocalDate.class &&
                    Arrays.stream(field.getDeclaredAnnotations()).anyMatch(y ->y.annotationType() == DateFormat.class) &&
                    field.getDeclaredAnnotation(DateFormat.class).dateFormat() == DateEnum.DD_MM_YYYY) {
                sb.append(reverseDate(x.toString()));
            } else {
                sb.append(x);
            }
            sb.append("\"; ");
        });
    }

    /**
     * Форматирование даты согласно аннотации DateFormat.
     * @param obj объект с полем даты.
     * @param field поле этого объекта с датой.
     * @return отформатированная дата.
     * @throws IllegalAccessException если произошла ошибка считывания данных из поля.
     */
    private String formatDate(Object obj, Field field) throws IllegalAccessException {
        if (Arrays.stream(field.getDeclaredAnnotations()).anyMatch(x ->x.annotationType() == DateFormat.class)) {
            if (field.getDeclaredAnnotation(DateFormat.class).dateFormat() == DateEnum.DD_MM_YYYY) {
                return reverseDate(field.get(obj).toString());
            }
        }
        return field.get(obj).toString();
    }

    /**
     * Меняет местами день и год в строке с датой.
     * @param currDate дата.
     * @return дата после свопа дня и года.
     */
    private String reverseDate(String currDate) {
        var newDate = currDate.split("-");
        var tmp = newDate[0];
        newDate[0] = newDate[2];
        newDate[2] = tmp;
        return newDate[0] + "-" + newDate[1] + "-" + newDate[2];
    }

    /**
     * Каст объекта к коллекции.
     * @param obj объект.
     * @param <T> интерфейс коллекции.
     * @return коллекция.
     * unchecked каст не вызывает исключения т.к данный метод вызывается только для объектов типа  List<?> или Set<?>.
     */
    @SuppressWarnings("unchecked")
    private  <T extends Collection<?>> T collectionCast(Object obj) {
        return (T)obj;
    }
}
