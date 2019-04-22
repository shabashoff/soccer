package ru.shabashoff.monitor;

import lombok.SneakyThrows;
import ru.shabashoff.monitor.entity.Server;

public class Launch {
    @SneakyThrows
    public static void main(String[] args) {
        Server server = new Server(5999);

        Thread.sleep(1_000_000);
    }
}
