package stepup.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class StudentTest {
    Student student;

    @BeforeEach
    public void createStudent() {
        student = new Student("Sergey");
    }

    @AfterEach
    public void deleteStudent() {
        student = null;
    }

    @RepeatedTest(value = 4, name = "Test add correct grade")
    public void addCorrectGradeTest(RepetitionInfo repetitionInfo) {
        student.addGrade(repetitionInfo.getCurrentRepetition() + 1);
        Assertions.assertEquals(student.getGrades().get(0), repetitionInfo.getCurrentRepetition() + 1);
    }

    @ParameterizedTest (name = "{0} is wrong grade")
    @ValueSource(ints = { 0, 1, 6, 7 })
    public void addWrongGradeTest(int argument) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> student.addGrade(argument));
    }

    @Test
    public void checkStudentNameTest() {
        Assertions.assertEquals("Sergey", student.getName());
    }

    @Test
    public void checkAddStudentNameTest() {
        student.setName("Tom");
        Assertions.assertEquals("Tom", student.getName());
    }

    @Test
    public void checkHashStudentNameTest() {
        student.setName("Tom");
        Assertions.assertEquals(1096746, student.hashCode());
    }

    @Test
    public void checkStudentObjNullTest() {
        assertNotEquals(null, student);
        Assertions.assertTrue(student.equals(student));
        assertNotEquals(student, student.getClass());
    }

    @Test
    public void checkStudentsToStringTest() {
        Assertions.assertEquals("Student{name=Sergey, marks=[]}", student.toString());
    }
}

