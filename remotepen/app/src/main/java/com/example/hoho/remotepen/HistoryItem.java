package com.example.hoho.remotepen;

/**
 * # COMP 4521    #  LAU KA HO        20094423  khlauad@ust.hk
 HistoryItem is an Object to store the the information of each record, including RowID, time, context and isBookmark boolean.
 */
public class HistoryItem {
    private int ID;
    private String time;
    private String text;
    private int isBookmark;

    public void History(String t, String txt) {
        this.time=t;
        this.text=txt;
    }

    public void History(int id, String t, String txt, boolean isB){
        this.ID=id;
        this.time=t;
        this.text=txt;
        setIsBookmark(isB);
    }

    public int getID(){return this.ID;}
    public void setID(int id){this.ID=id;}

    public String getTime(){return this.time;}
    public void setTime(String t){this.time=t;}

    public String getText(){return this.text;}
    public void setText(String txt){this.text=txt;}

    public boolean getIsBookmark(){
        if(isBookmark==1)
            return true;
        else
            return false;
    }
    public int getIsBookmarkInt(){return isBookmark;}
    public void setIsBookmark(boolean isB){
        if(isB)
            this.isBookmark=1;
        else
            this.isBookmark=0;
    }
    public void setIsBookmark(int isB){
        if(isB>0)
            this.isBookmark=1;
        else
            this.isBookmark=0;
    }
}
