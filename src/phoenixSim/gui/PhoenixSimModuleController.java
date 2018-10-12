package phoenixSim.gui;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.controlsfx.control.StatusBar;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;

import People.Meisam.GUI.PhoenixSim.ModulesLibrary.ModuleCreator.ModuleCreator;
import People.Meisam.GUI.PhoenixSim.TabsLibrary.CouplerDesigner.silicon_nitride.ring_wg.RingWgCouplerSiNTab;
import People.Meisam.GUI.Utilities.plotDigitizer.PlotDigitizer;
import ch.epfl.general_libraries.results.AbstractResultsDisplayer;
import ch.epfl.javancox.experiments.builder.ExperimentConfigurationCockpit;
import ch.epfl.javancox.experiments.builder.tree_model.ObjectConstuctionTreeModel;
import flanagan.io.FileInput;
import flanagan.io.FileOutput;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import mathLib.plot.util.CustomChartPanel;
import mathLib.util.CustomJFileChooser;
import phoenixSim.modules.CreditsModule;
import phoenixSim.modules.DatabaseModule;
import phoenixSim.modules.ExportCompleted;
import phoenixSim.modules.ImportDataModule;
import phoenixSim.modules.PlotterModule;
import phoenixSim.tabs.AbstractTab;
import phoenixSim.tabs.tab.AddDropFittingSymmetricBSDoubleReflectorTab;
import phoenixSim.tabs.tab.AddDropFittingSymmetricBSTab;
import phoenixSim.tabs.tab.AddDropFittingSymmetricTab;
import phoenixSim.tabs.tab.AddDropFittingSymmetricThruTab;
import phoenixSim.tabs.tab.AddDropRingBSTab;
import phoenixSim.tabs.tab.AddDropRingFifthOrderTab;
import phoenixSim.tabs.tab.AddDropRingFourthOrderTab;
import phoenixSim.tabs.tab.AddDropRingSecondOrderTab;
import phoenixSim.tabs.tab.AddDropRingTab;
import phoenixSim.tabs.tab.AddDropRingThirdOrderTab;
import phoenixSim.tabs.tab.AllPassFittingTab;
import phoenixSim.tabs.tab.AllPassRingSelfHeatingTab;
import phoenixSim.tabs.tab.BERPenaltyTab;
import phoenixSim.tabs.tab.CouplerDesignerWelcomeTab;
import phoenixSim.tabs.tab.DataSmoothingTab;
import phoenixSim.tabs.tab.DownSamplingTab;
import phoenixSim.tabs.tab.HeaterDCFittingTab;
import phoenixSim.tabs.tab.HeaterDCTab;
import phoenixSim.tabs.tab.HeaterFreqFittingTab;
import phoenixSim.tabs.tab.HeaterFreqTab;
import phoenixSim.tabs.tab.HeaterImpulseTab;
import phoenixSim.tabs.tab.HeaterPWMTab;
import phoenixSim.tabs.tab.HeaterSinTab;
import phoenixSim.tabs.tab.HeaterStepTab;
import phoenixSim.tabs.tab.HeaterWelcomeTab;
import phoenixSim.tabs.tab.InterfaceTab;
import phoenixSim.tabs.tab.MaterialsTab;
import phoenixSim.tabs.tab.PINResShiftFittingTab;
import phoenixSim.tabs.tab.PINacFittingTab;
import phoenixSim.tabs.tab.PINdcFittingTab;
import phoenixSim.tabs.tab.RaceTrackCouplerTab;
import phoenixSim.tabs.tab.RingRingCouplerTab;
import phoenixSim.tabs.tab.RingWgCouplerTab;
import phoenixSim.tabs.tab.SlabWgCoupledModeProfileTab;
import phoenixSim.tabs.tab.SlabWgCoupledModesTab;
import phoenixSim.tabs.tab.SlabWgModeProfileTab;
import phoenixSim.tabs.tab.SlabWgModesTab;
import phoenixSim.tabs.tab.StripWgCoupledModesSiNTab;
import phoenixSim.tabs.tab.StripWgCoupledModesTab;
import phoenixSim.tabs.tab.StripWgModeProfileTab;
import phoenixSim.tabs.tab.StripWgModesSiNTab;
import phoenixSim.tabs.tab.StripWgModesTab;
import phoenixSim.tabs.tab.StripWgSensitivityTab;
import phoenixSim.tabs.tab.WelcomeTab;
import phoenixSim.tabs.tab.WgWgCouplerTab;
import phoenixSim.util.FileChooserFX;
import phoenixSim.util.OSDetector;
import phoenixSim.util.SimulationDataBase;
import postdoc.ring.AddDropSpectrum;
import postdoc.ring.AllPassERContour;
import postdoc.ring.AllPassQContour;
import postdoc.ring.RingWgSpectrum;

public class PhoenixSimModuleController {

    SimulationDataBase simDataBase = new SimulationDataBase() ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    Map<Tab, Object> openTabs = new HashMap<Tab, Object>() ;
    ArrayList<Tab> orderedTabs = new ArrayList<>() ;
    //**************** FXML Components **********
    @FXML StatusBar statusBar ;
    @FXML public TabPane tabs ;
    @FXML public MenuItem closeApp ;
//    @FXML VBox mainWindow ;
    @FXML BorderPane mainWindow ;
    @FXML CheckMenuItem tabClosingSetting ;
    @FXML public CheckMenuItem fullScreenMode ;

    @FXML
    public void initialize(){
    	addTabListener();
    	ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
    }

    public TabPane getTabs(){
    	return tabs ;
    }

    private void addTabListener(){
    	ChangeListener<Tab> listener = new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				if(tabs.getSelectionModel().isEmpty()){
//					mainWindow.getChildren().remove(statusBar) ;
					statusBar.setText("Please add a tab to begin...");
					statusBar.setProgress(-1);
//					mainWindow.getChildren().add(statusBar) ;
					mainWindow.setBottom(statusBar);
					orderedTabs.clear();
				}
				else{
					updateOpenTabStatusBar();
				}
			}
		};

    	tabs.getSelectionModel().selectedItemProperty().addListener(listener) ;
    }


	@FXML
	public void dataBasePressed() throws IOException{
		DatabaseModule db = new DatabaseModule(simDataBase) ;
		db.refreshTable(simDataBase);
		db.controller.loadCompleted.addListener((v, ov, nv) -> {
			if(nv){
	        	simDataBase = db.controller.getDataBase() ;
	        	updateAllTabsDataBase() ; // do I need this? --> yes, because
	        	db.controller.loadCompleted.set(false); // must change the load flag for next use...
			}
        });
	}

	private void updateAllTabsDataBase(){
		for(Tab x : openTabs.keySet()){
			getTab(x).setSimDataBase(simDataBase);
		}
	}

	@FXML
	private void setOnDragOver(DragEvent event){
		Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        } else {
            event.consume();
        }
	}

	@FXML
	private void setOnDragDropped(DragEvent event){
        Dragboard db = event.getDragboard();
        boolean success = false;
        if (db.hasFiles()) {
            success = true;
            String fullPath = null;
            for (File file:db.getFiles()) {
            	if(checkExtension(file, "module")){
            		// firt clear all tabs
            		closeAllTabs();
                    fullPath = file.getAbsolutePath();
            		FileInput fin = new FileInput(fullPath) ;
            		int M = fin.numberOfLines() ;
            		String[] tabNames = new String[M] ;
            		for(int i=0; i<M; i++){
            			tabNames[i] = fin.readLine() ;
            		}
            		fin.close();
            		System.gc();
            		// now create tabs based on their names
            		for(String x : tabNames){
            			createTab(x);
            		}
            	}
            }
        }
        event.setDropCompleted(success);
        event.consume();
	}

	private boolean checkExtension(File file, String ext){
		String filePath = file.getAbsolutePath() ;
		String[] st = filePath.trim().split("\\.") ;
		int M = st.length ;
		String fileExt = st[M-1] ;
		if(fileExt.equals(ext)){
			return true ;
		}
		else{
			return false ;
		}
	}

	@FXML
	public void popupPlotPressed() throws CloneNotSupportedException{
//		getCurrentTab().getController().getFig().run() ;

		JFreeChart chart = (JFreeChart) getCurrentTab().getController().getFig().getChart().clone() ;
    	CustomChartPanel cpanel = new CustomChartPanel(chart, 640, 450, 640, 450, 640, 450, true, true, true, true, true, true) ;
	    JFrame chartFrame = new JFrame() ;
	    chartFrame.add(cpanel) ;
	    chartFrame.setSize(640, 450);
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/People/Meisam/GUI/Plotters/MatlabPlotter/Extras/presentation.png"));
        chartFrame.setIconImage(image);
        chartFrame.setTitle("Plot Viewer v1.0");
        chartFrame.setVisible(true);
	}

    @FXML
    public void creditsPressed() throws IOException {
    	new CreditsModule() ;
    }

    private void updateOpenTabStatusBar(){
    	try {
	    	if(!tabs.getTabs().isEmpty()){
	        	updateStatusBar(getCurrentTab().getStatusBar());
	    	}
		} catch (Exception e) {
		}

    }

    public void updateStatusBar(StatusBar statusBar){
    	mainWindow.getChildren().remove(this.statusBar) ;
    	this.statusBar = statusBar ;
//    	mainWindow.getChildren().add(statusBar) ;
    	mainWindow.setBottom(statusBar);

//    	javafx.scene.image.Image runImage = new javafx.scene.image.Image("/People/Meisam/GUI/Utilities/Extras/run-button.png") ;
//    	ImageView run = new ImageView(runImage) ;
//    	Button runButton = new Button("Run", run) ;
//    	runButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
//    	runButton.setPrefSize(20, 20);
//    	this.statusBar.getRightItems().add(new Separator(Orientation.VERTICAL)) ;
//    	this.statusBar.getRightItems().add(runButton) ;
////    	this.statusBar.getLeftItems().add(runButton) ;
////    	this.statusBar.getLeftItems().add(new Separator(Orientation.VERTICAL)) ;
    }

	@FXML
	public void popupTabPressed(){
		PhoenixSimModule module = new PhoenixSimModule(simDataBase, false) ;
		AbstractTab X = getCurrentTab() ;
		tabs.getTabs().remove(getCurrentTab().getTab()) ;
		module.getController().addSimulationTab(X);
	}

	@FXML
	public void tabsOrientationSetToLeft(){
		tabs.setSide(Side.LEFT);
	}

	@FXML
	public void tabsOrientationSetToTop(){
		tabs.setSide(Side.TOP);
	}

	@FXML
	public void tabsOrientationSetToRight(){
		tabs.setSide(Side.RIGHT);
	}

	@FXML
	public void exportPlotToMATLAB() throws IOException{
		getCurrentTab().getController().getFig().exportToMatlab();
	}

	@FXML
	public void exportPlotToFile(){
		getCurrentTab().getController().getFig().exportToFile();
	}

    @FXML
    public void openInPlotterPressed() throws IOException {
        new PlotterModule(simDataBase) ;
    }

    @FXML
    public void closeModulePressed(ActionEvent event){
    	tabs.getScene().getWindow().hide();
    }

    @FXML
    public void importDataPressed(){
    	new ImportDataModule(simDataBase) ;
    }

    @FXML
    public void xAxis_to_Log(){
    	getCurrentTab().getController().getFig().setXAxis_to_Log();
    }

    @FXML
    public void xAxis_to_Linear(){
    	getCurrentTab().getController().getFig().setXAxis_to_Linear();
    }

    @FXML
    public void yAxis_to_Log(){
    	getCurrentTab().getController().getFig().setYAxis_to_Log();
    }

    @FXML
    public void yAxis_to_Linear(){
    	getCurrentTab().getController().getFig().setYAxis_to_Linear();
    }

    @FXML
    public void savePlot_asJPEG(){
    	getCurrentTab().getController().getFig().saveAsJPEG(640, 480);
    }

    @FXML
    public void savePlot_asPNG(){
    	getCurrentTab().getController().getFig().saveAsPNG(640, 480);
    }

    @FXML
    public void savePlot_asSVG(){
    	getCurrentTab().getController().getFig().saveAsSVG(640, 480);
    }

    @FXML
    public void gdsDesignerPressed(){
    	// cockpit must be run on a separate thread, otherwise "-XstartOnFirstThread" will not work...
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String packageString = "GDS" ;
				String classString = "GDS.Layout.Executable.Create_New_Cell" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", packageString, "-c", classString});
			}
    	} ;

    	EventQueue.invokeLater(r);
    }

    @FXML
    public void tabGDSPressed(){
    	getCurrentTab().getController().generateGDS();
    }

    @FXML
    public void setMarkerON(){
    	getCurrentTab().getController().getFig().markerON();
    }

    @FXML
    public void setMarkerOFF(){
    	getCurrentTab().getController().getFig().markerOFF();
    }


    @FXML TextField lineWidthTextField ;

    @FXML
    public void setLineWidth(){
    	String st = lineWidthTextField.getText() ;
    	int numPlots = getCurrentTab().getController().getFig().getNumberOfFigures() ;
    	for(int i=0; i<numPlots; i++){
    		getCurrentTab().getController().getFig().setFigLineWidth(i, Float.parseFloat(st));
    	}
    }

    @FXML TextField fontSizeTextField ;

    @FXML
    public void setFontSize(){
    	String st = fontSizeTextField.getText() ;
    	getCurrentTab().getController().getFig().setFontSize((int) MoreMath.evaluate(st));
    }

    @FXML
    public void plotDigitizerLinearXLinearY(){
    	FileChooserFX fc = new FileChooserFX() ;
    	fc.openFile();

    	Runnable r = new Runnable(){
			@Override
			public void run() {
				PlotDigitizer dig = new PlotDigitizer(fc, true, true) ;
				dig.setPreferredSize(new Dimension(640, 450));
				dig.digitize();
			}
    	} ;
    	EventQueue.invokeLater(r);
    }

    @FXML
    public void plotDigitizerLinearXLogY(){
    	FileChooserFX fc = new FileChooserFX() ;
    	fc.openFile();

    	Runnable r = new Runnable(){
			@Override
			public void run() {
				PlotDigitizer dig = new PlotDigitizer(fc, true, false) ;
				dig.setPreferredSize(new Dimension(640, 450));
				dig.digitize();
			}
    	} ;
    	EventQueue.invokeLater(r);
    }

    @FXML
    public void plotDigitizerLogXLogY(){
    	FileChooserFX fc = new FileChooserFX() ;
    	fc.openFile();

    	Runnable r = new Runnable(){
			@Override
			public void run() {
				PlotDigitizer dig = new PlotDigitizer(fc, false, false) ;
				dig.setPreferredSize(new Dimension(640, 450));
				dig.digitize();
			}
    	} ;
    	EventQueue.invokeLater(r);
    }

    @FXML
    public void plotDigitizerLogXLinearY(){
    	FileChooserFX fc = new FileChooserFX() ;
    	fc.openFile();

    	Runnable r = new Runnable(){
			@Override
			public void run() {
				PlotDigitizer dig = new PlotDigitizer(fc, false, true) ;
				dig.setPreferredSize(new Dimension(640, 450));
				dig.digitize();
			}
    	} ;
    	EventQueue.invokeLater(r);
    }

    @FXML
    public void saveSnapshot(){
        WritableImage image = tabs.getScene().snapshot(null);
        FileChooserFX fc = new FileChooserFX() ;
        fc.addExtension("png");
        fc.saveFile();
        File file = fc.getSelectedFile() ;
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
        }
    }

    @FXML
    public void setTabsClosingPolicy(){
    	ListChangeListener<Tab> listener = new ListChangeListener<Tab>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Tab> c) {
				c.next() ;
				if(c.getRemovedSize() != 0){
					Tab closedTab = c.getRemoved().get(0) ;
					orderedTabs.remove(closedTab) ;
				}
			}
		};
    	if(tabClosingSetting.isSelected()){
    		tabs.setTabClosingPolicy(TabClosingPolicy.ALL_TABS);
    		tabs.getTabs().addListener(listener) ;
    	}
    	else{
    		tabs.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    		tabs.getTabs().removeListener(listener);
    	}

    }


    // setting up the workspace directory
    @FXML
    public void setDefaultWorkspacePressed(){
    	FileChooserFX fc = new FileChooserFX() ;
    	fc.setExtension("confsim");
    	fc.saveFile();
    	String path = fc.getDirectoryPath() ;
    	FileChooserFX.path = path ;
    	CustomJFileChooser.path = path ;
    	// changing the default path of cockpit
    	ObjectConstuctionTreeModel.DEFAULT_FILE_NAME = path + File.separator + "tree.conf" ;
    	// create the config file
    	FileOutput fo = new FileOutput(fc.getFilePath()) ;
    	fo.println(fc.getDirectoryPath());
    	fo.close();
    	// update status bar
    	StatusBar statbar = new StatusBar() ;
    	statbar.setText("Workspace is set to: " + path);
    	updateStatusBar(statbar);
    }

    @FXML
    public void loadConfigFilePressed(){
    	// loading the config file
    	FileChooserFX fc = new FileChooserFX() ;
    	fc.setExtension("confsim");
    	fc.openFile();
    	if(fc.getSelectedFile() != null){
        	// reading the config file
        	FileInput fi = new FileInput(fc.getFilePath()) ;
        	// update the status bar
        	StatusBar statbar = new StatusBar() ;
        	statbar.setText("Config file loaded: " + fc.getFilePath());
        	updateStatusBar(statbar);
        	// applying the config file line by line
        	String path = fi.readLine() ;
        	FileChooserFX.path = path ;
        	// changing the default path of cockpit
        	ObjectConstuctionTreeModel.DEFAULT_FILE_NAME = path + File.separator + "tree.conf" ;
    	}
    }

    //********* utilities menu ***********

    @FXML
    public void sceneBuilderPressed(){
		try {
			File file = new File(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/programs/scenebuilder-all-8.3.0-all.jar").toURI()) ;
			Desktop.getDesktop().open(file);
		} catch (Exception e) {
			// TODO: handle exception
		}
    }

    @FXML
    public void pythonInstallerPressed(){
    	if(OSDetector.isMac()){
    		try {
    			File file = new File(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/programs/python-3.5.0-macosx10.6.pkg").toURI()) ;
    			Desktop.getDesktop().open(file);
    		} catch (Exception e) {
    			// TODO: handle exception
    		}
    	}
    	else if(OSDetector.isWindows()){
    		try {
    			File file = new File(Object.class.getClass().getResource("/People/Meisam/GUI/Utilities/programs/python-3.5.0-amd64.exe").toURI()) ;
    			Desktop.getDesktop().open(file);
    		} catch (Exception e) {
    			// TODO: handle exception
    		}
    	}
    }

	//********* general tabs addition ***********

    private void addSimulationTab(AbstractTab simulationTab){
		tabs.getTabs().add(simulationTab.getTab()) ;
		tabs.getSelectionModel().select(simulationTab.getTab());
		openTabs.put(simulationTab.getTab(), simulationTab) ;
		orderedTabs.add(simulationTab.getTab()) ;
		// updating status bar
		updateStatusBar(simulationTab.getStatusBar());
    }

	//********* adding tabs library *************

    @FXML
    public void addRingWgCouplerSiNTab() {
    	RingWgCouplerSiNTab X = new RingWgCouplerSiNTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
    public void addStripWgCoupledModesSiNTab() {
    	StripWgCoupledModesSiNTab X = new StripWgCoupledModesSiNTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
    public void addStripWgModesSiNTab() {
    	StripWgModesSiNTab X = new StripWgModesSiNTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
    public void addAllPassRingSelfHeatingTab() {
    	AllPassRingSelfHeatingTab X = new AllPassRingSelfHeatingTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
    public void addAddDropRingFifthhOrderTab() {
    	AddDropRingFifthOrderTab X = new AddDropRingFifthOrderTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
    public void addAddDropRingFourthOrderTab() {
    	AddDropRingFourthOrderTab X = new AddDropRingFourthOrderTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
    public void addAddDropRingThirdOrderTab() {
    	AddDropRingThirdOrderTab X = new AddDropRingThirdOrderTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
    public void addAddDropRingBSTab() {
    	AddDropRingBSTab X = new AddDropRingBSTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
    public void addAddDropRingSecondOrderTab() {
    	AddDropRingSecondOrderTab X = new AddDropRingSecondOrderTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
    public void addPINacFittingTab() {
    	PINacFittingTab X = new PINacFittingTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
    public void addPINResShiftFittingTab() {
    	PINResShiftFittingTab X = new PINResShiftFittingTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addRaceTrackCouplerTab(){
    	RaceTrackCouplerTab X = new RaceTrackCouplerTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addAddDropFittingSymmetricBSDoubleReflectorTab(){
    	AddDropFittingSymmetricBSDoubleReflectorTab X = new AddDropFittingSymmetricBSDoubleReflectorTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addDownSamplingTab(){
    	DownSamplingTab X = new DownSamplingTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addRingRingCouplerTab(){
    	RingRingCouplerTab X = new RingRingCouplerTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addAddDropFittingSymmetricBSTab(){
    	AddDropFittingSymmetricBSTab X = new AddDropFittingSymmetricBSTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addAddDropRingTab(){
    	AddDropRingTab X = new AddDropRingTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addPINdcFittingTab(){
    	PINdcFittingTab X = new PINdcFittingTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addBERPenaltyTab(){
    	BERPenaltyTab X = new BERPenaltyTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addAddDropFittingSymmetricThruTab(){
    	AddDropFittingSymmetricThruTab X = new AddDropFittingSymmetricThruTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addAllPassFittingTab(){
    	AllPassFittingTab X = new AllPassFittingTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addDataSmoothingTab(){
    	DataSmoothingTab X = new DataSmoothingTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

    @FXML
	public void addHeaterFreqFittingTab(){
    	HeaterFreqFittingTab X = new HeaterFreqFittingTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addHeaterDCFittingTab(){
		HeaterDCFittingTab X = new HeaterDCFittingTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addAddDropFittingSymmetricTab(){
		AddDropFittingSymmetricTab X = new AddDropFittingSymmetricTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addStripWgSensitivityTab(){
		StripWgSensitivityTab X = new StripWgSensitivityTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addHeaterWelcomeTab(){
		HeaterWelcomeTab X = new HeaterWelcomeTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addCouplerDesignerWelcomeTab(){
		CouplerDesignerWelcomeTab X = new CouplerDesignerWelcomeTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addStripWgModeProfileTab(){
		StripWgModeProfileTab X = new StripWgModeProfileTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addMaterialsTab(){
		MaterialsTab X = new MaterialsTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addInterfaceTab(){
		InterfaceTab X = new InterfaceTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addSlabWgCoupledModeProfileTab(){
		SlabWgCoupledModeProfileTab X = new SlabWgCoupledModeProfileTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addSlabWgModeProfileTab(){
		SlabWgModeProfileTab X = new SlabWgModeProfileTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addSlabWgCoupledModesTab(){
		SlabWgCoupledModesTab X = new SlabWgCoupledModesTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addSlabWgModesTab(){
		SlabWgModesTab X = new SlabWgModesTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addHeaterPWMTab(){
		HeaterPWMTab X = new HeaterPWMTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addHeaterSinTab(){
		HeaterSinTab X = new HeaterSinTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addHeaterStepTab(){
		HeaterStepTab X = new HeaterStepTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addHeaterImpulseTab(){
		HeaterImpulseTab X = new HeaterImpulseTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addHeaterFreqTab(){
		HeaterFreqTab X = new HeaterFreqTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addHeaterDCTab(){
		HeaterDCTab X = new HeaterDCTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addRingWgCouplerTab(){
		RingWgCouplerTab X = new RingWgCouplerTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addWgWgCouplerTab(){
		WgWgCouplerTab X = new WgWgCouplerTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addStripWgCoupledModesTab(){
		StripWgCoupledModesTab X = new StripWgCoupledModesTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addStripWgModesTab(){
		StripWgModesTab X = new StripWgModesTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	@FXML
	public void addWelcomeTab(){
		WelcomeTab X = new WelcomeTab(simDataBase) ;
		tabs.getTabs().add(X.getTab()) ;
		tabs.getSelectionModel().select(X.getTab());
		openTabs.put(X.getTab(), X) ;
		orderedTabs.add(X.getTab()) ;
		// updating status bar
		updateStatusBar(X.getStatusBar());
	}

	//********* adding Tree Library ***********

	@FXML
	public void benes8x8MZIPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements" ;
				String classString = "People.Qixiang.Benes.Benes8x8.Benes8x8MZISwitch_qixiang" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void benes8x8MZIMappingPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements" ;
				String classString = "People.Qixiang.Mapping.Benes8x8MZISwitch_AllConfig_RMSE" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void benes4x4MZIMappingPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements" ;
				String classString = "People.Qixiang.Mapping.Benes4x4MZISwitch_AllConfig_RMSE" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void benes4x4MZIPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements" ;
				String classString = "Simulations.switches.benes.Benes4x4MZISwitch" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void addDropDesignSpaceRingGapPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements" ;
				String classString = "Simulations.rings.addDrop.TestAddDropDesignSpaceGap" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void addDropSpectrumRadiusKappaPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements" ;
				String classString = "Simulations.rings.addDrop.AddDropFirstOrderSim" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void sipLinkDesignPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;edu.columbia.lrl;" ;
				String classString = "edu.columbia.lrl.CrossLayer.PhysicalLayoutExperiment" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void MZI2x2SwitchPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;" ;
				String classString = "Simulations.switches.thesis.Switch2x2MZISim" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void adrBSSymmetricPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;" ;
				String classString = "Simulations.rings.backscattering.AddDropBackScatteringAnalyticalSim" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void adrBSAsymmetricPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;" ;
				String classString = "Simulations.rings.backscattering.AddDropBackScatteringAsymmetricSim" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void heaterPhotoconductance(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;" ;
				String classString = "Simulations.heater.HeaterPhotoConductanceModel" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void addDropFirstOrderSFG(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;" ;
				String classString = "Simulations.rings.addDrop.sfg.AddDropFirstOrderSim" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void addDropSecondOrderSFG(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;" ;
				String classString = "Simulations.rings.addDrop.sfg.AddDropSecondOrderSim" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void addDropThirdOrderSFG(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;" ;
				String classString = "Simulations.rings.addDrop.sfg.AddDropThirdOrderSim" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void addDropFourthOrderSFG(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;" ;
				String classString = "Simulations.rings.addDrop.sfg.AddDropFourthOrderSim" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void addDropFifthOrderSFG(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;" ;
				String classString = "Simulations.rings.addDrop.sfg.AddDropFifthOrderSim" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void addDropSixthOrderSFG(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;" ;
				String classString = "Simulations.rings.addDrop.sfg.AddDropSixthOrderSim" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void addDropSeventhOrderSFG(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;" ;
				String classString = "Simulations.rings.addDrop.sfg.AddDropSeventhOrderSim" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void interconnectSFG(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements;edu.lrl.interconnectSFG;" ;
				String classString = "edu.lrl.interconnectSFG.Interconnect" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	//********* HPE Tree Library **************
	@FXML
	public void hpeAddDropRingPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "hpe.edu.lrl;" ;
				String classString = "hpe.edu.lrl.simulations.TestAddDropRingSFG" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void hpeWaveguidesPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "hpe.edu.lrl;" ;
				String classString = "hpe.edu.lrl.simulations.HPEWaveguides" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void hpeRaceTrackCouplerPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "hpe.edu.lrl;" ;
				String classString = "hpe.edu.lrl.simulations.RaceTrackCoupler" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void hpeAddDropBSPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "hpe.edu.lrl;" ;
				String classString = "hpe.edu.lrl.simulations.AddDropRingBackScatteringSFGAsym" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void hpeAddDropClosedFormPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "hpe.edu.lrl;" ;
				String classString = "hpe.edu.lrl.simulations.TestAddDropRingClosedForm" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}

	@FXML
	public void modeSlabWgTEPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements" ;
				String classString = "Simulations.effectiveIndexMethod.slabWg.TestModeSlabWgTE" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}
	
	@FXML
	public void modeSlabWgTMPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements" ;
				String classString = "Simulations.effectiveIndexMethod.slabWg.TestModeSlabWgTM" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}
	
	@FXML
	public void modeStripWgTEPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements" ;
				String classString = "Simulations.effectiveIndexMethod.stripWg.TestModeStripWgTE" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}
	
	@FXML
	public void modeStripWgTMPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "PhotonicElements" ;
				String classString = "Simulations.effectiveIndexMethod.stripWg.TestModeStripWgTM" ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}
	
	//*******************NASA project******************************
	
	@FXML
	public void allPassRingNASAPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "postdoc" ;
				String classString = RingWgSpectrum.class.getName() ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}
	
	@FXML
	public void addDropRingNASAPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "postdoc" ;
				String classString = AddDropSpectrum.class.getName() ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}
	
	@FXML
	public void addAllPassRingQContoursPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "postdoc" ;
				String classString = AllPassQContour.class.getName() ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}
	
	@FXML
	public void addAllPassRingERContoursPressed(){
    	Runnable r = new Runnable(){
			@Override
			public void run() {
				String pacakgeString = "postdoc" ;
				String classString = AllPassERContour.class.getName() ;
				ExperimentConfigurationCockpit.main(new String[]{"-p", pacakgeString, "-c", classString});
				AbstractResultsDisplayer.showGUI = true ;
			}
    	} ;
    	EventQueue.invokeLater(r);
	}


	//*****************************************

	@FXML
	public void addEmptyModuleDontShareDataBase(){
		new PhoenixSimModule() ;
	}

	@FXML
	public void addEmptyModuleShareDataBase(){
		new PhoenixSimModule(simDataBase) ;
	}

	//********* adding Modules library ***********

	@FXML
	public void thermalAnalyzerModulePressed(){
		(new ModuleCreator(simDataBase)).createThermalAnalyzer();
	}

	@FXML
	public void couplerDesignerModulePressed(){
		(new ModuleCreator(simDataBase)).createCouplerDesigner();
	}

	//*******************************************

	@FXML
	public void closeCurrentTabPressed(){
		orderedTabs.remove(tabs.getSelectionModel().getSelectedItem()) ;
		tabs.getTabs().remove(tabs.getSelectionModel().getSelectedItem()) ;
		updateOpenTabStatusBar();
	}

	@FXML
	public void closeAllTabs(){
		tabs.getTabs().removeAll(tabs.getTabs()) ;
		orderedTabs.clear();
	}

	@FXML
	public void closeOtherTabs(){
//		tabs.getTabs().removeAll(tabs.getTabs()) ;
//		orderedTabs.clear();
	}

	//********exporting open tabs as a stand-alone module******

	@FXML
	public void exportAsModule() throws IOException{

		FileChooserFX fc = new FileChooserFX() ;
		fc.setExtension("module");
		fc.saveFile();
		String fullPath = fc.getFilePath() ;

		FileOutput fout = new FileOutput(fullPath, "w") ;
		for(Tab x : orderedTabs){
			fout.println(getTab(x).getName());
		}
		fout.close();
		new ExportCompleted() ;
	}

	@FXML
	public void importAsModule(){
		// first remove all current tabs
		tabs.getTabs().removeAll(orderedTabs) ;
		openTabs.clear();
		orderedTabs.clear();
		// now open the file selection
		FileChooserFX fc = new FileChooserFX() ;
		fc.setExtension("module");
		fc.openFile();
		String fullPath = fc.getFilePath() ;
		// now read from file
		FileInput fin = new FileInput(fullPath) ;
		int M = fin.numberOfLines() ;
		String[] tabNames = new String[M] ;
		for(int i=0; i<M; i++){
			tabNames[i] = fin.readLine() ;
		}
		fin.close();
		// now create tabs based on their names
		for(String x : tabNames){
			createTab(x);
		}


	}


	//******getting the right type of current open tab******

	@SuppressWarnings("unchecked")
	private <T extends AbstractTab> T getCurrentTab(){
		return (T) openTabs.get(tabs.getSelectionModel().getSelectedItem()) ;
	}

	@SuppressWarnings("unchecked")
	private <T extends AbstractTab> T getTab(Tab tab){
		return (T) openTabs.get(tab) ;
	}

	//******saving tabs and exporting as module**********

	private void createTab(String tabName){
		switch(tabName){
			// simulation tabs
			case "WelcomeTab": {addWelcomeTab(); break;}
			case "StripWgModesTab": {addStripWgModesTab(); break;}
			case "StripWgCoupledModesTab": {addStripWgCoupledModesTab(); break;}
			case "RingWgCouplerTab": {addRingWgCouplerTab(); break;}
			case "WgWgCouplerTab": {addWgWgCouplerTab(); break;}
			case "RaceTrackCouplerTab": {addRaceTrackCouplerTab(); break;}
			case "HeaterDCTab": {addHeaterDCTab(); break;}
			case "HeaterFreqTab": {addHeaterFreqTab(); break;}
			case "HeaterImpulseTab": {addHeaterImpulseTab(); break;}
			case "HeaterStepTab": {addHeaterStepTab(); break;}
			case "HeaterSinTab": {addHeaterSinTab(); break;}
			case "HeaterPWMTab": {addHeaterPWMTab(); break;}
			case "SlabWgModesTab": {addSlabWgModesTab(); break;}
			case "SlabWgCoupledModesTab": {addSlabWgCoupledModesTab(); break;}
			case "SlabWgModeProfileTab": {addSlabWgModeProfileTab(); break;}
			case "SlabWgCoupledModeProfileTab": {addSlabWgCoupledModeProfileTab(); break;}
			case "InterfaceTab": {addInterfaceTab(); break;}
			case "MaterialsTab": {addMaterialsTab(); break;}
			case "StripWgModeProfileTab": {addStripWgModeProfileTab(); break;}
			case "CouplerDesignerWelcomeTab": {addCouplerDesignerWelcomeTab(); break;}
			case "HeaterWelcomeTab": {addHeaterWelcomeTab(); break;}
			case "StripWgSensitivityTab": {addStripWgSensitivityTab(); break;}
			case "AddDropRingTab": {addAddDropRingTab(); break;}
			case "AddDropRingSecondOrderTab": {addAddDropRingTab(); break;}
			case "AddDropRingBSTab": {addAddDropRingBSTab(); break;}
			case "AddDropRingThirdOrderTab": {addAddDropRingThirdOrderTab(); break;}
			case "AddDropRingFourthOrderTab": {addAddDropRingFourthOrderTab(); break;}
			case "AddDropRingFifthOrderTab": {addAddDropRingFifthhOrderTab(); break;}
			case "AllPassRingSelfHeatingTab": {addAddDropRingFifthhOrderTab(); break;}
			case "StripWgModesSiNTab": {addStripWgModesSiNTab(); break;}
			case "StripWgCoupledModesSiNTab": {addStripWgCoupledModesSiNTab(); break;}
			case "RingWgCouplerSiNTab": {addRingWgCouplerSiNTab(); break;}
			// measurement toolset
			case "HeaterDCFittingTab": {addHeaterDCFittingTab(); break;}
			case "HeaterFreqFittingTab": {addHeaterFreqFittingTab(); break;}
			case "DataSmoothingTab": {addDataSmoothingTab(); break;}
			case "AddDropFittingSymmetricTab": {addAddDropFittingSymmetricTab(); break;}
			case "AddDropFittingSymmetricBSTab": {addAddDropFittingSymmetricBSTab(); break;}
			case "AllPassFittingTab": {addAllPassFittingTab(); break;}
			case "AddDropFittingSymmetricThruTab": {addAddDropFittingSymmetricThruTab(); break;}
			case "BERPenaltyTab": {addBERPenaltyTab(); break;}
			case "PINdcFittingTab": {addPINdcFittingTab(); break;}
			case "DownSamplingTab": {addDownSamplingTab(); break;}
			case "AddDropFittingSymmetricBSDoubleReflectorTab": {addAddDropFittingSymmetricBSDoubleReflectorTab(); break;}
			case "PINResShiftFittingTab": {addPINResShiftFittingTab(); break;}
			case "PINacFittingTab": {addPINacFittingTab(); break;}
			// default case
			default : {break;}
		}

	}


}
