package ru.shabashoff.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j;
import ru.shabashoff.entity.server.*;
import ru.shabashoff.parser.MsgParser;

@Log4j
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class RCSSServerClient extends UdpServerClient {

    MsgParser parser = new MsgParser();

    @SneakyThrows
    public RCSSServerClient(String teamName) {
        super("localhost", 6000);

        initTeam(teamName);

        startServerListener();
    }


    private void initTeam(String teamName) {
        sendMessage("(init " + teamName + " (version 7))");
    }


    private void onServerMessage(ServerMessage message) {
        switch (message.getType()) {
            case SEE_MESSAGE:
                onSeeMessage((SeeMessage) message);
                break;
            case SENSE_BODY:
                onSenseBodyMessage((SenseBody) message);
                break;
            case HEAR:
                onHearMessage((HearMessage) message);
                break;
            case ERROR:
                onErrorMessage((ErrorMessage) message);
                break;
            default:
                log.warn(message);
        }
    }

    @Override
    public void onServerMessage(String message) {
        onServerMessage(parser.parseMessage(message));
    }

    protected abstract void onErrorMessage(ErrorMessage message);

    protected abstract void onHearMessage(HearMessage message);

    protected abstract void onSenseBodyMessage(SenseBody message);

    protected abstract void onSeeMessage(SeeMessage message);
}
