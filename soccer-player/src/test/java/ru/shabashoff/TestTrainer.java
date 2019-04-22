package ru.shabashoff;

import org.junit.Test;
import ru.shabashoff.entity.Player;
import ru.shabashoff.entity.Trainer;
import ru.shabashoff.entity.UdpServerClient;

public class TestTrainer {
    @Test
    public void test() throws InterruptedException {
        TestCoach trainer = new TestCoach();

        Thread.sleep(1_000_000);
    }


    static class TestCoach extends UdpServerClient {


        public TestCoach() {
            super("localhost", 6001);

            sendMessage("(init (version 7))");
            sendMessage("(eye on)");
        }

        @Override
        protected void onServerMessage(String message) {

        }
    }


    static class UtcMessage extends UdpServerClient {

        public UtcMessage(int port) {
            super("localhost", port);

        }

        @Override
        protected void onServerMessage(String message) {
        }

        @Override
        public void startServerListener() {
        }
    }


}
