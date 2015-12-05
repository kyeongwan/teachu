package kr.ac.ssu.teachu.model;

import java.util.Calendar;

/**
 * Created by nosubin on 2015-12-06.
 */
public class schedule
{
    public String work="";
    public String day="";
    public schedule(String s)
    {
        this.work=s;
        Calendar c=Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        this.day=""+c.getTime().getDay();
    }
}
