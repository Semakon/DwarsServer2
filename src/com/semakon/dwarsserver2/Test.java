package com.semakon.dwarsserver2;

import com.semakon.dwarsserver2.model.User;
import com.semakon.dwarsserver2.model.database.Database;
import com.semakon.dwarsserver2.model.database.XMLdatabase;
import com.semakon.dwarsserver2.model.rankings.Ranking;

import java.util.List;

/**
 * Author:  M.P. de Vries
 * Date:    18-12-2018
 */
public class Test {

    public static void main(String[] args) {
        Database db = XMLdatabase.getInstance();
        List<User> users = db.loadUsers();
        List<Ranking> rankings = db.loadRankings(users);

        // Print users and rankings
        System.out.println("Users:");
        for (User u : users) {
            System.out.println(u);
        }
        System.out.println("\nRankings:");
        for (Ranking r : rankings) {
            System.out.println(r);
        }
    }

}
