package pl.edu.agh.sportsApp.repository.user.projection;

public interface UserData {

    Long getId();

    String getFirstName();

    String getLastName();

    Double getRating();

    String getPhotoId();

    Integer getFavoriteCategory();

    Integer getEventParticipationNum();

    Integer getEventOwnershipsNum();

    Integer getUserRatingsNum();

}
