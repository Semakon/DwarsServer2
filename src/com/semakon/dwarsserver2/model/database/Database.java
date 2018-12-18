package com.semakon.dwarsserver2.model.database;

import com.semakon.dwarsserver2.model.User;
import com.semakon.dwarsserver2.model.rankings.Ranking;

import java.util.List;

/**
 * Author:  M.P. de Vries
 * Date:    18-12-2018
 */
public interface Database {

    void saveUsers(List<User> users);

    List<User> loadUsers();

    void saveRankings(List<Ranking> rankings);

    List<Ranking> loadRankings();

}
