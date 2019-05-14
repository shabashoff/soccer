package ru.shabashoff.entity.server;

public enum MessageType {
    SERVER_PARAMS, PLAYER_PARAMS, PLAYER_TYPE, SEE_MESSAGE, INIT_MESSAGE, SENSE_BODY,CHANGE_PLAYER_TYPE,SEE_GLOBAL,HEAR, ERROR;

    public static MessageType parseType(String type) {
        switch (type) {
            case "server_param":
                return SERVER_PARAMS;
            case "player_type":
                return PLAYER_TYPE;
            case "see":
                return SEE_MESSAGE;
            case "init":
                return INIT_MESSAGE;
            case "sense_body":
                return SENSE_BODY;
            case "player_param":
                return PLAYER_PARAMS;
            case "change_player_type":
                return CHANGE_PLAYER_TYPE;
            case "hear":
                return HEAR;
            case "see_global":
                return SEE_GLOBAL;
            case "error":
                return ERROR;
        }
        throw new IllegalArgumentException("Can't parse monitor message type: " + type);
    }
}
