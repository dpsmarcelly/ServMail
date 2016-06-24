package Main;

import java.util.HashMap;
import java.util.Map;

import Controller.MailServer;
import Model.Email;

 /**
  * Class responsible for reporting all data related to sending email.
  * @author marcelly.paula
  *
  */

public class SendMail {

	public static void main(String[] args) throws Exception {
	
		Email email = new Email();
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("dvd.pansardis@gmail.com", "email gmail");
		map.put("dpsmarcelly@gmail.com", "gmail");
		map.put("alexandre.penteado@geopx.com.br", "geopx");
		email.setToMailsUsers(map);
		email.setSubjectMail("fuck the system");
		email.setBodyMail("fuck all");
		
		MailServer ms = new MailServer("Email.xml");
		
		if(ms.SendMail(email)){
			System.out.print("Email successfully sent!");
		}else{
			System.out.print(">> Error: Send Message!");
		}

	}
}
