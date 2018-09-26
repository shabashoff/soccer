package ru.shabashoff.server;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.Message;

import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RCSSServerClient {
    int COUNT_THREADS = 4;
    String SERVER_HOST = "localhost";
    int SERVER_PORT = 6000;
    ExecutorService executor;
    DatagramSocket socket;

    public RCSSServerClient() {
        executor = Executors.newFixedThreadPool(COUNT_THREADS);

        try {
            socket = new DatagramSocket();
            socket.setReuseAddress(true);
            InetAddress addr = InetAddress.getLocalHost();

            byte[] sendData = "init test (version 7)".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, 6000);

            log.info(new String(sendData));

            socket.receive(sendPacket);

            String modifiedSentence = new String(sendPacket.getData());

            log.info("FROM SERVER:" + modifiedSentence);

        } catch (IOException e) {
            throw new IllegalComponentStateException("Cant connect to the server " + SERVER_HOST + ":" + SERVER_PORT + "\n Error:" + e.getMessage());
        }
    }


    public Future<?> sendMessage(Message msg) {
        SenderMsg sendMsg = new SenderMsg(msg);
        return executor.submit(sendMsg);
    }

    @FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
    private class SenderMsg implements Runnable {

        Message msg;

        private SenderMsg(Message msg) {
            this.msg = msg;
        }

        public void run() {

        }
    }
}
