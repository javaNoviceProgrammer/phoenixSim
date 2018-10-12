package phoenixSim.util.ribbon.skin;

import java.util.Collection;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import phoenixSim.util.ribbon.Ribbon;
import phoenixSim.util.ribbon.RibbonTab;

/**
 * Created by pedro_000 on 1/18/14.
 */
public class RibbonSkin extends SkinBase<Ribbon> {
    VBox outerContainer;
    private TabPane tabPane;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public RibbonSkin(Ribbon control) {
        super(control);
        tabPane = new TabPane();
        outerContainer = new VBox();

        control.getTabs().addListener(new ListChangeListener<RibbonTab>() {
            @Override
            public void onChanged(Change<? extends RibbonTab> changed) {
                tabsChanged(changed);
            }
        });
        updateAddedRibbonTabs(control.getTabs());
        outerContainer.getStyleClass().setAll("outer-container");
        outerContainer.getChildren().addAll(control.getQuickAccessBar(), tabPane);
        getChildren().add(outerContainer);
        control.selectedRibbonTabProperty().addListener(new ChangeListener() {
			@Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                tabPane.getSelectionModel().select((RibbonTab)newValue);
            }
        });
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
                control.setSelectedRibbonTab((RibbonTab)tabPane.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void updateAddedRibbonTabs(Collection<? extends RibbonTab> ribbonTabs) {
        for (RibbonTab ribbonTab : ribbonTabs)
            tabPane.getTabs().add(ribbonTab);
    }

    private void tabsChanged(ListChangeListener.Change<? extends RibbonTab> changed) {
        while(changed.next())
        {
            if (changed.wasAdded())
            {
                updateAddedRibbonTabs(changed.getAddedSubList());
            }
            if(changed.wasRemoved())
            {
                for (RibbonTab ribbonTab : changed.getRemoved())
                    tabPane.getTabs().remove(ribbonTab);
            }
        }
    }

}
