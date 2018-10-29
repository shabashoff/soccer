package ru.shabashoff.entity.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;


@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PlayerType extends ServerMessage{
    List<Double> parameters = new ArrayList<>();

    public PlayerType() {
        super(MessageType.PLAYER_TYPE);
    }

    @Override
    public void fillFields(List<String> params) {
        params.forEach(p -> parameters.add(Double.valueOf(p)));
    }
}
