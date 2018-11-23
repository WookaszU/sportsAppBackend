package pl.edu.agh.sportsApp.emailsender;

import pl.edu.agh.sportsApp.model.User;

import javax.mail.MessagingException;

public interface IEmailSender {

    void send(String to, String title, String content, Boolean isHtml) throws MessagingException;

    void sendRegisterEmail(User user, String registerToken) throws MessagingException;

}
