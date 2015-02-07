package com.example.ajklen.lostandfound;

/**
 * Created by ajklen on 2/6/15.
 */
public class ListItem {
    public String item, location, description;
    public double distance;

    public ListItem(){
        super();
    }

    public ListItem(String item, String location, String description, double distance){
        super();
        this.item = item;
        this.location = location;
        this.description= description;
        this.distance = distance;
    }

    @Override
    public String toString(){
        return String.format("%s at %s: %s", item, location, description);
    }
}
