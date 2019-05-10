package ru.shabashoff.start;

import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.Team;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Launch {

    String TEAM_NAME = "test";

    @SneakyThrows
    public static void main(String[] args) {
        log.info("Started app");

        Team team = new Team();

        Thread.sleep(1_000_000);
    }
}
