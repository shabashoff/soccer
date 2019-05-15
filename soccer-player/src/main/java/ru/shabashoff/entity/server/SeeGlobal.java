package ru.shabashoff.entity.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.shabashoff.entity.Point;
import ru.shabashoff.utils.GameUtils;

@Data
public class SeeGlobal extends ServerMessage {
    Long time;
    Point ballPoint;
    List<Info> info = new ArrayList<>();
    Map<Integer, Info> players = new HashMap<>();


    SeeGlobal() {
        super(MessageType.SEE_GLOBAL);
    }

    @Override
    public void fillFields(List<String> params) {
        System.out.println(params);

        time = Long.valueOf(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            String sq = params.get(i);
            List<String> ss = GameUtils.parseMessages(sq.substring(1, sq.length() - 1));
            String nm = ss.get(0);

            List<String> obj = GameUtils.parseMessages(nm.substring(1, nm.length() - 1));
            switch (obj.get(0)) {
                case "b":
                    ballPoint = new Point(Double.parseDouble(ss.get(1)), Double.parseDouble(ss.get(2)));
                    break;
                case "p":
                    if (obj.get(1).equals("\"team-1\"")) {
                        players.put(
                            Integer.valueOf(obj.get(2)),
                            new Info(
                                new Point(Double.parseDouble(ss.get(1)), Double.parseDouble(ss.get(2))),
                                new BigDecimal(ss.get(3)
                                )
                            )
                        );
                    }
                    break;
            }
            System.out.println(ss);
        }

    }


    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) public
    static class Info {
        Point point;
        BigDecimal angle;

        Info(Point point, BigDecimal angle) {
            this.point = point;
            this.angle = angle;
        }
    }
}
