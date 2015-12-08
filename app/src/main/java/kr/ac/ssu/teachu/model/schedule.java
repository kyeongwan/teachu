package kr.ac.ssu.teachu.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by nosubin on 2015-12-06.
 */
public class Schedule implements Comparable<Schedule> {
    public int type;
    public String title = "";
    public String day = "";
    public Date date;
    public String context;

    public Schedule(String title, String context,  Date time) {
        this.title = title;
        this.date = time;
        this.context = context;
    }

    public Schedule(String s) {
        this.title = s;
        this.type = 0;
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public int compareTo(Schedule schedule) {
        return -1*(schedule.date.compareTo(this.date));
    }
}

