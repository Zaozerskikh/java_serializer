package implementation;

import ru.hse.homework4.Exported;
import ru.hse.homework4.Ignored;
import ru.hse.homework4.UnknownPropertiesPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Утилитный класс, который проверяет, возможно ли сериализовать объект класса /
 * десериализовать строку в объект указанного класса или нет.
 */
public class Validator {

    /**
     * Проверка сериализуемого объекта на null.
     * @param obj сериализуемый объект.
     */
    public static void validateObject(Object obj) {
        if (obj == null) {
            throw new UnsupportedOperationException("Unable to serialize null object");
        }
        validateClass(obj.getClass());
    }

    /**
     * Обертка над методом validateClass(Class<?> clazz) которая отлавливает циклические ссылки.
     * @param clazz проверяемый класс.
     * @throws UnsupportedOperationException если класс не может быть сериализован/десериализован.
     */
    public static void validateClass(Class<?> clazz) throws UnsupportedOperationException {
        try {
            validate(clazz);
        } catch (StackOverflowError error) {
            throw new UnsupportedOperationException("Cyclic references found");
        }
    }

    /**
     * Основной метод валидации класса.
     * @param clazz проверяемый класс.
     * @throws UnsupportedOperationException если инстанс класса не может быть сериализован.
     */
    private static void validate(Class<?> clazz) throws UnsupportedOperationException {
        // Проверка на аннотацию Exported.
        if (Arrays.stream(clazz.getDeclaredAnnotations()).noneMatch(x -> x.annotationType() == Exported.class)) {
            throw new UnsupportedOperationException("Class " + clazz.getSimpleName() + " not annotated \"@Exported\"");
        }

        // Проверка на отсутствие наследования.
        if (clazz.getSuperclass() != Object.class) {
            throw new UnsupportedOperationException("Non Object superclass detected in class: \"" + clazz.getSimpleName() + "\"");
        }

        // Проверка на конструктор без параметров.
        if (Arrays.stream(clazz.getDeclaredConstructors()).noneMatch(x -> x.getParameterCount() == 0)) {
            throw new UnsupportedOperationException("Parameterless constructor in class \"" + clazz.getSimpleName() + "\" not found");
        }

        // Проверка на "неизвестные" поля.
        Arrays.stream(clazz.getDeclaredFields()).filter(Validator::isFieldNotIgnored).forEach(x -> {
            if (!checkType(x.getType())) {
                if (clazz.getAnnotation(Exported.class).unknownPropertiesPolicy() == UnknownPropertiesPolicy.FAIL) {
                    throw new UnsupportedOperationException("Unknown field \"" + x.getName() + "\" in class: \"" + clazz.getSimpleName() + "\"");
                }
            }
        });

        // Проверка дженериков в полях типа List и Set.
        Arrays.stream(clazz.getDeclaredFields())
                .filter(x -> (x.getType() == List.class || x.getType() == Set.class))
                .filter(Validator::isFieldNotIgnored).forEach(x -> {
            if (clazz.getAnnotation(Exported.class).unknownPropertiesPolicy() ==UnknownPropertiesPolicy.FAIL && !checkListsAndSets(x)) {
                throw new UnsupportedOperationException("Unknown generic type in field \""
                        + x.getName() + "\" in class: \"" + clazz.getSimpleName() + "\"");
            } else if (checkListsAndSets(x) && getTypeParam(x).getClassLoader() != null) {
                // Рекурсивная валидация дженерик типа.
                validate(getTypeParam(x));
            }
        });

        // Рекурсивная валидация всех не игнорируемых полей пользовательских типов.
        Arrays.stream(clazz.getDeclaredFields())
                .filter(x -> x.getType().getClassLoader() != null)
                .filter(Validator::isFieldNotIgnored)
                .forEach(x -> validate(x.getType()));
    }

    /**
     * Проверка типа на соответствие сериализуемым типам из тз.
     * @param clazz проверяемый тип.
     * @return может ли тип быть сериализован.
     */
    public static boolean checkType(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == String.class || clazz == Enum.class || clazz == List.class ||
                clazz == Set.class || clazz == LocalDate.class || clazz == LocalTime.class || clazz == LocalDateTime.class ||
                clazz == Integer.class || clazz == Double.class || clazz == Float.class || clazz == Character.class ||
                clazz == Long.class || clazz == Byte.class || clazz == Short.class || clazz == Boolean.class ||
                Arrays.stream(clazz.getDeclaredAnnotations()).anyMatch(x -> x.annotationType() == Exported.class);
    }

    /**
     * Проверка полей типа List и Set.
     * @param field поле.
     * @return может ли быть сериализовано или нет.
     */
    private static boolean checkListsAndSets(Field field) {
        Class<?> typeParam;
        try {
           typeParam = getTypeParam(field);
        } catch (ClassCastException exception) {
            // Если возникает ошибка при попытке извлечь дженерик, значит это либо сырой тип, либо
            // неопределенный тип <?>. Оба случая не соответствуют тз, значит поле сериализации не подлежит.
            return false;
        }
        // Если удалось извлечь тип, то следует проверка на соответствие типам из тз.
        return checkType(typeParam);
    }

    /**
     * Получение типизирующего параметра дженерик листа или сета из поля.
     * @param field поле.
     * @return типизирующий параметр.
     * @throws ClassCastException если дженерик тип извлечь не удалось (сырой тип или <?>).
     */
    public static Class<?> getTypeParam(Field field) throws  ClassCastException {
        return (Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
    }

    /**
     * Проверка является ли поле не игнорируемым или нет.
     * @param field поле.
     * @return является ли поле не игнорируемым.
     */
    private static boolean isFieldNotIgnored(Field field) {
        return Arrays.stream(field.getDeclaredAnnotations())
                .noneMatch(x -> x.annotationType() == Ignored.class);
    }
}
