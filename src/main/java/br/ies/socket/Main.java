package br.ies.socket;

import br.ies.socket.server.Report;
import br.ies.socket.server.SocketImpl;
import br.ies.socket.singleton.core.SingletonEngine;

import java.util.Timer;

public class Main {
    static Timer timer = new Timer();

    public static void main(String[] args) {
        SingletonEngine.init();

        timer.scheduleAtFixedRate(new Report(), 0, 30000);

        new SocketImpl().listen();
    }
}