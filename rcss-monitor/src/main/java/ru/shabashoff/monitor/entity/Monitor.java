package ru.shabashoff.monitor.entity;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
@SuppressWarnings("ALL")
public class Monitor {
    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 6000;
    private final InetAddress SERVER_ADDRES;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final DatagramSocket server;

    @SneakyThrows
    public Monitor() {
        server = new DatagramSocket();
        SERVER_ADDRES = InetAddress.getByName(SERVER_HOST);

        restartServerListener();

        sendMessage("(dispinit version 1)");
    }

    private void restartServerListener() {
        executor.execute(() -> {
            try {
                while (true) {
                    byte[] bytes = new byte[8192];
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
                    InetAddress address = packet.getAddress();

                    server.receive(packet);

                    String response = new String(packet.getData(), StandardCharsets.UTF_8).trim();
                    log.info("Message from client: " + response);

                    onNewMessageFromClient(bytes, packet.getAddress(), packet.getPort());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                restartServerListener();
            }
        });
    }

    @SneakyThrows
    private void sendMessage(String msg){
        byte[] bytes = msg.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, SERVER_ADDRES, SERVER_PORT);

        log.info("Send to the server: " + msg);

        server.send(sendPacket);
    }

    private void onNewMessageFromClient(byte[] bytes, InetAddress addr, int port) {
        String key = addr.toString() + port;

    }
}
