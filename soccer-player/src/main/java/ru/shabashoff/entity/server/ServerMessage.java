package ru.shabashoff.entity.server;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class ServerMessage {
    MessageType type;

    public abstract void fillFields(List<String> params);

    public static ServerMessage build(MessageType type) {
        switch (type) {
            case SEE_MESSAGE:
                return new SeeMessage();
            case SERVER_PARAMS:
                return new ServerParameters();
            case PLAYER_TYPE:
                return new PlayerType();
            case INIT_MESSAGE:
                return new InitMessage();
            case PLAYER_PARAMS:
                return new PlayerParameters();
            case SENSE_BODY:
                return new SenseBody();
            case HEAR:
                return new HearMessage();
            case CHANGE_PLAYER_TYPE:
                return new ChangePlayerType();
            case ERROR:
                return new ErrorMessage();
        }
        throw new IllegalArgumentException("Can't find entity for MessageType : " + type);
    }
}
