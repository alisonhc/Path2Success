package com.example.tyler.Path2Success;


import java.util.SimpleTimeZone;

/**
 * Created by angelica on 3/2/16.
 */
public class IndividualGoal {

    private String title;
    private String dueDate;
    private Boolean isDone;
    private Integer category;
    private String randomID;


    public IndividualGoal(String title,String date, Integer category){
        this.title=title;
        this.dueDate=date;
        isDone=false;
        this.category=category;
        //add the object to local storage with the above parameters
        //addToLocalStorage();
    }

//    private void addToLocalStorage() {
//
//    }

    public String getTitle() {
        return title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String date) {
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

    public Integer getCategory(){
        return category;
    }

    public void setCategory(Integer category){
        this.category=category;
    }

    @Override
    public String toString() {
        return "IndividualGoal{" +
                "title='" + title + '\'' +
                ", date=" + dueDate +
                '}';
    }

}