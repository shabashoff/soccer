package ru.shabashoff.entity;


import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.server.MessageType;
import ru.shabashoff.entity.server.SeeGlobal;
import ru.shabashoff.entity.server.ServerMessage;
import ru.shabashoff.parser.MsgParser;

@Log4j
public class Coach extends UdpServerClient {

    private final String teamName;

    private final List<Player> players = new ArrayList<>();


    public Coach(String teamName) {
        super("localhost", 6002);

        this.teamName = teamName;
    }

    @Override
    protected void onServerMessage(String message) {
        log.info("Coach has message:" + message);
        ServerMessage m = MsgParser.parseMessage(message);
        if (m.getType() == MessageType.SEE_GLOBAL) {
            SeeGlobal gl = (SeeGlobal) m;
            for (Player player : players) {
                SeeGlobal.Info info = gl.getPlayers().get(player.getId());
                player.calcInternalParams(info.getAngle(), info.getPoint(), gl.getBallPoint());
                player.action();
            }
        }
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void initCoach() {
        sendMessage("(init " + teamName + "(version 7))");
        sendMessage("(eye on)");
    }

    public void initPlayer(Player player) {
        changePlayerType(player.getId(), player.getPlayerPosition().serverPos);
    }

    private void changePlayerType(int playerNum, int playerPosition) {
        sendMessage("(change_player_type " + playerNum + " " + playerPosition + ")");
    }
}
