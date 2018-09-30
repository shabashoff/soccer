package ru.shabashoff.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.GameObject;
import ru.shabashoff.entity.Params;

import java.util.HashMap;
import java.util.Map;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MsgParser {
    public Map<GameObject, Params> parseSeeMessage(String message) {
        HashMap<GameObject, Params> map = new HashMap<>();
        char[] chars = message.toCharArray();

        int n = 0;

        while (n < chars.length) {
            while (isSpace(chars[n])) n++;

            String nextStr = getNextStr(chars, n);
            log.info(nextStr);

            n += nextStr.length();
        }

        return null;
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
