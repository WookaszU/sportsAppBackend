package pl.edu.agh.sportsApp.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "event")
public class Event {
    @Column
    @Id
    @GeneratedValue
    private int id;

    @Column
    private String title;

    @Column
    private String location;

    @Column(length = 5000)
    private String content;

    public Event() {
    }

    public Event(String title, String location, String content) {
        this.location = location;
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation(){return location;}

    public void setLocation(String location){this.location=location;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
