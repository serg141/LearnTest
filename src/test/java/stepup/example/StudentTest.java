package stepup.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

public class StudentTest {
    Student student;
    StudentRepo repo;

    @BeforeEach
    public void createStudent() {
        student = new Student("Sergey");
        repo = Mockito.mock(StudentRepo.class);
        student.setStudentRepo(repo);
    }

    @AfterEach
    public void deleteStudent() {
        student = null;
        repo = null;
    }

    @RepeatedTest(value = 4, name = "Test add correct grade")
    public void addCorrectGradeTest(RepetitionInfo repetitionInfo) {
        Mockito.when(repo.checkGrade(repetitionInfo.getCurrentRepetition() + 1))
                        .thenReturn(true);
        student.addGrade(repetitionInfo.getCurrentRepetition() + 1);
        Assertions.assertEquals(student.getGrades().get(0), repetitionInfo.getCurrentRepetition() + 1);
    }

    @ParameterizedTest(name = "{0} is wrong grade")
    @ValueSource(ints = { 0, 1, 6, 7 })
    public void addWrongGradeTest(int argument) {
        Mockito.when(repo.checkGrade(argument)).thenReturn(false);
        Assertions.assertThrows(IllegalArgumentException.class, () -> student.addGrade(argument));
    }
}