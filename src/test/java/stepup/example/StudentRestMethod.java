package stepup.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;

public class StudentRestMethod {
    public Student getStudent(int id) {
        return given()
                .when().get("/student/" + id)
                .then()
                .spec(Specification.responseSpec200())
                .extract().as(Student.class);
    }

    public void addStudent(String str) {
        given().body(str).when().post("/student").then().spec(Specification.responseSpec201());
    }

    public int addStudentIdNull(String str) {
        return given().body(str).when().post("/student").then().spec(Specification.responseSpec201())
                .extract().body().jsonPath().get();
    }

    public void deleteStudent(int id) {
        given().when().delete("/student/" + id);
    }

    public List<Student> getTopStudent() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Response response = given()
                .when().get("/topStudent")
                .then()
                .spec(Specification.responseSpec200())
                .extract().response();

        deleteStudent(1);
        deleteStudent(2);

        return mapper.readValue(response.asInputStream(), new TypeReference<>(){});
    }
}
