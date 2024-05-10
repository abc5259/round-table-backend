package com.roundtable.roundtable.business.mail;

public interface MailProvider {

    void sendEmail(String toEmail, String title, String text);

}
