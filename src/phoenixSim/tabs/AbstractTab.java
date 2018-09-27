package phoenixSim.tabs;

import org.controlsfx.control.StatusBar;
import People.Meisam.GUI.Utilities.SimulationDataBase;
import javafx.scene.control.Tab;

public abstract class AbstractTab {

	public abstract Tab getTab() ;
	public abstract StatusBar getStatusBar() ;
	public abstract void setSimDataBase(SimulationDataBase simDataBase) ;
	public abstract SimulationDataBase getSimDataBase() ;
	public abstract <T extends AbstractTabController> T getController() ;
	public abstract String getName() ;
	public abstract void popUpTab() ;

}
