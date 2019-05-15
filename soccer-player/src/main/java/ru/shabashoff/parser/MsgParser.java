package ru.shabashoff.parser;

import java.util.List;
import ru.shabashoff.entity.server.MessageType;
import ru.shabashoff.entity.server.ServerMessage;
import ru.shabashoff.utils.GameUtils;

public class MsgParser {

    public static ServerMessage parseMessage(String message) {
        MessageType type = parseType(message);
        ServerMessage serverMessage = ServerMessage.build(type);
        List<String> messages = GameUtils.parseMessages(
            message.substring(
                message.indexOf(' ') + 1,
                message.length() - 1
            )
        );
        serverMessage.fillFields(messages);
        return serverMessage;
    }


    private static MessageType parseType(String message) {
        String type = message.substring(message.indexOf('(') + 1, message.indexOf(' '));
        return MessageType.parseType(type);
    }
}
