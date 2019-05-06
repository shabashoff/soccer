package ru.shabashoff.entity;


import lombok.extern.log4j.Log4j;

@Log4j
public class Coach extends UdpServerClient {

    public Coach(String teamName) {
        super("localhost", 6002);

        sendMessage("(init " + teamName + "(version 7))");
        sendMessage("(eye on)");
    }

    @Override
    protected void onServerMessage(String message) {
        log.info("Coach has message:" + message);
    }

    public void initPlayer(Player player) {
        changePlayerType(player.getId(), player.getPlayerPosition().serverPos);
    }

    private void changePlayerType(int playerNum, int playerPosition) {
        sendMessage("(change_player_type " + playerNum + " " + playerPosition + ")");
    }
}
