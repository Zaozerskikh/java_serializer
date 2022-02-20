package mapper_tests.tests;

import implementation.Mapper;
import mapper_tests.tests.classes_for_tests.BadPerson;
import mapper_tests.tests.classes_for_tests.Person;
import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс, тестирующий непосредственный функционал Mapper'a, а именно:
 * 1) Сериализация из строки.
 * 2) Сериализация из файла.
 * 3) Сериализация из потока данных.
 * 4) Десериализация из строки.
 * 5) Десериализация из файла.
 * 6) Десериализация из потока данных.
 * 7) Опция retainIdentity.
 * Полные тесты корректности работы механизмов валидации, записи, и чтения объектов здесь
 * отсутствуют, так как соответствующий функционал полностью протестирован отдельно в классах
 * ValidatorTest, WriterTest и ReaderTest соответственно.
 * Здесь же стоит задача протестировать именно разные методы получения данных для сериализации,
 * и работу с retainIdentity, поэтому используются один простой исходный объект класса для тестирования.
 */
public class MapperTest {
    @Test
    public void normalReadFromString() {
        String person = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
        new Mapper(false).readFromString(Person.class, person);
    }

    @Test
    public void incorrectStringFormat() {
        assertThrows(UnsupportedOperationException.class, () -> {
            String person = "{name = \"Sergey\"; age = \"19\"; pet = \"name = \"some_pet_name\"; }\"; }";
            new Mapper(false).readFromString(Person.class, person);
        });
    }

    @Test
    public void readFromStringIncorrectClass() {
        assertThrows(UnsupportedOperationException.class, () -> {
            String person = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
            new Mapper(false).readFromString(BadPerson.class, person);
        });
    }

    @Test
    public void normalReadFromStream() throws IOException {
        String person = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
        new Mapper(false).read(Person.class, new ByteArrayInputStream(person.getBytes()));
    }

    @Test
    public void incorrectStreamFormat() {
        assertThrows(UnsupportedOperationException.class, () -> {
            String person = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; \"; }";
            new Mapper(false).read(Person.class, new ByteArrayInputStream(person.getBytes()));
        });
    }

    @Test
    public void readFromStreamIncorrectClass() {
        assertThrows(UnsupportedOperationException.class, () -> {
            String person = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
            new Mapper(false).read(BadPerson.class, new ByteArrayInputStream(person.getBytes()));
        });
    }

    @Test
    public void normalReadFromFile() throws IOException {
        var file = new File(Path.of("").toAbsolutePath() + "/src/test/resources/mapper_test.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            var obj1 = new Mapper(false).readFromString(Person.class, reader.readLine());
        }
    }

    @Test
    public void incorrectFileDataFormat() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var file = new File(Path.of("").toAbsolutePath() + "/src/test/resources/bad_mapper_test.txt");
            try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
                var obj1 = new Mapper(false).readFromString(Person.class, reader.readLine());
            }
        });
    }

    @Test
    public void fileNotExists() {
        assertThrows(FileNotFoundException.class, () -> {
            var file = new File(Path.of("").toAbsolutePath() + "/src/test/resources/not_a_file_lol.txt");
            try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
                var obj1 = new Mapper(false).readFromString(Person.class, reader.readLine());
            }
        });
    }

    @Test
    public void readFromFileIncorrectClass() {
        assertThrows(UnsupportedOperationException.class, () -> {
            var file = new File(Path.of("").toAbsolutePath() + "/src/test/resources/mapper_test.txt");
            try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
                var obj1 = new Mapper(false).readFromString(BadPerson.class, reader.readLine());
            }
        });
    }

    @Test
    public void retainIdentity_TrueCheckObj_ReadFromString() {
        var mapper = new Mapper(true);
        String person = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
        assertSame(mapper.readFromString(Person.class, person), mapper.readFromString(Person.class, person));
    }

    @Test
    public void retainIdentityTrue_CheckObjField_ReadFromString() {
        var mapper = new Mapper(true);
        String person = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
        assertSame(mapper.readFromString(Person.class, person).getPet(), mapper.readFromString(Person.class, person).getPet());
    }

    @Test
    public void retainIdentityFalse_CheckObj_ReadFromString() {
        var mapper = new Mapper(false);
        String person = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
        assertNotSame(mapper.readFromString(Person.class, person), mapper.readFromString(Person.class, person));
    }

    @Test
    public void retainIdentityFalse_CheckObjField_ReadFromString() {
        var mapper = new Mapper(false);
        String person = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
        assertNotSame(mapper.readFromString(Person.class, person).getPet(), mapper.readFromString(Person.class, person).getPet());
    }

    @Test
    public void retainIdentity_TrueCheckObj_ReadFromStream() throws IOException {
        var mapper = new Mapper(true);
        String personString = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
        assertSame(mapper.read(Person.class, new ByteArrayInputStream(personString.getBytes())),
                mapper.read(Person.class, new ByteArrayInputStream(personString.getBytes())));
    }

    @Test
    public void retainIdentityTrue_CheckObjField_ReadFromStream() throws IOException {
        var mapper = new Mapper(true);
        String personString = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
        assertSame(mapper.read(Person.class, new ByteArrayInputStream(personString.getBytes())).getPet(),
                mapper.read(Person.class, new ByteArrayInputStream(personString.getBytes())).getPet());
    }

    @Test
    public void retainIdentityFalse_CheckObj_ReadFromStream() throws IOException {
        var mapper = new Mapper(false);
        String personString = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
        assertNotSame(mapper.read(Person.class, new ByteArrayInputStream(personString.getBytes())),
                mapper.read(Person.class, new ByteArrayInputStream(personString.getBytes())));
    }

    @Test
    public void retainIdentityFalse_CheckObjField_ReadFromStream() throws IOException {
        var mapper = new Mapper(false);
        String personString = "{name = \"Sergey\"; age = \"19\"; pet = \"{name = \"some_pet_name\"; }\"; }";
        assertNotSame(mapper.read(Person.class, new ByteArrayInputStream(personString.getBytes())).getPet(),
                mapper.read(Person.class, new ByteArrayInputStream(personString.getBytes())).getPet());
    }

    @Test
    public void retainIdentity_TrueCheckObj_ReadFromFile() throws IOException {
        var mapper = new Mapper(true);
        var file = new File(Path.of("").toAbsolutePath() + "/src/test/resources/mapper_test.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String person = reader.readLine();
            assertSame(mapper.readFromString(Person.class, person), mapper.readFromString(Person.class, person));
        }
    }

    @Test
    public void retainIdentityTrue_CheckObjField_ReadFromFile() throws IOException {
        var mapper = new Mapper(true);
        var file = new File(Path.of("").toAbsolutePath() + "/src/test/resources/mapper_test.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String person = reader.readLine();
            assertSame(mapper.readFromString(Person.class, person).getPet(), mapper.readFromString(Person.class, person).getPet());
        }
    }

    @Test
    public void retainIdentityFalse_CheckObj_ReadFromFile() throws IOException{
        var mapper = new Mapper(false);
        var file = new File(Path.of("").toAbsolutePath() + "/src/test/resources/mapper_test.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String person = reader.readLine();
            assertNotSame(mapper.readFromString(Person.class, person), mapper.readFromString(Person.class, person));
        }
    }

    @Test
    public void retainIdentityFalse_CheckObjField_ReadFromFile() throws IOException{
        var mapper = new Mapper(false);
        var file = new File(Path.of("").toAbsolutePath() + "/src/test/resources/mapper_test.txt");
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String person = reader.readLine();
            assertNotSame(mapper.readFromString(Person.class, person).getPet(), mapper.readFromString(Person.class, person).getPet());
        }
    }
}
