package Model;

import java.util.Map;

/**
 * Class responsible for creating all the attributes
 * that will be needed to create and send an email
 * @author marcelly.paula
 *
 */

public class Email {
	
	//mail subject
	private String subjectMail;
	//email body, where this text message
	private String bodyMail;
	//email list and names of recipients
	 private Map<String, String> toMailsUsers;

	
	public String getSubjectMail() {
		return subjectMail;
	}
	public void setSubjectMail(String subjectMail) {
		this.subjectMail = subjectMail;
	}
	public String getBodyMail() {
		return bodyMail;
	}
	public void setBodyMail(String bodyMail) {
		this.bodyMail = bodyMail;
	}
	public Map<String, String> getToMailsUsers() {
		return toMailsUsers;
	}
	public void setToMailsUsers(Map<String, String> toMailsUsers) {
		this.toMailsUsers = toMailsUsers;
	}
	

}

