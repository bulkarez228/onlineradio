package com.example.onlineradio;

public class Station implements Comparable<Station> {
    String name;
    String url;
    String description;
    String type;
    Boolean like=false;
    Boolean played=false;
    int img;

    public Station() {
    }

    public Station(String url, String name, String description, String type){
        this.url=url;
        this.name=name;
        this.description=description;
        this.type=type;
        if (this.name.equals("")){
            this.name="No name radio";
        }
    }

    public Station(String url, String name, String description, String type, int img){
        this.url=url;
        this.name=name;
        this.description=description;
        this.type=type;
        this.img = img;
        if (this.name.equals("")){
            this.name="No name radio";
        }
    }

    public Station(String type){
        this.type=type;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public Boolean getLike() {
        return like;
    }

    public int getImg() {
        return img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Station station) {
        return Boolean.compare(getLike(), station.getLike());
    }
}
