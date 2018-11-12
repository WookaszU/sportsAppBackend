package pl.edu.agh.sportsApp.repository.event.projection;

public interface RatingFormElement {

    Long getUserId();

    String getFirstName();

    String getLastName();

    String getPhotoId();

    Double getRating();

}
