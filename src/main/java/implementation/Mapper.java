package implementation;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Mapper implements ru.hse.homework4.Mapper {

    private Map<String, Object> deserialized;

    private final boolean retainIdentity;

    public Mapper(boolean readIdentity) {
        this.retainIdentity = readIdentity;
        if (readIdentity) {
            deserialized = new HashMap<>();
        }
    }

    @Override
    public String writeToString(Object object) throws UnsupportedOperationException {
        Validator.validate(object.getClass());
        return Writer.getWriter().write(object, new StringBuilder(), null);
    }

    @Override
    public void write(Object object, OutputStream outputStream) throws IOException, UnsupportedOperationException {
        Validator.validate(object.getClass());
        outputStream.write(Writer.getWriter()
                .write(object, new StringBuilder(), null)
                .getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void write(Object object, File file) throws IOException, UnsupportedOperationException {
        Validator.validate(object.getClass());
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.print(Writer.getWriter().write(object, new StringBuilder(), null));
        } catch (Exception e) {
            System.out.println("Serialization error: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T readFromString(Class<T> clazz, String input) {
        try {
            var obj = Reader.getReader().readFromString(clazz, input);
            if (retainIdentity) {
                if (deserialized.containsKey(input)) {
                    // unchecked каст проходит всегда без ошибок, ведь в
                    // мапе хранится объект типа T по ключу input.
                    return (T) deserialized.get(input);
                } else {
                    deserialized.put(input, obj);
                    return obj;
                }
            }
            return obj;
        } catch (Exception e) {
            throw new IllegalArgumentException("Input string coud not be deserialized to \"" + clazz.getSimpleName() + "\" instance");
        }
    }

    @Override
    public <T> T read(Class<T> clazz, InputStream inputStream) throws IOException {
        try {
            return Reader.getReader().readFromString(clazz, new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalArgumentException("Input string coud not be deserialized to \"" + clazz.getSimpleName() + "\" instance");
        }
    }

    @Override
    public <T> T read(Class<T> clazz, File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return Reader.getReader().readFromString(clazz, reader.readLine());
        } catch (Exception e) {
            throw new IllegalArgumentException("Input string coud not be deserialized to \"" + clazz.getSimpleName() + "\" instance");
        }
    }
}
