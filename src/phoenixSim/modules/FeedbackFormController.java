package phoenixSim.modules;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import phoenixSim.builder.AbstractController;
import phoenixSim.builder.WindowBuilder;

public class FeedbackFormController extends AbstractController {
	
	@FXML TextField nameTextField ;
	@FXML ComboBox<String> topicComboBox ;
	@FXML TextArea feedbackTextArea ;
	@FXML Button sendButton ;
	@FXML ImageView errorImage ;
	@FXML Text errorText ;
	
	
	@FXML
	public void initialize(){
		clearErrorMessage() ;
		// initializing topic combo box
		topicComboBox.getItems().clear();
		topicComboBox.getItems().addAll("Reporting a bug", "Suggestion on missing functionalities", "Suggestion on existing fuctionalities", "Other") ;

	}
	
	private void clearErrorMessage(){
		errorImage.setVisible(false);
		errorText.setVisible(false); 
	}
	
	private boolean checkAllBoxesFilled(){
		if(nameTextField.getText().isEmpty() || topicComboBox.getSelectionModel().isEmpty() || feedbackTextArea.getText().isEmpty()){
			return false ;
		}
		else{
			return true ;
		}
	}
	
	@FXML
	public void sendPressed() throws AddressException, MessagingException{
		if(checkAllBoxesFilled()){
			// send
			sendMail();
		}
		else{
			errorImage.setVisible(true);
			errorText.setVisible(true); 
			// add listener to items 
			nameTextField.setOnKeyTyped(e->{
				clearErrorMessage() ;
			}) ;
			feedbackTextArea.setOnKeyTyped(e->{
				clearErrorMessage() ;
			});
			topicComboBox.setOnMouseClicked(e->{
				clearErrorMessage();
			});
		}
	}
	
	private void sendMail() throws AddressException, MessagingException{
		String from = "" ; // put your username here, "user@gmail.com"
		String pass = "" ; // put your password here
		String[] to = {"mb3875@columbia.edu"} ; // put recipients 
		String host = "smtp.gmail.com" ;
		
		Properties prop = System.getProperties() ;
		prop.put("mail.smtp.starttls.enable", "true") ;
		prop.put("mail.smtp.host", host) ;
		prop.put("mail.smtp.user", from) ;
		prop.put("mail.smtp.password", pass) ;
		prop.put("mail.smtp.port", "587") ;
		prop.put("mail.smtp.auth", "true") ;
		
		Session session = Session.getDefaultInstance(prop) ;
		MimeMessage message = new MimeMessage(session) ;
		message.setFrom(new InternetAddress(from));
		InternetAddress[] toAddress = new InternetAddress[to.length] ;
		for(int i=0; i<to.length; i++){
			toAddress[i] = new InternetAddress(to[i]) ;
		}
		for(int i=0; i<toAddress.length; i++){
			message.setRecipient(Message.RecipientType.TO, toAddress[i]);
		}
		
		message.setSubject("PhoenixSim Feedback: " + topicComboBox.getSelectionModel().getSelectedItem());
		String name = nameTextField.getText() ;
		message.setContent(feedbackTextArea.getText() + "    --" + name, "text/html; charset=utf-8");
		Transport transport = session.getTransport("smtp") ;
		transport.connect(host, from, pass);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
		
		FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Credits/FeedbackForm/Confirmation/sent_confirmation.fxml")) ;
		WindowBuilder confirmation = new WindowBuilder(loader) ;
		confirmation.setIcon("/People/Meisam/GUI/Credits/FeedbackForm/Extras/email.png");
		try {
			confirmation.build("Confirmation Status", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
