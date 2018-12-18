package com.semakon.dwarsserver2.model.rankings;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  M.P. de Vries
 * Date:    18-12-2018
 */
public class Ranking {

    private int id;
    private String name;
    private String description;
    private List<RankingItem> rankingItems;

    public Ranking(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rankingItems = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RankingItem> getRankingItems() {
        return rankingItems;
    }

    public void setRankingItems(List<RankingItem> rankingItems) {
        this.rankingItems = rankingItems;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder(id + ": " + name + " {\n");

        for (RankingItem i : rankingItems) {
            res.append("\t").append(i).append(",\n");
        }
        res.delete(res.length() - 2, res.length() - 1);
        res.append("}");

        return res.toString();
    }

}
