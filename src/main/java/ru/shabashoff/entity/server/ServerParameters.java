package ru.shabashoff.entity.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ServerParameters extends ServerMessage {
    List<Double> parameters = new ArrayList<>();

    public ServerParameters() {
        super(MessageType.SERVER_PARAMS);
    }

    @Override
    public void fillFields(List<String> params) {
        params.forEach(p -> parameters.add(Double.valueOf(p)));
    }
}
