package ru.shabashoff.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
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
