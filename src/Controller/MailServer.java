package Controller;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map.Entry;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.sun.mail.util.MailSSLSocketFactory;
import Model.Email;

/**
 * Class responsible for receiving
 * Settings, create the necessary objects
 * For email and then send it.
 * It is this class that we will work with the reading of the XML file
 * Containing the parameters of protol, port, server, and User and Password
 * Responsible for sending e-mails with the JavaMail API
 * @author marcelly.paula
 *
 */

public class MailServer {
	
	private final static Protocoll protocoll = Protocoll.SMTPS;
	private final static boolean debug = true;
	private NodeList nlProtocol;
	private NodeList nlServer; 
	private NodeList nlPort;
	private NodeList nlUserName;
	private NodeList nlPassword;
	String UserName;
	String Password;
	String Protocol;
	String Server;
	String Port;
	
	public MailServer(String pathXml){
		ReadXml (pathXml);
	}
	/**
	 * Method responsible for making reading
	 * Xml file and capture the values of tags
	 * @param pathXml
	 * @return
	 */
	private boolean ReadXml (String pathXml){
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder Builder;
			Builder = dbFactory.newDocumentBuilder();
			Document document = Builder.parse(new File(pathXml));
			document.getDocumentElement().normalize();
			
			nlProtocol = document.getElementsByTagName("protocol");
			Protocol = nlProtocol.item(0).getTextContent();
		
			nlServer = document.getElementsByTagName("server");
			Server = nlServer.item(0).getTextContent();
			
			nlPort = document.getElementsByTagName("port");
			Port = nlPort.item(0).getTextContent();
			
			nlUserName = document.getElementsByTagName("username");
			UserName = nlUserName.item(0).getTextContent();
			
			nlPassword = document.getElementsByTagName("password");
			Password = nlPassword.item(0).getTextContent();
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	/**
	 * responsible method necessary properties for sending email
	 * @param email
	 * @return
	 * @throws IOException
	 */
	public boolean SendMail(Email email) throws IOException {
		try { 
			Properties props = new Properties();
			
		    props.put("mail.transport.protocol",Protocol); 
		    props.put("mail.smtp.host", Server);
		    props.put("mail.smtp.port", Port);       
		    props.put("mail.smtp.auth", "true");
		    
		    switch (protocoll) {
		        case SMTPS:
		            props.put("mail.smtp.ssl.enable", true);
		            break;
		        case TLS:
		            props.put("mail.smtp.starttls.enable", true);
		            break;
				default:
					break;
		    }
		    
		    /**
		     * SSL security check
		     */
			MailSSLSocketFactory sf = null;
		
			sf = new MailSSLSocketFactory(); //Initializes a new SSL connection
				
			sf.setTrustAllHosts(true); //host reliability check
			props.put("mail.smtp.ssl.socketFactory", sf); // Method responsible for creating the SMTP connection layer
			
			/**
			 * responsible method for verifying authentication sending email
			 */
	        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                  protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(UserName, Password);
                  }
             });
        	
		    session.setDebug(debug);
			/**
			 * responsible method for assembling the destination email
			 */
		
			MimeMessage message = new MimeMessage(session);
			
	        boolean first = true;
	        for (Entry<String, String> map : email.getToMailsUsers().entrySet()) {
	            if (first) {
	                //set the first recipient
	            	message.addRecipient(javax.mail.Message.RecipientType.TO,
	                          new InternetAddress(map.getKey(), map.getValue())
	                 );
	                first = false;
	            } else {
	                //are the other recipients
	            	message.addRecipient(javax.mail.Message.RecipientType.CC,
	                          new InternetAddress(map.getKey(), map.getValue())
	                 );
	            }
	        }
	        
			message.setSubject(email.getSubjectMail()); // Set the email subject 
			message.setText(email.getBodyMail()); // set the content/body of the email
			
			Transport tr = session.getTransport(Protocol);
			tr.connect(Server, UserName, Password);
			tr.sendMessage(message, message.getAllRecipients());
			message.saveChanges();

			tr.close();
			System.out.println("Email successfully sent!");

		} catch (MessagingException | GeneralSecurityException e) {
			System.out.println(">> Error: Send Message!");
			e.printStackTrace();
		}
	    
		return true;	
	}
}

