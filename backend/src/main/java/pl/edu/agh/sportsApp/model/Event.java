package pl.edu.agh.sportsApp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@NoArgsConstructor
@Entity(name = "event")
public class Event {

    @Getter
    @Column
    @Id
    @GeneratedValue
    private int id;

    @Column
    @Getter @Setter
    private String title;

    @Column
    @Getter @Setter
    private String location;

    @Column(length = 5000)
    @Getter @Setter
    private String content;

    public Event(String title, String location, String content) {
        this.location = location;
        this.title = title;
        this.content = content;
    }

}
