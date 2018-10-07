package pl.edu.agh.sportsApp.emailsender;


import pl.edu.agh.sportsApp.model.Account;

import javax.mail.MessagingException;

public interface IEmailSender {

    void send(String to, String title, String content) throws MessagingException;

    void sendRegisterEmail(String email, String registerToken) throws MessagingException;

}
