package com.semakon.dwarsserver2.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Author:  M.P. de Vries
 * Date:    19-12-2018
 */
public class ClientHandler extends Thread {

    /** Dwars Server objects */
    private DwarsServer server;

    /** Connection tools */
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(DwarsServer server, Socket socket) {
        this.server = server;
        this.socket = socket;
        // TODO: add input handler
    }

    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String input;
            while ((input = in.readLine()) != null) {
                // Debug
                System.out.println("Received from " + this.getId() + ": " + input);

                // TODO: Handle input
            }
        } catch (IOException e) {
            System.err.println("Client disconnected");
            server.clientDisconncted(this);
        }
    }

    public void send(String msg) {
        out.println(msg);
    }

    public void disconnect() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Disconnected from client");
    }

}
