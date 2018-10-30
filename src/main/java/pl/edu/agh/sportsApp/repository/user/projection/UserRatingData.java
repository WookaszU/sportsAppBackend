package pl.edu.agh.sportsApp.repository.user.projection;

public interface UserRatingData {

    Long getId();

    String getDescription();

    Double getRating();

    Long getEventId();

    Long getEvaluativeId();

    String getEvaluativeFirstName();

    String getEvaluativeLastName();

}
