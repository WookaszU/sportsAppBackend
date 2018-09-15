package pl.edu.agh.sportsApp.emailsender;


import pl.edu.agh.sportsApp.model.Account;

import javax.mail.MessagingException;

public interface IEmailSender {

    void send(String to, String title, String content) throws MessagingException;

    String sendRegisterEmail(Account newAccount) throws MessagingException;

}
