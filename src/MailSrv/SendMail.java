package MailSrv;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import com.sun.mail.util.MailSSLSocketFactory;
 

public class SendMail {

	
	private final static String username = "psdmarcelly@gmail.com"; // do painel de controle do SMTP
	private final static String password = "barbaridade"; // do painel de controle do SMTP
	private final static Protocol protocol = Protocol.SMTPS;
	private final static boolean debug = true;
    
    public static void main(String[] args) throws Exception {
    	/**
    	 * Metodo responsável pela leitura do arquivo xml 
    	 */
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder Builder = dbFactory.newDocumentBuilder();
		Document document = Builder.parse(new File("Email.xml"));
		document.getDocumentElement().normalize();
	
		NodeList Protocol = document.getElementsByTagName("protocolo");
		String Protocolo = Protocol.item(0).getTextContent();
		NodeList server = document.getElementsByTagName("servidor");
		String Servidor = server.item(0).getTextContent();
		NodeList Port = document.getElementsByTagName("porta");
		String Porta = Port.item(0).getTextContent();
    	
		String to [] =  {"dpsmarcelly@gmail.com"};
		String from = "psdmarcelly@gmail.com";
    	
    	Properties props = new Properties();
	    props.put("mail.transport.protocol",Protocolo); 
	    props.put("mail.smtp.host", Servidor);
	    props.put("mail.smtp.port", Porta);       
	    props.put("mail.smtp.auth", "true");
	    switch (protocol) {
        case SMTPS:
            props.put("mail.smtp.ssl.enable", true);
            break;
        case TLS:
            props.put("mail.smtp.starttls.enable", true);
            break;
    }
        
	    /**
	     * Verificação de segurança SSL
	     */
		MailSSLSocketFactory sf = null;
		try {
			sf = new MailSSLSocketFactory(); //Inicializa uma nova conexão SSL
		} catch (GeneralSecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		sf.setTrustAllHosts(true); //Verificação de confiabilidade do host 
		props.put("mail.smtp.ssl.socketFactory", sf); // Metodo responsável por criar as camadas de coenxão SMTP
		
		/**
		 * Metodo responsável por verificar a autenticação do email de envio
		 */
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                  protected PasswordAuthentication getPasswordAuthentication() {
                       return new PasswordAuthentication(username,password);
                  }
             });
        	
	    session.setDebug(debug);
		/**
		 * Metodo responsável por montar o email de destino
		 */
		try {
			MimeMessage message = new MimeMessage(session);
			InternetAddress[] addressTo = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}
			message.setRecipients(javax.mail.Message.RecipientType.TO, addressTo); // destinatário
			message.setFrom(new InternetAddress(from));
			message.setSubject("Teste Aplicativo JEE"); // assunto 
			message.setText("Teste Xml," + "\n\nTeste XML"); // conteúdo/corpo  do email
			
			Transport tr = session.getTransport(Protocolo);
			tr.connect(Servidor, username, password);
			tr.sendMessage(message, message.getAllRecipients());
			message.saveChanges();

			tr.close();
			System.out.println("E-mail enviado com Sucesso!");

		} catch (MessagingException e) {
			System.out.println(">> Erro: Envio Mensagem");
			e.printStackTrace();
		}
    }
}
