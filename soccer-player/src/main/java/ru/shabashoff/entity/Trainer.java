package ru.shabashoff.entity;

import lombok.extern.log4j.Log4j;

@Log4j
public class Trainer extends UdpServerClient {
    public Trainer(String teamName) {
        super("localhost", 6001);
        sendMessage("(init (version 7))");
        sendMessage("(look)");
    }

    @Override
    protected void onServerMessage(String message) {
        //log.info("Server");
    }
}
