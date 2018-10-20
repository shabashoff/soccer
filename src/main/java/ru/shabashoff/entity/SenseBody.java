package ru.shabashoff.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SenseBody extends ServerMessage {
    ViewWidth viewWidth;
    ViewQuality viewQuality;

    final Map<String, Integer> map = new HashMap<>();

    public SenseBody() {
        super(MessageType.SENSE_BODY);
    }

    @Override
    public void fillFields(List<String> params) {
        //TODO
    }

    enum ViewWidth {
        NARROW, NORMAL, WIDE
    }

    enum ViewQuality {
        HIGH, LOW
    }
}
