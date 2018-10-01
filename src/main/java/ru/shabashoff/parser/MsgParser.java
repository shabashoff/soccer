package ru.shabashoff.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.GameObject;
import ru.shabashoff.entity.SeeObjects;

import java.util.ArrayList;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MsgParser {

    public SeeObjects parseSeeMessage(String message) {
        char[] chars = message.toCharArray();
        ArrayList<GameObject> gameObjects = new ArrayList<>();

        GameObject currObj = new GameObject();

        Long start = Long.valueOf(getNextStr(chars, 5));

        int level = 0;

        for (int i = 6 + start.toString().length(); i < chars.length; i++) {

            while (isSpace(chars[i])) i++;

            if (chars[i] == '(') {
                if (level == 0) {
                    currObj = new GameObject();
                }
                level++;
            } else {
                if (chars[i] == ')') {
                    if (level == 1) {
                        gameObjects.add(currObj);
                    }
                    level--;
                } else {
                    String s = getNextStr(chars, i);

                    if (level == 2) {
                        currObj.addName(s.charAt(0));
                    } else {
                        currObj.addNum(Float.valueOf(s));
                    }
                    i += s.length() - 1;
                }
            }
        }

        return new SeeObjects(gameObjects, start);
    }


    private String getNextStr(char[] arr, int start) {

        if (isBracket(arr[start])) return new String(arr, start, 1);

        int i = start;

        while (i < arr.length && !isStopSymbol(arr[i])) {
            i++;
        }

        return new String(arr, start, i - start);
    }

    private boolean isStopSymbol(char c) {
        return isSpace(c) || isBracket(c);
    }

    private boolean isSpace(char c) {
        return c == ' ';
    }

    private boolean isBracket(char c) {
        return c == '(' || c == ')';
    }
}
