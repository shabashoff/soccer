package ru.shabashoff.server;

import lombok.SneakyThrows;
import ru.shabashoff.server.entity.Server;

public class Launch {
    @SneakyThrows
    public static void main(String[] args) {
        Server server = new Server(6000);

        Thread.sleep(1_000_000);
    }
}
