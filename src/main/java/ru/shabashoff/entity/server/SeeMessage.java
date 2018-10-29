package ru.shabashoff.entity.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;

@Log4j
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeeMessage extends ServerMessage {
    final List<GameObject> gameObjects;

    long time;

    public SeeMessage() {
        super(MessageType.SEE_MESSAGE);
        this.gameObjects = new ArrayList<>();
    }

    @Override
    public void fillFields(List<String> params) {
        time = Long.parseLong(params.get(0));
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
