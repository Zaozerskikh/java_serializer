package implementation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс, реализующий основную механику работы Mappera - работу с
 * вводом-выводом данных, и свойством retainIdentity.
 */
public class Mapper implements ru.hse.homework4.Mapper {

    /**
     * Хэш таблица для реализации поддержки retainIdentity.
     */
    private Map<String, Object> deserialized;

    /**
     * Само свойство retainIdentity.
     */
    private final boolean retainIdentity;

    /**
     * Конструктор.
     * @param retainIdentity свойство из тз.
     */
    public Mapper(boolean retainIdentity) {
        this.retainIdentity = retainIdentity;
        if (retainIdentity) {
            deserialized = new HashMap<>();
        }
    }

    /**
     * Запись объекта в строку.
     * @param object объект.
     * @return строковое представление объекта согласно формату.
     * @throws UnsupportedOperationException если сериализовать объект невозможно.
     */
    @Override
    public String writeToString(Object object) throws UnsupportedOperationException {
        Validator.validateObject(object);
        return Writer.getWriter().write(object);
    }

    /**
     * Запись объекта в поток данных согласно формату.
     * @param object объект.
     * @param outputStream поток.
     * @throws IOException если ошибка связана с потоком.
     * @throws UnsupportedOperationException если сериализовать объект невозможно.
     */
    @Override
    public void write(Object object, OutputStream outputStream) throws IOException, UnsupportedOperationException {
        try {
            outputStream.write(Writer.getWriter().write(object).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IOException("File error: " + e.getMessage());
        }
    }

    /**
     * Запись объекта в файл согласно формату.
     * @param object объект.
     * @param file файл.
     * @throws IOException если ошибка связана с файлом.
     * @throws UnsupportedOperationException если сериализовать объект невозможно.
     */
    @Override
    public void write(Object object, File file) throws IOException, UnsupportedOperationException {
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print(Writer.getWriter().write(object));
        } catch (IOException e) {
            throw new IOException("File error: " + e.getMessage());
        }
    }

    /**
     * Считывание объекта из строки.
     * @param clazz тип объекта, который надо считать.
     * @param input строка, из которой надо считать объект.
     * @param <T> типизирующий параметр класса объекта.
     * @return десериализованный объект.
     * @throws UnsupportedOperationException если десериализовать объект (ошибка валидации класса объекта).
     * @throws IllegalArgumentException если входная строка некорректного формата.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T readFromString(Class<T> clazz, String input) throws UnsupportedOperationException, IllegalArgumentException {
        try {
            Validator.validateClass(clazz);
            var obj = Reader.getReader().readFromString(clazz, input);
            if (retainIdentity) {
                if (deserialized.containsKey(input)) {
                    // unchecked каст проходит всегда без ошибок, ведь в
                    // мапе хранится объект типа T по ключу input.
                    return (T) deserialized.get(input);
                } else {
                    deserialized.put(input, obj);
                }
            }
            return obj;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Input string coud not be deserialized to \"" + clazz.getSimpleName() + "\" instance");
        }
    }

    /**
     * Считывание объекта из потока.
     * @param clazz тип объекта, который надо считать.
     * @param inputStream поток, из которого надо считать объект.
     * @param <T> типизирующий параметр класса объекта.
     * @return десериализованный объект.
     * @throws IOException если проблема с входным потоком.
     * @throws UnsupportedOperationException если десериализовать объект (ошибка валидации класса объекта).
     * @throws IllegalArgumentException если строка во входном потоке некорректного формата.
     */
    @Override
    public <T> T read(Class<T> clazz, InputStream inputStream) throws IOException, UnsupportedOperationException, IllegalArgumentException {
        try {
            return readFromString(clazz, new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IOException("File error: " + e.getMessage());
        }
    }

    /**
     * Считывание объекта из файла.
     * @param clazz тип объекта, который надо считать.
     * @param file файл, из которого надо считать объект.
     * @param <T> типизирующий параметр класса объекта.
     * @return десериализованный объект.
     * @throws IOException если проблема с входным файлом.
     * @throws UnsupportedOperationException если десериализовать объект (ошибка валидации класса объекта).
     * @throws IllegalArgumentException если строка во входном файле некорректного формата.
     */
    @Override
    public <T> T read(Class<T> clazz, File file) throws IOException, UnsupportedOperationException, IllegalArgumentException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return readFromString(clazz, reader.readLine());
        } catch (IOException e) {
            throw new IOException("File error: " + e.getMessage());
        }
    }
}
