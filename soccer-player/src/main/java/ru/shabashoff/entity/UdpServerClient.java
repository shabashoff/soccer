package ru.shabashoff.entity;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class UdpServerClient {

    private final int serverPort;

    ExecutorService serverListener = Executors.newSingleThreadExecutor();

    DatagramSocket socket;
    InetAddress serverAddress;


    @SneakyThrows
    public UdpServerClient(String host, int port) {
        this.serverPort = port;

        socket = new DatagramSocket();
        socket.setReuseAddress(true);

        serverAddress = InetAddress.getByName(host);

        log.info(socket.getLocalSocketAddress().toString());

        startServerListener();
    }

    @SneakyThrows
    public void sendMessage(String msg) {
        byte[] bytes = msg.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, serverAddress, serverPort);

        log.info("Send message: " + msg);

        socket.send(sendPacket);
    }

    @SneakyThrows
    public void startServerListener() {
        serverListener.submit(() -> {
            try {
                while (true) {
                    byte[] bytes = new byte[2048];
                    DatagramPacket getPacket = new DatagramPacket(bytes, bytes.length);

                    socket.receive(getPacket);

                    String response = new String(getPacket.getData(), StandardCharsets.UTF_8).trim();

                    log.info("Message from monitor: " + response);

                    onServerMessage(response);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                startServerListener();
            }
        });
    }



    protected abstract void onServerMessage(String message);
}
