package ru.shabashoff.monitor.entity;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
@Log4j
@SuppressWarnings("ALL")
public class Client {
    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 6000;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final List<String> messageFromClientQueue = Collections.synchronizedList(new ArrayList<>());
    private final List<String> messageFromServerQueue = Collections.synchronizedList(new ArrayList<>());
    private final DatagramSocket socket;
    private final InetAddress serverAddress;
    private final InetAddress clientAddress;
    private final int clientPort;


    @SneakyThrows
    public Client(InetAddress clientAddress, int clientPort) {

        this.clientAddress = clientAddress;
        this.clientPort = clientPort;

        socket = new DatagramSocket();
        socket.setReuseAddress(true);

        serverAddress = InetAddress.getByName(SERVER_HOST);

        restartServerListener();
    }

    public void addClientMessage(byte[] bytes) {
        String msg = new String(bytes);

        log.info("Add client message:" + msg);
        messageFromClientQueue.add(msg);

        sendClientMessage(bytes);
    }

    private void addServerMessage(byte[] bytes) {
        String s = new String(bytes);
        log.info("Add monitor message:" + s);
        messageFromServerQueue.add(s);

        sendServerMessage(bytes);
    }

    @SneakyThrows
    public void sendClientMessage(byte[] bytes) {
        DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, serverAddress, SERVER_PORT);

        socket.send(sendPacket);
    }

    @SneakyThrows
    private void sendServerMessage(byte[] bytes) {
        DatagramPacket sendPacket = new DatagramPacket(bytes, bytes.length, clientAddress, clientPort);

        socket.send(sendPacket);
    }

    @SneakyThrows
    private void restartServerListener() {
        executor.submit(() -> {
            try {
                while (true) {
                    byte[] bytes = new byte[8192];
                    DatagramPacket getPacket = new DatagramPacket(bytes, bytes.length);

                    socket.receive(getPacket);

                    addServerMessage(getPacket.getData());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                restartServerListener();
            }
        });
    }
}
