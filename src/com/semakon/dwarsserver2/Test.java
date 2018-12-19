package com.semakon.dwarsserver2;

import com.semakon.dwarsserver2.model.User;
import com.semakon.dwarsserver2.model.database.Database;
import com.semakon.dwarsserver2.model.database.XMLdatabase;
import com.semakon.dwarsserver2.model.rankings.Ranking;
import com.semakon.dwarsserver2.model.rankings.RankingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  M.P. de Vries
 * Date:    18-12-2018
 */
public class Test {

    public static void main(String[] args) {
        Database db = XMLdatabase.getInstance();
        List<User> users = db.loadUsers();
//
//        // Print users and rankings
//        System.out.println("Users:");
//        for (User u : users) {
//            System.out.println(u);
//        }

        List<Ranking> rankings = new ArrayList<>();
        List<RankingItem> items = new ArrayList<>();
        items.add(new RankingItem(1, users.get(0), 0));
        items.add(new RankingItem(2, users.get(1), 10));
        items.add(new RankingItem(3, users.get(2), 12));
        Ranking ranking = new Ranking(1, "Panda Punten", "Weken niet geneukt.");
        ranking.setRankingItems(items);
        rankings.add(ranking);
        db.saveRankings(rankings);

        List<Ranking> loadRankings = db.loadRankings(users);


        System.out.println("\nRankings:");
        for (Ranking r : loadRankings) {
            System.out.println(r);
        }
    }

}
