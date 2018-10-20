package ru.shabashoff.start;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.server.Team;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Launch {

    String TEAM_NAME = "test";

    public static void main(String[] args) {
        log.info("Started app");

        Team team = new Team();

        team.simpleStrategy();
    }
}
