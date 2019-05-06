package ru.shabashoff.entity.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePlayerType extends ServerMessage {
    int from, to;

    public ChangePlayerType() {
        super(MessageType.CHANGE_PLAYER_TYPE);
    }

    @Override
    public void fillFields(List<String> params) {
        from = Integer.parseInt(params.get(0));
        to = Integer.parseInt(params.get(1));
    }

    @Override
    public String toString() {
        return "ChangePlayerType{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
