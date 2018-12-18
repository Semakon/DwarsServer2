package com.semakon.dwarsserver2;

import com.semakon.dwarsserver2.model.User;
import com.semakon.dwarsserver2.model.database.Database;
import com.semakon.dwarsserver2.model.database.XMLdatabase;

/**
 * Author:  M.P. de Vries
 * Date:    18-12-2018
 */
public class Test {

    public static void main(String[] args) {
        Database db = XMLdatabase.getInstance();
        for (User u : db.loadUsers()) {
            System.out.println(u);
        }
    }

}
