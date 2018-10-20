package ru.shabashoff.entity;

public enum MessageType {
    SERVER_PARAMS, PLAYER_TYPE, SEE_MESSAGE, INIT_MESSAGE, SENSE_BODY;//TODO: check message types!

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
        }
        throw new IllegalArgumentException("Can't parse server message type: " + type);
    }
}
