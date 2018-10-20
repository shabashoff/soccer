package ru.shabashoff.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Getter
public class SeeObjects extends ServerMessage {
    final List<GameObject> gameObjects;

    @Setter
    Long time;

    public SeeObjects() {
        super(MessageType.SEE_MESSAGE);
        this.gameObjects = new ArrayList<>();
    }

    public SeeObjects(Long time) {
        super(MessageType.SEE_MESSAGE);
        this.gameObjects = new ArrayList<>();
        this.time = time;
    }

    public SeeObjects(List<GameObject> gameObjects, Long time) {
        super(MessageType.SEE_MESSAGE);
        this.gameObjects = gameObjects;
        this.time = time;
    }

    @Override
    public void fillFields(List<String> params) {
        time = Long.valueOf(params.get(0));
        for (int i = 1; i < params.size(); i++) {
            gameObjects.add(parseGameObject(params.get(i)));
        }
    }

    private GameObject parseGameObject(String str) {
        GameObject gameObject = new GameObject();
        int l = 0;
        int start = 0;
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (c == '(') {
                start = i + 1;
                l++;
            }

            if (start != i && (c == ')' || c == ' ')) {
                if (l == 2) {
                    gameObject.addName(charArray[start]);
                }
                else {
                    gameObject.addNum(Float.valueOf(str.substring(start, i)));
                }
                start = i + 1;
            }

            if (c == ')') l--;
        }
        return gameObject;
    }
}
