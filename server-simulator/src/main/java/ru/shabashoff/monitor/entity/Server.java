package ru.shabashoff.monitor.entity;

import lombok.extern.log4j.Log4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j
@SuppressWarnings("ALL")
public class Server {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final DatagramSocket server;

    private final Map<String, Client> clients = Collections.synchronizedMap(new HashMap<>());

    public Server(int port) throws SocketException {
        server = new DatagramSocket(port);

        restartServerListener();
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
                    //log.info("Message from client: " + response);

                    onNewMessageFromClient(bytes, packet.getAddress(), packet.getPort());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                restartServerListener();
            }
        });
    }

    private void onNewMessageFromClient(byte[] bytes, InetAddress addr, int port) {
        String key = addr.toString() + port;

        String msg = new String(bytes);

        if (clients.containsKey(key)) clients.get(key).addClientMessage(bytes);
        else {
            Client cl = new Client(addr, port);
            cl.addClientMessage(bytes);

            clients.put(key, cl);
        }
    }
}
