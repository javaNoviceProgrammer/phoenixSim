package phoenixSim.controls;

import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import phoenixSim.builder.intf.ActionInterface;
import phoenixSim.modules.VariableSelectorModule;
import phoenixSim.util.SimulationDataBase;

public class SetButton extends Button {
	
	ContextMenu contextMenu ;
	MenuItem chooseFromDataBase ;
	
	public SetButton(String varName, String varAlias, SimulationDataBase simDataBase) {
		setText("Set");
		chooseFromDataBase = new MenuItem("choose from database") ;
		contextMenu = new ContextMenu() ;
		contextMenu.getItems().add(chooseFromDataBase) ;
		setContextMenu(contextMenu);
		
		contextMenu.setOnAction(e1 -> {
			VariableSelectorModule varSelector = new VariableSelectorModule(simDataBase) ;
			varSelector.setExitAction(new ActionInterface() {
				@Override
				public void setExitAction() {
					simDataBase.addNewVariable(varSelector.getController().getVariable());
				}
			});
		});
		
	}

}
