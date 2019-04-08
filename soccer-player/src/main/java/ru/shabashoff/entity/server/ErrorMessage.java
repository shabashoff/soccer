package ru.shabashoff.entity.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorMessage extends ServerMessage {

    String message;

    public ErrorMessage() {
        super(MessageType.ERROR);
    }

    @Override
    public void fillFields(List<String> params) {
        message = params.get(0);
    }
}
