package ru.shabashoff.start;

import lombok.extern.log4j.Log4j;
import ru.shabashoff.server.RCSSServerClient;

@Log4j
public class Launch {

    public static void main(String[] args) {
        log.info("Started app");
        RCSSServerClient client = new RCSSServerClient();


    }
}
