package stepup.example;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Student {
    private String name;
    private Integer id;
    private List<Integer> marks = new ArrayList<>();

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", marks=" + marks +
                '}';
    }
}
