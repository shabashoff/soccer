package ru.shabashoff.monitor;

import lombok.SneakyThrows;
import ru.shabashoff.monitor.entity.Monitor;
import ru.shabashoff.monitor.entity.Server;

public class Launch {
    @SneakyThrows
    public static void main(String[] args) {
        Monitor mnt = new Monitor();

        Thread.sleep(1_000_000);
    }
}
