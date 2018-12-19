package com.semakon.dwarsserver2.view;

import com.semakon.dwarsserver2.server.DwarsServer;

import java.util.Scanner;

/**
 * Author:  M.P. de Vries
 * Date:    19-12-2018
 */
public class TextServerView extends Thread {

    private DwarsServer server;
    private boolean stop;

    public TextServerView(DwarsServer server) {
        this.server = server;
        stop = false;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (!stop) {
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                if (input.startsWith("send")) {
                    server.broadcast(input);
                }
                // TODO: handle other commands
            }
        }
    }

    public void displayMsg(String msg) {
        System.out.println(msg);
    }

    public void displayError(String error) {
        System.err.println(error);
    }

    public void close() {
        stop = true;
    }

}
