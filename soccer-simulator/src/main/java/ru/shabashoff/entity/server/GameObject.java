package ru.shabashoff.entity.server;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GameObject {

    StringBuilder naming;
    List<Float> numbers;

    public GameObject() {
        this.naming = new StringBuilder();
        this.numbers = new ArrayList<>();
    }


    public String getName() {
        return naming.toString();
    }

    public Float getNumOnPosition(int n) {
        return numbers.get(n);
    }

    public void addName(char c) {
        naming.append(c);
    }

    public void addNum(Float n) {
        numbers.add(n);
    }
}
