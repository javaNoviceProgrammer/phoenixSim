package phoenixSim.modules;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import phoenixSim.builder.AbstractController;

public class GeneralCreditsController extends AbstractController {
	
	@FXML Button lrlWebsiteButton ;
	@FXML Button feedbackButton ;
	
	
	@FXML
	public void websitePressed() throws Exception, URISyntaxException{
		Desktop.getDesktop().browse(new URI("http://lightwave.ee.columbia.edu"));
	}
	
	@FXML
	public void feedbackPressed() throws Exception{
    	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Credits/FeedbackForm/feedback_form.fxml")) ;
    	WindowBuilder credits = new WindowBuilder(loader) ;
    	credits.setIcon("/People/Meisam/GUI/Credits/FeedbackForm/Extras/feedback.png");
    	credits.build("Feedback Form", false);
	}
	
	
	

}
