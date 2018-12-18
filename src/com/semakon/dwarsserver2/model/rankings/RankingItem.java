package com.semakon.dwarsserver2.model.rankings;

import com.semakon.dwarsserver2.model.User;

/**
 * Author:  M.P. de Vries
 * Date:    18-12-2018
 */
public class RankingItem {

    private int id;
    private User user;
    private int points;

    public RankingItem(int id, User user, int points) {
        this.id = id;
        this.user = user;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return id + ": " + user.getName() + " - " + points;
    }

}
