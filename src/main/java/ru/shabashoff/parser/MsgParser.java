package ru.shabashoff.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.GameObject;
import ru.shabashoff.entity.MessageType;
import ru.shabashoff.entity.SeeObjects;
import ru.shabashoff.entity.ServerMessage;

import java.util.ArrayList;
import java.util.List;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MsgParser {

    @Deprecated
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
            }
            else {
                if (chars[i] == ')') {
                    if (level == 1) {
                        gameObjects.add(currObj);
                    }
                    level--;
                }
                else {
                    String s = getNextStr(chars, i);

                    if (level == 2) {
                        currObj.addName(s.charAt(0));
                    }
                    else {
                        currObj.addNum(Float.valueOf(s));
                    }
                    i += s.length() - 1;
                }
            }
        }

        return new SeeObjects(gameObjects, start);
    }


    private List<String> getMessages(String message) {
        List<String> list = new ArrayList<>();
        char[] chars = message.toCharArray();
        int start = 0;
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(') count++;
            else if (chars[i] == ')') count--;

            if (chars[i] == ' ' && count == 0) {
                list.add(new String(chars, start, i - start));
                start = i + 1;
            }
        }
        return list;
    }


    public ServerMessage parseMessage(String message) {
        MessageType type = parseType(message);
        log.info(type);
        ServerMessage seeObjects = ServerMessage.build(type);

        List<String> messages = getMessages(message.substring(message.indexOf(' ') + 1, message.length() - 1));
        log.info(messages);
        seeObjects.fillFields(messages);
        return seeObjects;
    }


    private MessageType parseType(String message) {
        String type = message.substring(message.indexOf('(') + 1, message.indexOf(' '));
        return MessageType.parseType(type);
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
