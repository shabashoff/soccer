package ru.shabashoff.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.server.MessageType;
import ru.shabashoff.entity.server.ServerMessage;

import java.util.ArrayList;
import java.util.List;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MsgParser {

    private List<String> getMessages(String message) {
        List<String> list = new ArrayList<>();
        char[] chars = message.toCharArray();
        int start = 0;
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(') count++;
            else if (chars[i] == ')') count--;

            if ((chars[i] == ' ') && count == 0) {
                list.add(new String(chars, start, i - start));
                start = i + 1;
            }
        }
        list.add(new String(chars, start, message.length() - start));
        return list;
    }


    public ServerMessage parseMessage(String message) {
        MessageType type = parseType(message);
        ServerMessage serverMessage = ServerMessage.build(type);
        List<String> messages = getMessages(message.substring(message.indexOf(' ') + 1, message.length() - 1));
        serverMessage.fillFields(messages);
        return serverMessage;
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
