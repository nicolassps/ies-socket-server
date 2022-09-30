package br.ies.socket.server;

import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;

import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;
import static java.util.Objects.isNull;

@Log
public class SocketImpl {
    private static final Integer PORT = 3030;
    private static final Integer MAX_TENTATIVE = 3;
    private ServerSocket socket;

    public void listen() {
        log.log(INFO, "Starting server listening service");

        startup(0);
    }

    private void startup(Integer tentative){
        if(tentative >= MAX_TENTATIVE)
            throw new RuntimeException("Max connections retry");

        try{
            if(isNull(socket))
                socket = new ServerSocket(PORT);

            var client = socket.accept();

            var processor = new Processor(client.getInputStream().readAllBytes());
            new Thread(processor).run();

            startup(tentative);
        } catch (IOException e) {
            log.log(WARNING, "Connection lost, retrying connect");
            socket = null;
            startup(tentative++);
        }
    }
}
