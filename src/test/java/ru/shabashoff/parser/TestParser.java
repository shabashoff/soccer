package ru.shabashoff.parser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import org.junit.Assert;
import org.junit.Test;
import ru.shabashoff.entity.server.*;

import java.util.List;

@Log4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TestParser {
    String TEST_SEE_MESSAGE = "(see 0 ((f t l 10) 9.7 24) ((f r t) 68 -26) ((f r b) 72.2 32) ((f g r b) 62.2 10) ((g r) 61.6 4) ((f g r t) 61.6 -3) ((f p r b) 50.9 28) ((f p r c) 45.2 5) ((f p r t) 47.5 -20) ((f t r 30) 52.5 -42) ((f t r 40) 60.3 -36) ((f t r 50) 68.7 -31) ((f b r 40) 65.4 41) ((f b r 50) 73 36) ((f r 0) 66.7 3) ((f r t 10) 66.7 -5) ((f r t 20) 68 -14) ((f r t 30) 71.5 -21) ((f r b 10) 68 12) ((f r b 20) 70.8 20) ((f r b 30) 74.4 27) ((b) 10 24 0 0) ((l r) 61.6 90))";
    String TEST_SERVER_PARAM = "(server_param 14.02 5 0.3 0.4 0.1 60 1.05 1 8000 45 1 0.3 0.5 0.002 1 0.3 0.6 0.005 0.6 0.01 0.1 0 1 1 1 1 0.085 0.94 0.05 0.2 3 2.7 0.006 0.027 0.7 2 1.7 100 -100 180 -180 180 -180 90 -90 90 3 0 0 0 0 1.085 1.2 1 1 2 1 2.5 0 0 128 128 300 1 1 1 1 50 1 3000 100 150 10 100 300 10 1 1 1 5 1 1 1 9.15 50 0.1 0.01 -1 -1 -1 -1 -1 -1 -1 0 0 0 100 0 0 0 0 200)";
    String TEST_PLAYER_TYPE = "(player_type 5 1.05 43.4514 0.335263 3.38157 0.0062581 0.3 0.73225 0.13225 84.9154 0.860339 0.460339)";

    @Test
    public void testParserSeeMessage() {
        MsgParser parser = new MsgParser();
        SeeMessage seeMessage = (SeeMessage) parser.parseMessage(TEST_SEE_MESSAGE);

        log.info(seeMessage.getGameObjects());

        Assert.assertEquals(seeMessage.getType(), MessageType.SEE_MESSAGE);

        Assert.assertEquals(seeMessage.getTime(), 0L);

        GameObject gameObject = seeMessage.getGameObjects().get(0);

        List<Float> numbers = gameObject.getNumbers();

        Assert.assertEquals("ftl10", gameObject.getName());

        Assert.assertEquals(9.7, numbers.get(0), 0.000001);
        Assert.assertEquals(24., numbers.get(1), 0.000001);
    }

    @Test
    public void testParserServerParams() {
        MsgParser parser = new MsgParser();
        ServerParameters serverParameters = (ServerParameters) parser.parseMessage(TEST_SERVER_PARAM);

        Assert.assertEquals(serverParameters.getType(), MessageType.SERVER_PARAMS);

        Assert.assertEquals(serverParameters.getParameters().get(0), 14.02, 0.000001);
        Assert.assertEquals(serverParameters.getParameters().get(1), 5, 0.000001);
        Assert.assertEquals(serverParameters.getParameters().get(2), 0.3, 0.000001);
        Assert.assertEquals(serverParameters.getParameters().get(3), 0.4, 0.000001);
        Assert.assertEquals(serverParameters.getParameters().get(4), 0.1, 0.000001);
        Assert.assertEquals(serverParameters.getParameters().get(5), 60, 0.000001);
    }

    @Test
    public void testPlayerType() {
        MsgParser parser = new MsgParser();
        PlayerType playerType = (PlayerType) parser.parseMessage(TEST_PLAYER_TYPE);

        Assert.assertEquals(playerType.getType(), MessageType.PLAYER_TYPE);

        Assert.assertEquals(playerType.getParameters().get(0), 5, 0.000001);
        Assert.assertEquals(playerType.getParameters().get(1), 1.05, 0.000001);
        Assert.assertEquals(playerType.getParameters().get(2), 43.4514, 0.000001);
        Assert.assertEquals(playerType.getParameters().get(3), 0.335263, 0.000001);
        Assert.assertEquals(playerType.getParameters().get(4), 3.38157, 0.000001);
        Assert.assertEquals(playerType.getParameters().get(5), 0.0062581, 0.000001);
    }


}
