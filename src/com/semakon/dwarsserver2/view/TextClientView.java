package com.semakon.dwarsserver2.view;

import com.semakon.dwarsserver2.server.TestClient;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Author:  M.P. de Vries
 * Date:    19-12-2018
 */
public class TextClientView extends Thread {

    private TestClient client;
    private boolean stop;

    public TextClientView(TestClient client) {
        this.client = client;
        stop = false;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (!stop) {
            if (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(" ");

                // Empty input
                if (split.length < 1) {
                    displayError("Invalid command: Empty input");
                    continue;
                }
                // switch over command
                switch (split[0]) {
                    case "send":
                        client.send(addSpaces(Arrays.copyOfRange(split, 1, split.length)));
                        break;
                    case "quit":
                        client.shutdown();
                        break;
                    default:
                        displayError("Invalid command");
                }
            }
        }
    }

    /**
     * Turns a String array into a single string with spaces.
     * @param msg The String array where spaces are added.
     * @return A single String with spaces.
     */
    private String addSpaces(String[] msg) {
        StringBuilder res = new StringBuilder();
        for (String s : msg) {
            res.append(s).append(" ");
        }
        res.deleteCharAt(res.length() - 1);
        return res.toString();
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
