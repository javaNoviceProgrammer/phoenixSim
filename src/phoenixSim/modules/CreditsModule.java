package phoenixSim.modules;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class CreditsModule {

    FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Credits/GeneralCredits/general_credits.fxml")) ;
    WindowBuilder credits = new WindowBuilder(loader) ;

    public CreditsModule(){
        credits.setIcon("/People/Meisam/GUI/Credits/GeneralCredits/Extras/gift-box.png");
        try {
			credits.build_NoModality("Credits", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
