package ru.shabashoff.entity.server;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GameObject {
    List<Character> naming;
    List<Float> numbers;

    public GameObject() {
        this.naming = new ArrayList<>();
        this.numbers = new ArrayList<>();
    }

    public void addName(char c) {
        naming.add(c);
    }

    public void addNum(Float n) {
        numbers.add(n);
    }
}
