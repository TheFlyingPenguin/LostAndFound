package com.example.ajklen.lostandfound;

/**
 * Created by ajklen on 2/6/15.
 */
public class ListItem {
    public String item, location, description;
    public double distance, latitude, longitude;

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

    public ListItem(String result){
        super();

        String[] exp = result.split("~");

        item = exp[0];
        description = exp[1];
        latitude = Double.valueOf(exp[2]);
        longitude = Double.valueOf(exp[3]);
        location = exp[4];
        distance = 0;
    }

    @Override
    public String toString(){
        return String.format("%s at %s: %s", item, location, description);
    }
}
