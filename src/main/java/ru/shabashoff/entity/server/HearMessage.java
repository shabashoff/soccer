package ru.shabashoff.entity.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HearMessage extends ServerMessage {

    long time;

    String name;
    String message;

    public HearMessage() {
        super(MessageType.HEAR);
    }

    @Override
    public void fillFields(List<String> params) {
        time = Long.parseLong(params.get(0));
        name = params.get(1);
        message = params.get(2);
    }
}
