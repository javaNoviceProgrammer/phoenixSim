package phoenixSim.modules;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import phoenixSim.builder.WindowBuilder;

public class CreditsModule {

    FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/phoenixSim/fxmls/general_credits.fxml")) ;
    WindowBuilder credits = new WindowBuilder(loader) ;

    public CreditsModule(){
        credits.setIcon("/phoenixSim/extras/gift-box.png");
        try {
			credits.build_NoModality("Credits", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
