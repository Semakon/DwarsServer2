package com.semakon.dwarsserver2;

import com.semakon.dwarsserver2.server.DwarsServer;

/**
 * Author:  M.P. de Vries
 * Date:    18-12-2018
 */
public class Main {

    public static final int PORT = 9090;

    public static void main(String[] args) {
        DwarsServer server = new DwarsServer(PORT);
        server.run();
    }

}
