package sourcecode.util;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;

/**
 *
 * @author Fábio Augusto Rodrigues
 */
public class SendEmail {
    
    public static boolean sendEmail(String from, String password, String to, String title, String content, String anexo){

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.gmail.com");
        email.setSslSmtpPort("465");
        email.setStartTLSRequired(true);
        email.setStartTLSEnabled(true);
        email.setSSLOnConnect(true);

        email.setAuthenticator(new DefaultAuthenticator(from, password));

        try{
            email.setFrom(from);

            email.setSubject(title);
            email.setMsg(content);
            email.addTo(to);

            if (anexo != null){
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(anexo);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDescription("Send e-mail");
                attachment.setName("E-mails");
                
                email.attach(attachment);
            }

            email.send();
                
        }catch(Exception error){
            return false;
        }
        
       return true;
    }
    
    public static boolean sendEmailAll(String from, String password, String emails, String subject, String content,
        String attach){   
        
        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
          new javax.mail.Authenticator() {
               protected PasswordAuthentication getPasswordAuthentication()
               {
                     return new PasswordAuthentication(from,
                     password);
               }
          });

        session.setDebug(true);

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            
            //Remetente
            Address[] toUser = InternetAddress.parse(emails);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(subject);//Assunto
            message.setText(content);
            
            if (attach != null){
                
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(attach);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDescription("Send e-mail");
                
                BodyPart messageBodyPart = new MimeBodyPart();

                messageBodyPart.setText("This is message body");
                
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);

                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attach);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(attach);
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);
            }
            
            Transport.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
