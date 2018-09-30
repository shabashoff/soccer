package ru.shabashoff.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.junit.Test;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TestParser {
    String TEST_SEE = "(see 0 ((f c) 9.7 24 0 0) ((f r t) 68 -26) ((f r b) 72.2 32) ((f g r b) 62.2 10) ((g r) 61.6 4) ((f g r t) 61.6 -3) ((f p r b) 50.9 28) ((f p r c) 45.2 5) ((f p r t) 47.5 -20) ((f t r 30) 52.5 -42) ((f t r 40) 60.3 -36) ((f t r 50) 68.7 -31) ((f b r 40) 65.4 41) ((f b r 50) 73 36) ((f r 0) 66.7 3) ((f r t 10) 66.7 -5) ((f r t 20) 68 -14) ((f r t 30) 71.5 -21) ((f r b 10) 68 12) ((f r b 20) 70.8 20) ((f r b 30) 74.4 27) ((b) 10 24 0 0) ((l r) 61.6 90))";


    @Test
    public void testSeeParse() {
        MsgParser parser = new MsgParser();
        parser.parseSeeMessage(TEST_SEE);
    }

}
