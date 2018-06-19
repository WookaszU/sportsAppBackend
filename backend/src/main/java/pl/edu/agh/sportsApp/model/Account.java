package pl.edu.agh.sportsApp.model;

import org.assertj.core.util.Lists;

import javax.persistence.*;
import java.util.List;

@Entity(name = "account")
public class Account {

    @Column(name = "account_id")
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE)
    private byte[] image;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "account_event", joinColumns = {@JoinColumn(name = "account_id")})
    private List<Event> events;

    public Account() {
    }

    public Account(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        events = Lists.newArrayList();
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }
}
