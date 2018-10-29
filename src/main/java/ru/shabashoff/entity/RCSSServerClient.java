package ru.shabashoff.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.server.*;
import ru.shabashoff.parser.MsgParser;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class RCSSServerClient {
    String SERVER_HOST = "localhost";
    int SERVER_PORT = 6000;

    ExecutorService serverListener = Executors.newSingleThreadExecutor();

    DatagramSocket socket;
    InetAddress serverAddress;

    MsgParser parser = new MsgParser();


    @SneakyThrows
    public RCSSServerClient(String teamName) {
        socket = new DatagramSocket();
        socket.setReuseAddress(true);

        serverAddress = InetAddress.getByName(SERVER_HOST);

        log.info(socket.getLocalSocketAddress().toString());

        initTeam(teamName);

        restartServerListener();
    }

    @SneakyThrows
    public void sendMessage(String msg) {
        byte[] bytes = msg.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, serverAddress, SERVER_PORT);

        log.info("Send to the server: " + msg);

        socket.send(sendPacket);
    }

    private void initTeam(String teamName) {
        sendMessage("(init " + teamName + " (version 7))");
    }

    @SneakyThrows
    private void restartServerListener() {
        serverListener.submit(() -> {
            try {
                while (true) {
                    byte[] bytes = new byte[2048];
                    DatagramPacket getPacket = new DatagramPacket(bytes, bytes.length);

                    socket.receive(getPacket);

                    String response = new String(getPacket.getData(), StandardCharsets.UTF_8).trim();

                    log.info("Message from server: " + response);

                    onServerMessage(parser.parseMessage(response));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                restartServerListener();
            }
        });
    }

    private void onServerMessage(ServerMessage message) {
        switch (message.getType()) {
            case SEE_MESSAGE:
                onSeeMessage((SeeMessage) message);
                break;
            case SENSE_BODY:
                onSenseBodyMessage((SenseBody) message);
                break;
            case HEAR:
                onHearMessage((HearMessage) message);
                break;
            case ERROR:
                onErrorMessage((ErrorMessage) message);
                break;
            default:
                log.warn(message);
        }
    }

    protected abstract void onErrorMessage(ErrorMessage message);

    protected abstract void onHearMessage(HearMessage message);

    protected abstract void onSenseBodyMessage(SenseBody message);

    protected abstract void onSeeMessage(SeeMessage message);
}
