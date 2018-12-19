package com.semakon.dwarsserver2.server;

import com.semakon.dwarsserver2.view.TextClientView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Author:  M.P. de Vries
 * Date:    19-12-2018
 */
public class TestClient {

    private String host;
    private int port;

    private boolean connected;

    private TextClientView view;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public TestClient(String host, int port) {
        this.host = host;
        this.port = port;
        connected = false;

        view = new TextClientView(this);
        view.start();
    }

    public void connect() {
        if (connected) {
            view.displayError("You are already connected");
            return;
        }
        try {
            socket = new Socket(host, port);
            view.displayMsg("Connected to " + host + " on port " + port);
            connected = true;
            run();
        } catch (IOException e) {
            view.displayError("Could not connect to " + host + " on port " + port);
        }
    }

    private void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String input;
            while ((input = in.readLine()) != null) {
                // Debug
                view.displayMsg("Received: " + input);

                // TODO: Handle input
            }
        } catch (IOException e) {
            view.displayError("Disconnected from server");
        }

    }

    public void send(String msg) {
        out.println(msg);
    }

    public void shutdown() {
        view.displayMsg("Shutting down...");
        view.close();
        System.exit(0);
    }

    public static void main(String[] args) {
        new TestClient("localhost", 9090).connect();
    }

}
