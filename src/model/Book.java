package model;

import java.util.ArrayList;

public class Book {
    private String title;
    public int totalCount;
    private int totalLent;
    public ArrayList<String> lent = new ArrayList<>();

    Book(String title, int total) {
        this.title = title;
        this.totalCount = total;
    }

    public boolean lend(String toUser) {
        totalLent += 1;
        totalCount -= 1;
        lent.add(toUser);
        return true;
    }

    public boolean ret(String fromUser) {
        totalCount += 1;
        lent.remove(fromUser);
        return true;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalLent() {
        return totalLent;
    }

    public ArrayList<String> getLent() {
        return lent;
    }

    public String getTitle() {
        return title;
    }
}