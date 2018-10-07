package pl.edu.agh.sportsApp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@Entity(name = "event")
@Data
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

    // full Account? or id
    @Column
    private int ownerAccountId;

    public Event(String title, String location, String content) {
        this.location = location;
        this.title = title;
        this.content = content;
    }

}
