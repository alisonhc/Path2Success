package com.example.tyler.Path2Success;


/**
 * Created by angelica on 3/2/16.
 */
public class IndividualGoal {

    private String title;
    private String dueDate;
    private Boolean isDone;
    private Integer category;


    public IndividualGoal(String title,String date, Integer category){
        this.title=title;
        this.dueDate=date;
        isDone=false;
        this.category=category;
    }

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