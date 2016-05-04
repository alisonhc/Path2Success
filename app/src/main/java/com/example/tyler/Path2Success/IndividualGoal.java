package com.example.tyler.Path2Success;


import java.io.Serializable;
import java.util.SimpleTimeZone;

/**
 * Created by angelica on 3/2/16.
 */
public class IndividualGoal implements Serializable {

    private String title;
    private String dueDate;
    private Boolean isCompleted;
    private Integer category;
    private String randomID;


    public IndividualGoal(String title,String date, Integer category){
        this.title=title;
        this.dueDate=date;
        isCompleted=false;
        this.category=category;
        this.randomID = "";
    }

    public String getTitle() {
        return title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String date) {
        dueDate = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean val) {
        isCompleted = val;
    }

    public Integer getCategory(){
        return category;
    }

    public void setCategory(Integer category){
        this.category=category;
    }

    public String getRandomID() {
        return randomID;
    }

    public void setRandomID(String randomID) {
        this.randomID = randomID;
    }

    @Override
    public String toString() {
        return "IndividualGoal{" +
                "title='" + title + '\'' +
                ", date=" + dueDate +
                '}';
    }

}