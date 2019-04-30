package ru.shabashoff;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;

@SuppressWarnings("ALL")
public class LogToTrain {

    @Test
    public void test() {

        Set<TrainAction> acts = getTrainSample("../ts.rcl").acts;

        System.out.println(acts);

        HashMap<String, List<List<BigDecimal>>> st = new HashMap<>();

        for (TrainAction act : acts) {
            if (st.containsKey(act.action)) {
                st.get(act.action).add(act.params);
            } else {
                ArrayList<List<BigDecimal>> lst = new ArrayList<>();
                lst.add(act.params);
                st.put(act.action, lst);
            }
        }

        System.out.println(st);
    }


    @SneakyThrows
    private TrainSample getTrainSample(String fileName) {
        HashSet<String> msgs = new HashSet<>(Arrays.asList("kick", "catch", "turn", "move", "dash", "tackle", "turn_neck"));

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

            if (st.equals("Coach")) continue;

            int n = Integer.parseInt(st);

            List<String> prs = getMessages(str.substring(line[0].length() + line[1].length() + line[2].length() + 3).replace("(", " (").trim());

            PlayerTime pt = new PlayerTime(n, l + r, 0);

            for (String pr : prs) {
                List<String> ls = getMessages(pr.substring(1, pr.length() - 1));
                if (msgs.contains(ls.get(0))) {
                    List<BigDecimal> bb = new ArrayList<>();
                    for (int i = 1; i < ls.size(); i++) {
                        if (!isNumeric(ls.get(i))) continue;

                        bb.add(new BigDecimal(ls.get(i)));
                    }

//                    TrainAction ta = convertToNormalValue(new TrainAction(ls.get(0), bb));
                    TrainAction ta = new TrainAction(ls.get(0), bb);

                    if (pars.containsKey(pt)) {
                        pars.get(pt).add(ta);
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
    private Map<PlayerTime, List<BigDecimal>> getTrainVectors(String fileName) {
        FileInputStream fi = new FileInputStream(fileName);

        Scanner sc = new Scanner(fi);

        Map<PlayerTime, List<BigDecimal>> pars = new HashMap<>();

        while (sc.hasNext()) {
            String line = sc.nextLine();
            String type = parseType(line);

            if (type.equals("show")) {
                List<String> messages = getMessages(line.substring(line.indexOf(' ') + 1, line.length() - 1));

                long time = Long.parseLong(messages.get(0));

                List<BigDecimal> bls = getBallParameters(messages.get(1));

                for (int i = 2; i < messages.size(); i++) {
                    List<BigDecimal> p = getParameters(messages.get(i));
                    p.addAll(bls);

                    pars.put(new PlayerTime((i - 2) % 11, time, i > 10 ? 1 : 0), p);
                }
            }
        }

        return pars;
    }


    private List<BigDecimal> getParameters(String msg) {
        List<String> pps = getMessages(msg.substring(1, msg.length() - 1));
        List<BigDecimal> bls = new ArrayList<>();

        bls.add(BigDecimal.valueOf(Double.parseDouble(pps.get(1))));
        bls.add(new BigDecimal(pps.get(2).charAt(pps.get(2).length() - 1)));

        for (int i = 3; i < 9; i++) {
            bls.add(BigDecimal.valueOf(Double.parseDouble(pps.get(i))));
        }

        return bls;
    }


    private List<BigDecimal> getBallParameters(String msg) {
        List<String> pps = getMessages(msg.substring(1, msg.length() - 1));
        List<BigDecimal> bls = new ArrayList<>();

        for (int i = 1; i < pps.size(); i++) {
            bls.add(BigDecimal.valueOf(Double.parseDouble(pps.get(i))));
        }

        return bls;
    }

    private TrainAction convertToNormalValue(TrainAction act) {

        HashMap<String, List<TrainAction>> sm = fillActions();

        List<BigDecimal> params = act.params;

        BigDecimal delta;
        BigDecimal minDelta = BigDecimal.valueOf(Long.MAX_VALUE);
        int n = -1;

        List<TrainAction> smq = sm.get(act.action);
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
            mp.put("turn", Arrays.asList(new TrainAction("turn", Arrays.asList(bd(cur)))));
            mp.put("turn_neck", Arrays.asList(new TrainAction("turn_neck", Arrays.asList(bd(cur)))));
            cur += delta;
        }

        mp.put("turn", Arrays.asList(new TrainAction("turn", Arrays.asList(bd(34.3662f)))));

        return mp;
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
            if (chars[i] == '(') count++;
            else if (chars[i] == ')') count--;

            if ((chars[i] == ' ') && count == 0) {
                list.add(new String(chars, start, i - start));
                start = i + 1;
            }
        }
        list.add(new String(chars, start, message.length() - start));
        return list;
    }


    private String parseType(String message) {
        if (message.indexOf('(') == -1 || message.indexOf(' ') == -1) return message;

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
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
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
        private final String action;
        private final List<BigDecimal> params;

        TrainAction(String action, List<BigDecimal> params) {
            this.action = action;
            this.params = params;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TrainAction that = (TrainAction) o;
            return action.equals(that.action)
                    && params.equals(that.params);
        }

        @Override
        public int hashCode() {
            return Objects.hash(action, params);//
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
