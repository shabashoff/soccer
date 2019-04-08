package ru.shabashoff.entity.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class  SenseBody extends ServerMessage {
    long time;
    ViewWidth viewWidth;
    ViewQuality viewQuality;

    double stamina;
    double staminaEffort;

    double speed;
    double speedAngle;

    double headAngle;

    int kickCount;
    int dashCount;
    int turnCount;
    int sayCount;
    int turnNeckCount;
    int catchCount;
    int moveCount;
    int changeViewCount;

    final Map<String, Integer> map = new HashMap<>();

    public SenseBody() {
        super(MessageType.SENSE_BODY);
    }

    @Override
    public void fillFields(List<String> params) {
        time = Long.parseLong(params.get(0));
        String[] split;

        split = getSplitAndAssert(3, "view_mode", params.get(1));

        viewQuality = ViewQuality.parse(split[1]);
        viewWidth = ViewWidth.parse(split[2]);

        split = getSplitAndAssert(3, "stamina", params.get(2));

        stamina = Double.parseDouble(split[1]);
        staminaEffort = Double.parseDouble(split[2]);

        split = getSplitAndAssert(3, "speed", params.get(3));

        speed = Double.parseDouble(split[1]);
        speedAngle = Double.parseDouble(split[2]);

        split = getSplitAndAssert(2, "head_angle", params.get(4));

        headAngle = Double.parseDouble(split[1]);

        split = getSplitAndAssert(2, "kick", params.get(5));

        kickCount = Integer.parseInt(split[1]);

        split = getSplitAndAssert(2, "dash", params.get(6));

        dashCount = Integer.parseInt(split[1]);

        split = getSplitAndAssert(2, "turn", params.get(7));

        turnCount = Integer.parseInt(split[1]);

        split = getSplitAndAssert(2, "say", params.get(8));

        sayCount = Integer.parseInt(split[1]);

        split = getSplitAndAssert(2, "turn_neck", params.get(9));

        turnNeckCount = Integer.parseInt(split[1]);

        split = getSplitAndAssert(2, "catch", params.get(10));

        catchCount = Integer.parseInt(split[1]);

        split = getSplitAndAssert(2, "move", params.get(11));

        moveCount = Integer.parseInt(split[1]);

        split = getSplitAndAssert(2, "change_view", params.get(12));

        changeViewCount = Integer.parseInt(split[1]);
    }

    private String[] getSplitAndAssert(int n, String name, String toSplit) {
        String[] arr = toSplit.substring(1, toSplit.length() - 1).split(" ");

        assert arr.length == n;
        assert arr[0].equals(name);

        return arr;
    }

    enum ViewWidth {
        NARROW, NORMAL, WIDE;

        public static ViewWidth parse(String s) {
            switch (s) {
                case "narrow":
                    return NARROW;
                case "normal":
                    return NORMAL;
                case "wide":
                    return WIDE;
            }
            throw new IllegalArgumentException("Can't parse " + s);
        }
    }

    enum ViewQuality {
        HIGH, LOW;

        public static ViewQuality parse(String s) {
            switch (s) {
                case "high":
                    return HIGH;
                case "low":
                    return LOW;
            }
            throw new IllegalArgumentException("Can't parse " + s);
        }
    }
}
