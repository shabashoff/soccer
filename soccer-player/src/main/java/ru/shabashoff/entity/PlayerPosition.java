package ru.shabashoff.entity;

public enum PlayerPosition {
    GOALKEEPER(1, 1), BACK(2, 9), HALFBACK(3, 9), FORWARD(4, 9);

    int serverPos;
    int internalPos;

    PlayerPosition(int internalPos, int serverPos) {
        this.serverPos = serverPos;
        this.internalPos = internalPos;
    }
}
