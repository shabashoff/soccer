package ru.shabashoff.entity.server;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SeeGlobal extends ServerMessage {
    Long time;
    List<Info>  info;

    public SeeGlobal() {
        super(MessageType.SEE_GLOBAL);
    }

    @Override
    public void fillFields(List<String> params) {
        System.out.println(params);

        time = Long.valueOf(params.get(0));


    }
}

class Info {
    String name;
    List<BigDecimal> list;
}