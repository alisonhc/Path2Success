package com.example.tyler.Path2Success;

import java.util.Date;

/**
 * Created by angelica on 3/2/16.
 */
public class IndividualGoal {

    private String title;
    private String dueDate;
    private Boolean isDone;


    public IndividualGoal(String title,String date){
        this.title=title;
        this.dueDate=date;
        isDone=false;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return dueDate;
    }

    public void setDate(String date) {
        this.dueDate = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void goalIsDone(){
        isDone=true;
    }

    public void goalIsUndone(){
        isDone=false;
    }
    public Boolean getIfIsDone(){
        return isDone;
    }

    @Override
    public String toString() {
        return "IndividualGoal{" +
                "title='" + title + '\'' +
                ", date=" + dueDate +
                '}';
    }

}

