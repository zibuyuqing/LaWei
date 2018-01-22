package com.lingy.lawei.weibo.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingy on 2017-10-24.
 */

public class StatusList {

    private List<Status> statuses = new ArrayList<Status>();
    private int previous_cursor = 0;
    private int next_cursor = 0;
    private int total_number = 0;

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public int getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(int previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public int getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(int next_cursor) {
        this.next_cursor = next_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }
}