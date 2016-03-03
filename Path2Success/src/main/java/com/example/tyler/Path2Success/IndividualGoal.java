package com.example.tyler.Path2Success;

import java.util.Date;

/**
 * Created by angelica on 3/2/16.
 */
public class IndividualGoal {

    String title;
    Date date;


    public IndividualGoal(String title, Date date){
        this.title=title;
        this.date=date;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "IndividualGoal{" +
                "title='" + title + '\'' +
                ", date=" + date +
                '}';
    }

}

