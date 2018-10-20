package ru.shabashoff.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public abstract class ServerMessage {
    MessageType type;

    public abstract void fillFields(List<String> params);

    public static ServerMessage build(MessageType type) {
        switch (type) {
            case SEE_MESSAGE:
                return new SeeObjects();
            case SERVER_PARAMS:
                return new ServerParameters();
            case PLAYER_TYPE:
                return new PlayerType();
        }
        throw new IllegalArgumentException("Can't find entity for MessageType : " + type);
    }
}
