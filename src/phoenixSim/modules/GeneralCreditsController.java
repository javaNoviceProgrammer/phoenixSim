package phoenixSim.modules;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import phoenixSim.builder.AbstractController;
import phoenixSim.builder.WindowBuilder;

public class GeneralCreditsController extends AbstractController {
	
	@FXML Button lrlWebsiteButton ;
	@FXML Button feedbackButton ;
	
	
	@FXML
	public void websitePressed() throws Exception, URISyntaxException{
		Desktop.getDesktop().browse(new URI("http://lightwave.ee.columbia.edu"));
	}
	
	@FXML
	public void feedbackPressed() throws Exception{
    	FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/feedback_form.fxml")) ;
    	WindowBuilder credits = new WindowBuilder(loader) ;
    	credits.setIcon("/phoenixSim/extras/feedback.png");
    	credits.build("Feedback Form", false);
	}
	
	
	

}
