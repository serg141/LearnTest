package stepup.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    StudentRestMethod restMethod = new StudentRestMethod();
    int id;

    @BeforeAll
    static void setUpBeforeClass() {
        Specification.installSpec(Specification.requestSpec());
    }

    @AfterEach
    public void deleteStudent() {
        restMethod.deleteStudent(id);
        System.out.println("Student deleted " + id);
    }

    @Test
    public void getNotExistStudentTest() {
        given().when().get("/student/-1").then().statusCode(404);
    }

    @Test
    public void getExistStudentTest() {
        id = 1;
        restMethod.addStudent("{\"name\":\"Tom\",\"id\":" + id + "}");
        assertEquals("Tom", restMethod.getStudent(id).getName());
        assertEquals(id, restMethod.getStudent(id).getId());
        assertEquals(new ArrayList<>(), restMethod.getStudent(id).getMarks());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "{\"name\":\"Tom\",    \"id\":2};                         Tom;    2",
            "{\"name\":\"Alex\",   \"id\":3,     \"marks\":[2,3,4]};  Alex;   3",
            "{\"name\":\"Vasya\",  \"id\":4,     \"marks\":[]};       Vasya;  4",
            "{\"name\":\"Vlad\",   \"id\":5,     \"marks\":null};     Vlad;   5"
    }, delimiter = ';')
    public void addStudentTest(String str, String name, int id) {
        this.id = id;
        restMethod.addStudent(str);
        assertEquals(id, restMethod.getStudent(id).getId());
        assertEquals(name, restMethod.getStudent(id).getName());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "{\"name\":\"Sergey\"};                                Sergey",
            "{\"name\":\"Anton\", \"id\":null};                    Anton",
            "{\"name\":\"Dima\",  \"id\":null, \"marks\":[2,3,4]}; Dima",
            "{\"name\":\"Kira\",  \"id\":null, \"marks\":[]};      Kira",
    }, delimiter = ';')
    public void addStudentIfIdNullTest(String str, String name) {
        id = restMethod.addStudentIdNull(str);
        assertEquals(name, restMethod.getStudent(id).getName());
    }

    @ParameterizedTest
    @CsvSource(value = {
            //"{\"name\":123}",
            //"{\"name\":true}",
            "{\"name\":\"Anton\", \"id\":\"1\"}",
            "{\"name\":\"Dima\",  \"id\":true}",
            "{\"name\":\"Kira\",  \"marks\":[\"1\", \"2\"]}",
            "{\"name\":\"Kira\",  \"marks\":[true, false]}",
            "{\"id\":\"100\"}"
    })
    public void errorWhenCreateStudentTest(String str) {
        given().body(str).when().post("/student").then().statusCode(400);
    }

    @Test
    public void updateStudentTest() {
        id = 1;
        restMethod.addStudent("{\"name\":\"Tom\", \"id\":" + id + "}");
        assertEquals("Tom", restMethod.getStudent(id).getName());
        restMethod.addStudent("{\"name\":\"Alex\",\"id\":" + id + "}");
        assertEquals("Alex", restMethod.getStudent(id).getName());
    }

    @Test
    public void removeStudentTest() {
        id = restMethod.addStudentIdNull("{\"name\":\"Tom\"}");
        given().when().delete("/student/" + id).then().statusCode(200);
        given().when().delete("/student/" + id).then().statusCode(404);
    }

    @Test
    public void getTopStudentTest() throws IOException {
        restMethod.addStudent("{\"name\":\"Tom\", \"id\":1,\"marks\":[2,3,4]}");
        restMethod.addStudent("{\"name\":\"Tom1\",\"id\":2,\"marks\":[2,3,4,4,4,4]}");

        List<Student> students = restMethod.getTopStudent();
        assertEquals("Tom1", students.get(0).getName());
        assertNotNull(students.get(0).getId());
        assertNotNull(students.get(0).getMarks());
    }

    @Test
    public void getNullTopStudentTest() {
        given().when().get("/topStudent").then().spec(Specification.responseSpec200());
    }

    @Test
    public void getNullMarksTopStudentTest() {
        id = restMethod.addStudentIdNull("{\"name\":\"Tom\"}");
        given().when().get("/topStudent").then().spec(Specification.responseSpec200());
    }

    @Test
    public void getTopStudentWithMoreMarksTest() throws IOException {
        restMethod.addStudent("{\"name\":\"Dima\", \"id\":1, \"marks\":[4,4]}");
        restMethod.addStudent("{\"name\":\"Dima1\",\"id\":2, \"marks\":[4,4,4]}");

        List<Student> students = restMethod.getTopStudent();
        assertEquals("Dima1", students.get(0).getName());
        assertNotNull(students.get(0).getId());
        assertNotNull(students.get(0).getMarks());
    }

    @Test
    public void getTwoTopStudentTest() throws IOException {
        restMethod.addStudent("{\"name\":\"Dima\", \"id\":1, \"marks\":[4,4,4]}");
        restMethod.addStudent("{\"name\":\"Dima1\",\"id\":2, \"marks\":[4,4,4]}");

        List<Student> students = restMethod.getTopStudent();
        assertEquals("Dima", students.get(0).getName());
        assertEquals("Dima1", students.get(1).getName());
    }
}