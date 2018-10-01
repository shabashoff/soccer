package ru.shabashoff.entity;

import lombok.Data;

import java.util.List;

@Data
public class SeeObjects {
    final List<GameObject> gameObjects;
    final Long time;
}
