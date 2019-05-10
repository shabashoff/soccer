package ru.shabashoff;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.Test;
import ru.shabashoff.decision.C45;
import ru.shabashoff.decision.DecisionTree;
import ru.shabashoff.entity.Player;
import ru.shabashoff.entity.Point;
import ru.shabashoff.entity.Team;
import ru.shabashoff.entity.server.PlayMode;

@SuppressWarnings("ALL")
public class LogToTrain {

    @Test
    public void tets2() {
        Map<PlayerTime, List<BigDecimal>> tv = getTrainVectors("../ts.rcg", new HashMap<>());
        TrainSample trainSample = getTrainSample("../ts.rcl");
        Map<PlayerTime, List<TrainAction>> ts = trainSample.pars;

        BigDecimal[][] vect = new BigDecimal[ts.size()][];
        Integer[] clss = new Integer[ts.size()];
        int i = 0;

        HashMap<String, Integer> st = new HashMap<>();

        for (Map.Entry<PlayerTime, List<TrainAction>> pt : ts.entrySet()) {
            PlayerTime key = pt.getKey();
            List<BigDecimal> bd = tv.get(key);
            if (bd != null) {
                System.out.println("player time is null:" + key);
            } else {
                continue;

            }
            vect[i] = new BigDecimal[bd.size()];
            bd.toArray(vect[i]);

            i++;
        }

        i = 0;
        int n = 0;
        HashMap<String, Integer> ss = new HashMap<>();

        for (TrainAction act : trainSample.acts) {
            if (ss.containsKey(act.action)) {
                clss[i] = ss.get(act.action);
            } else {
                ss.put(act.action, n);
                clss[i] = n;
                n++;
            }

            i++;
        }

        DecisionTree dt = (new C45()).trainModel(vect, clss);
        System.out.println(dt);
    }


    @Test
    public void test() {

        Map<PlayerTime, List<BigDecimal>> tv = getTrainVectors("../ts.rcg", new HashMap<>());


        TrainSample ts = getTrainSample("../ts.rcl");
        Set<TrainAction> acts = ts.acts;
        Map<PlayerTime, List<TrainAction>> pars = ts.pars;
        System.out.println(acts);

        HashMap<String, List<List<BigDecimal>>> st = new HashMap<>();

        BigDecimal[][] vect = new BigDecimal[acts.size()][];
        Integer[] clss = new Integer[acts.size()];

        int i = 0;

        for (TrainAction act : acts) {
            vect[i] = new BigDecimal[act.params.size()];
            act.params.toArray(vect[i]);
            i++;
        }

        i = 0;
        int n = 0;
        HashMap<String, Integer> ss = new HashMap<>();

        for (TrainAction act : acts) {
            if (ss.containsKey(act.action)) {
                clss[i] = ss.get(act.action);
            } else {
                ss.put(act.action, n);
                clss[i] = n;
                n++;
            }

            i++;
        }



        /*for (TrainAction act : acts) {
            if (st.containsKey(act.action)) {
                st.get(act.action).add(act.params);
            } else {
                ArrayList<List<BigDecimal>> lst = new ArrayList<>();
                lst.add(act.params);
                st.put(act.action, lst);
            }
        }*/

        System.out.println(st);

        DecisionTree dt = (new C45<Integer>()).trainModel(vect, clss);
        dt.saveToFile("testTree.dt");
//        System.out.println(dt);
    }

    @Test
    public void test10() {
        DecisionTree dt = calcSamples("../ts");
        dt.saveToFile("testSample.dt");
    }

    private DecisionTree calcSamples(String prefFile) {
        Team team = new Team();
        Map<Long, Player> map = new HashMap<>();

        for (Player player : team.getPlayers()) {
            map.put((long) player.getId(), player);
        }

        Map<PlayerTime, List<BigDecimal>> tv = getTrainVectors(prefFile + ".rcg", map);
        TrainSample trainSample = getTrainSample(prefFile + ".rcl");

        findDependencies(tv, trainSample);

        List<BigDecimal[]> vect = new ArrayList<>();
        List<String> clss = new ArrayList<>();

        for (Map.Entry<PlayerTime, List<TrainAction>> pt : trainSample.pars.entrySet()) {
            PlayerTime key = pt.getKey();
            List<BigDecimal> bd = tv.get(key);

            if (bd != null) {
                for (TrainAction ta : pt.getValue()) {

                    BigDecimal[] q = new BigDecimal[bd.size()];
                    q = bd.toArray(q);
                    vect.add(q);

                    clss.add(ta.getName());
                }
            }
        }

        BigDecimal[][] qq = new BigDecimal[vect.size()][];
        vect.toArray(qq);

        String[] cc = new String[clss.size()];
        cc = clss.toArray(cc);

        System.out.println(Arrays.toString(cc));

        return (new C45<String>()).trainModel(qq, cc);
    }

    private void findDependencies(Map<PlayerTime, List<BigDecimal>> tv, TrainSample ts) {

        final BigDecimal MAX_DELTA = BigDecimal.valueOf(0.1);

        for (Map.Entry<PlayerTime, List<TrainAction>> entry : ts.pars.entrySet()) {
            List<BigDecimal> bd = tv.get(entry.getKey());

            if (bd == null) {
                continue;
            }

            boolean founded = false;

            for (TrainAction ta : entry.getValue()) {
                for (BigDecimal param : ta.params) {
                    founded = false;

                    for (int i = 0; i < bd.size(); i++) {
                        BigDecimal q = bd.get(i);

                        if (param == null || q == null) {
                            if (param == null || q == null) {
                                ta.action += "_PARAM_" + i;
                                founded = true;
                                break;
                            }
                            continue;
                        }

                        if (param.subtract(q).abs().compareTo(MAX_DELTA) < 1) {
                            ta.action += "_PARAM_" + i;
                            founded = true;
                            break;
                        }
                    }

                    if (!founded) {
                        ta.action += "_" + param;
                    }
                }
            }
        }
        for (Map.Entry<PlayerTime, List<TrainAction>> entry : ts.pars.entrySet()) {
            for (TrainAction ta : entry.getValue()) {
                ta.params.clear();
            }
        }
    }


    @SneakyThrows
    private TrainSample getTrainSample(String fileName) {
        HashSet<String> msgs = new HashSet<>(Arrays.asList(
                "kick",
                "catch",
                "turn",
                "move",
                "dash",
                "tackle",
                "turn_neck"
        ));

        FileInputStream fi = new FileInputStream(fileName);

        Scanner sc = new Scanner(fi);

        Map<PlayerTime, List<TrainAction>> pars = new HashMap<>();
        Set<TrainAction> acts = new HashSet<>();

        while (sc.hasNext()) {
            String str = sc.nextLine();
            String[] line = str.split("[ \t]");

            int l = Integer.parseInt(line[0].substring(0, line[0].indexOf(',')));
            int r = Integer.parseInt(line[0].substring(line[0].indexOf(',') + 1));

            if (line[1].equals("(referee")) {
                //TODO
                continue;
            }

            String teamName = line[2].substring(0, line[2].indexOf('_'));

            String st = line[2].substring(line[2].indexOf('_') + 1, line[2].indexOf(':'));

            if (st.equals("Coach")) {
                continue;
            }

            int n = Integer.parseInt(st);

            List<String> prs = getMessages(str.substring(line[0].length() + line[1].length() + line[2].length() + 3)
                    .replace("(", " (")
                    .trim());

            PlayerTime pt = new PlayerTime(n, l + r, 0);

            for (String pr : prs) {
                List<String> ls = getMessages(pr.substring(1, pr.length() - 1));
                if (msgs.contains(ls.get(0))) {
                    List<BigDecimal> bb = new ArrayList<>();
                    for (int i = 1; i < ls.size(); i++) {
                        if (!isNumeric(ls.get(i))) {
                            continue;
                        }

                        bb.add(new BigDecimal(ls.get(i)));
                    }

                    TrainAction ta = convertToNormalValue(new TrainAction(ls.get(0), bb));
                    //TrainAction ta = new TrainAction(ls.get(0), bb);

                    if (pars.containsKey(pt)) {
                        pars.get(pt).add(ta);
                        //System.out.println(pt + ":" + pars.get(pt));
                    } else {
                        ArrayList<TrainAction> ll = new ArrayList<>();
                        ll.add(ta);
                        pars.put(pt, ll);
                    }

                    acts.add(ta);
                }
            }
        }
        return new TrainSample(pars, acts);
    }

    @SneakyThrows
    private Map<PlayerTime, List<BigDecimal>> getTrainVectors(String fileName, Map<Long, Player> players) {
        FileInputStream fi = new FileInputStream(fileName);

        Scanner sc = new Scanner(fi);

        Map<PlayerTime, List<BigDecimal>> pars = new HashMap<>();

        players.forEach((k, v) -> v.setPlayMode(PlayMode.before_kick_off));


        while (sc.hasNext()) {
            String line = sc.nextLine();
            String type = parseType(line);
            List<String> messages;
            switch (type) {
                case "show":
                    messages = getMessages(line.substring(line.indexOf(' ') + 1, line.length() - 1));

                    long time = Long.parseLong(messages.get(0));

                    List<List<BigDecimal>> p = getParameters("l", messages, players);

                    for (int i = 0; i < p.size(); i++) {
                        List<BigDecimal> ll = p.get(i);
                        pars.put(new PlayerTime(i % 11, time, i > 10 ? 1 : 0), ll);
                    }
                    break;
                case "playmode":
                    messages = getMessages(line.substring(line.indexOf(' ') + 1, line.length() - 1));
                    PlayMode pm = PlayMode.valueOf(messages.get(1));
                    players.forEach((k, v) -> v.setPlayMode(pm));
                    break;
            }


        }

        return pars;
    }


    private List<List<BigDecimal>> getParameters(String side, List<String> msg, Map<Long, Player> players) {
        List<List<BigDecimal>> list = new ArrayList<>();
        Point bp = getBallPoint(msg.get(1));
        Player player;

        for (int i = 2; i < msg.size(); i++) {
            List<BigDecimal> po = getParametersOld(msg.get(i));
            Long id = po.get(0).longValue();
            player = players.get(id);

            Point point = new Point(po.get(3), po.get(4));
            BigDecimal angle = po.get(7);

            if (id == 1) {
                System.out.println(po);
            }

            player.calcInternalParams(angle, point, bp);
            list.add(player.getSnapshotV2(true));
        }

        return list;
    }

    @Deprecated
    private List<BigDecimal> getParametersOld(String msg) {
        List<String> pps = getMessages(msg.substring(1, msg.length() - 1));
        String s = pps.get(0);
        BigDecimal id = new BigDecimal(getMessages(s.substring(1, s.length() - 1)).get(1));

        List<BigDecimal> parameters = new ArrayList<>();

        parameters.add(id);
        parameters.add(BigDecimal.valueOf(Double.parseDouble(pps.get(1))));
        parameters.add(new BigDecimal(pps.get(2).charAt(pps.get(2).length() - 1)));

        for (int i = 3; i < 9; i++) {
            parameters.add(BigDecimal.valueOf(Double.parseDouble(pps.get(i))));
        }

        return parameters;
    }


    private Point getBallPoint(String msg) {
        List<String> pps = getMessages(msg.substring(1, msg.length() - 1));

        Point point = new Point(new BigDecimal(pps.get(1)), new BigDecimal(pps.get(2)));

        return point;
    }

    private TrainAction convertToNormalValue(TrainAction act) {

        HashMap<String, List<TrainAction>> sm = fillActions();

        List<BigDecimal> params = act.params;

        BigDecimal delta;
        BigDecimal minDelta = BigDecimal.valueOf(Long.MAX_VALUE);
        int n = -1;

        List<TrainAction> smq = sm.get(act.action);

        if (smq == null) {
            return act;
        }

        for (int i = 0; i < smq.size(); i++) {
            delta = BigDecimal.ZERO;
            List<BigDecimal> get1 = smq.get(i).params;
            for (int j = 0; j < get1.size(); j++) {
                BigDecimal v = get1.get(j);
                BigDecimal q = params.get(j);
                delta = delta.add(v.subtract(q).abs());
            }

            if (delta.compareTo(minDelta) < 0) {
                minDelta = delta;
                n = i;
            }
        }

        if (n == -1) {
            return act;
        }

        return smq.get(n);
    }

    private HashMap<String, List<TrainAction>> fillActions() {
        HashMap<String, List<TrainAction>> mp = new HashMap<>();

        float cur = -180f;
        float delta = 36f;

        for (int i = 0; i < 10; i++) {
            mp.put("turn", Arrays.asList(new TrainAction("turn", asList(bd(cur)))));
            mp.put("turn_neck", Arrays.asList(new TrainAction("turn_neck", asList(bd(cur)))));
            cur += delta;
        }

        return mp;
    }

    private List<BigDecimal> asList(BigDecimal... dd) {
        ArrayList<BigDecimal> ls = new ArrayList<>();
        for (BigDecimal q : dd) {
            ls.add(q);
        }
        return ls;
    }


    private static BigDecimal bd(float f) {
        return BigDecimal.valueOf(f);
    }

    private static boolean isNumeric(String strNum) {
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return false;
        }
        return true;
    }

    private List<String> getMessages(String message) {
        List<String> list = new ArrayList<>();
        char[] chars = message.toCharArray();
        int start = 0;
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '(') {
                count++;
            } else if (chars[i] == ')') {
                count--;
            }

            if ((chars[i] == ' ') && count == 0) {
                list.add(new String(chars, start, i - start));
                start = i + 1;
            }
        }
        list.add(new String(chars, start, message.length() - start));
        return list;
    }


    private String parseType(String message) {
        if (message.indexOf('(') == -1 || message.indexOf(' ') == -1) {
            return message;
        }

        return message.substring(message.indexOf('(') + 1, message.indexOf(' '));
    }


    static class TrainSample {
        private final Map<PlayerTime, List<TrainAction>> pars;
        private final Set<TrainAction> acts;

        TrainSample(Map<PlayerTime, List<TrainAction>> pars, Set<TrainAction> acts) {
            this.pars = pars;
            this.acts = acts;
        }

        @Override
        public String toString() {
            return "TrainSample{" +
                    "pars=" + pars +
                    ", acts=" + acts +
                    '}';
        }
    }

    static class PlayerTime {
        private final long playerId;
        private final long time;
        private final long side;

        PlayerTime(long playerId, long time, long side) {
            this.playerId = playerId;
            this.time = time;
            this.side = side;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            PlayerTime that = (PlayerTime) o;
            return playerId == that.playerId &&
                    time == that.time &&
                    side == that.side;
        }

        @Override
        public int hashCode() {
            return Objects.hash(playerId, time, side);
        }

        @Override
        public String toString() {
            return "PlayerTime{" +
                    "playerId=" + playerId +
                    ", time=" + time +
                    ", side=" + side +
                    '}';
        }
    }

    static class TrainAction {
        private String action;
        private final List<BigDecimal> params;

        TrainAction(String action, List<BigDecimal> params) {
            this.action = action;
            this.params = params;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TrainAction that = (TrainAction) o;
            return action.equals(that.action)
                    && params.equals(that.params);
        }

        @Override
        public int hashCode() {
            return Objects.hash(action, params);//
        }

        public String getName() {
            StringBuilder sb = new StringBuilder();

            for (BigDecimal p : params) {
                sb.append('_').append(p);
            }

            return action + sb;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Arrays.asList(new TrainAction(\"").append(action).append("\",Arrays.asList(");
            for (BigDecimal p : params) {
                sb.append("bd(").append(p.floatValue()).append("f)),");
            }

            sb.deleteCharAt(sb.length() - 1);
            sb.append("))");

            return "(\"" + action + "\"," + sb + ")";
        }
    }

}
