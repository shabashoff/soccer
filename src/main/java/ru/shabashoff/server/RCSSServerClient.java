package ru.shabashoff.server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Log4j
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RCSSServerClient {
    String SERVER_HOST = "localhost";
    int SERVER_PORT = 6000;

    DatagramSocket socket;
    InetAddress serverAdress;

    @SneakyThrows
    public RCSSServerClient(String teamName) {

        socket = new DatagramSocket();
        socket.setReuseAddress(true);

        serverAdress = InetAddress.getByName(SERVER_HOST);

        log.info(socket.getLocalSocketAddress().toString());

        initTeam(teamName);
    }

    @SneakyThrows
    public String sendMessage(String msg) {
        byte[] bytes = msg.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, serverAdress, SERVER_PORT);

        log.info("Send to the server: " + msg);

        socket.send(sendPacket);

        bytes = new byte[2048];
        DatagramPacket getPacket = new DatagramPacket(bytes, bytes.length);

        socket.receive(getPacket);

        String response = new String(bytes);
        log.info("Response from the server: " + response);

        return response;
    }

    private void initTeam(String teamName) {
        sendMessage("(init " + teamName + " (version 7))");
    }
}
