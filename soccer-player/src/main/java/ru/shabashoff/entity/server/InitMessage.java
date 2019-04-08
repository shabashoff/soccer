package ru.shabashoff.entity.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InitMessage extends ServerMessage {
    Side side;
    int uniformNumber;
    String playMode;

    public InitMessage() {
        super(MessageType.INIT_MESSAGE);
    }

    @Override
    public void fillFields(List<String> params) {
        if (params.get(0).trim().equals("l")) side = Side.LEFT;
        else side = Side.RIGHT;

        uniformNumber = Integer.parseInt(params.get(1).trim());
        playMode = params.get(2);
    }

    enum Side {
        LEFT, RIGHT
    }
}
