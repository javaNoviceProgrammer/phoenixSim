package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import PhotonicElements.EffectiveIndexMethod.ModeProfile.SlabWg.ProfileSlabWgTE;
import PhotonicElements.EffectiveIndexMethod.ModeProfile.SlabWg.ProfileSlabWgTM;
import PhotonicElements.EffectiveIndexMethod.Structures.SlabWg;
import PhotonicElements.Materials.Dielectric.Silica;
import PhotonicElements.Materials.Dielectric.Silicon;
import PhotonicElements.Utilities.Wavelength;
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import mathLib.plot.MatlabChart;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.PlotterController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;

public class SlabWgModeProfileTabController extends AbstractTabController {

    SimulationDataBase simDataBase = new SimulationDataBase() ;
    MatlabChart figWgModeProfile ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

	@FXML Tab slabWgModeProfileTab ;
	@FXML Pane matlabPane ;
	@FXML CheckBox normalizedField ;
	@FXML RadioButton ReEx ;
	@FXML RadioButton ImEx ;
	@FXML RadioButton ReEy ;
	@FXML RadioButton ImEy ;
	@FXML RadioButton ReEz ;
	@FXML RadioButton ImEz ;
	@FXML RadioButton ReHx ;
	@FXML RadioButton ImHx ;
	@FXML RadioButton ReHy ;
	@FXML RadioButton ImHy ;
	@FXML RadioButton ReHz ;
	@FXML RadioButton ImHz ;

	@FXML
	public void initialize(){
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        figWgModeProfile = fig ;
        showPlot(fig, matlabPane);
        matlabPane.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<none>");
	}

	@FXML
	private void plotField(){
		// first read neff and width from database
		double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
		double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
		String mode = simDataBase.getVariable("mode").getAlias() ;
		SlabWg slab = new SlabWg(new Wavelength(lambda_nm[0]), width_nm[0], new Silica(), new Silicon(), new Silica()) ;
		double[] neff = simDataBase.getVariable("neff_()").getAllValues() ;
		SimulationVariable x_nm = new SimulationVariable("x_(nm)", "X (nm)", MoreMath.linspace(-5*width_nm[0], 5*width_nm[0], 5000))  ;
		simDataBase.addNewVariable(x_nm);
		double[] field = new double[x_nm.getLength()] ;
		if(ReEx.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ex_field().re() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ex_field().re() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Re_Ex_normalized = new SimulationVariable("Re_Ex_normalized", "Re(Ex)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Re_Ex_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Ex_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Re_Ex = new SimulationVariable("Re_Ex", "Re(Ex)", field) ;
				simDataBase.addNewVariable(Re_Ex);
				figWgModeProfile = createPlot(x_nm, Re_Ex) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}
		else if(ImEx.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ex_field().im() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ex_field().im() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Im_Ex_normalized = new SimulationVariable("Im_Ex_normalized", "Im(Ex)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Im_Ex_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Ex_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Im_Ex = new SimulationVariable("Im_Ex", "Im(Ex)", field) ;
				simDataBase.addNewVariable(Im_Ex);
				figWgModeProfile = createPlot(x_nm, Im_Ex) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}
		else if(ReEy.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ey_field().re() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ey_field().re() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Re_Ey_normalized = new SimulationVariable("Re_Ey_normalized", "Re(Ey)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Re_Ey_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Ey_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Re_Ey = new SimulationVariable("Re_Ey", "Re(Ey)", field) ;
				simDataBase.addNewVariable(Re_Ey);
				figWgModeProfile = createPlot(x_nm, Re_Ey) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}
		else if(ImEy.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ey_field().im() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ey_field().im() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Im_Ey_normalized = new SimulationVariable("Im_Ey_normalized", "Im(Ey)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Im_Ey_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Ey_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Im_Ey = new SimulationVariable("Im_Ey", "Im(Ey)", field) ;
				simDataBase.addNewVariable(Im_Ey);
				figWgModeProfile = createPlot(x_nm, Im_Ey) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}
		else if(ReEz.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ez_field().re() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ez_field().re() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Re_Ez_normalized = new SimulationVariable("Re_Ez_normalized", "Re(Ez)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Re_Ez_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Ez_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Re_Ez = new SimulationVariable("Re_Ez", "Re(Ez)", field) ;
				simDataBase.addNewVariable(Re_Ez);
				figWgModeProfile = createPlot(x_nm, Re_Ez) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}
		else if(ImEz.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ez_field().im() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Ez_field().im() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Im_Ez_normalized = new SimulationVariable("Im_Ez_normalized", "Im(Ez)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Im_Ez_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Ez_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Im_Ez = new SimulationVariable("Im_Ez", "Im(Ez)", field) ;
				simDataBase.addNewVariable(Im_Ez);
				figWgModeProfile = createPlot(x_nm, Im_Ez) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}
		else if(ReHx.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hx_field().re() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hx_field().re() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Re_Hx_normalized = new SimulationVariable("Re_Hx_normalized", "Re(Hx)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Re_Hx_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Hx_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Re_Hx = new SimulationVariable("Re_Hx", "Re(Hx)", field) ;
				simDataBase.addNewVariable(Re_Hx);
				figWgModeProfile = createPlot(x_nm, Re_Hx) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}
		else if(ImHx.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hx_field().im() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hx_field().im() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Im_Hx_normalized = new SimulationVariable("Im_Hx_normalized", "Im(Hx)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Im_Hx_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Hx_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Im_Hx = new SimulationVariable("Im_Hx", "Im(Hx)", field) ;
				simDataBase.addNewVariable(Im_Hx);
				figWgModeProfile = createPlot(x_nm, Im_Hx) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}
		else if(ReHy.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hy_field().re() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hy_field().re() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Re_Hy_normalized = new SimulationVariable("Re_Hy_normalized", "Re(Hy)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Re_Hy_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Hy_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Re_Hy = new SimulationVariable("Re_Hy", "Re(Hy)", field) ;
				simDataBase.addNewVariable(Re_Hy);
				figWgModeProfile = createPlot(x_nm, Re_Hy) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}
		else if(ImHy.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hy_field().im() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hy_field().im() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Im_Hy_normalized = new SimulationVariable("Im_Hy_normalized", "Im(Hy)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Im_Hy_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Hy_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Im_Hy = new SimulationVariable("Im_Hy", "Im(Hy)", field) ;
				simDataBase.addNewVariable(Im_Hy);
				figWgModeProfile = createPlot(x_nm, Im_Hy) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}
		else if(ReHz.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hz_field().re() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hz_field().re() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Re_Hz_normalized = new SimulationVariable("Re_Hz_normalized", "Re(Hz)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Re_Hz_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Hz_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Re_Hz = new SimulationVariable("Re_Hz", "Re(Hz)", field) ;
				simDataBase.addNewVariable(Re_Hz);
				figWgModeProfile = createPlot(x_nm, Re_Hz) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}
		else if(ImHz.isSelected()){
			for(int i=0; i<x_nm.getLength(); i++){
				if(mode.contains("TE")){
					ProfileSlabWgTE profile = new ProfileSlabWgTE(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hz_field().im() ;
				}
				else if(mode.contains("TM")){
					ProfileSlabWgTM profile = new ProfileSlabWgTM(slab, x_nm.getValue(i), neff[0]) ;
					field[i] = profile.get_Hz_field().im() ;
				}
			}
			if(normalizedField.isSelected()){
				double norm = MoreMath.Arrays.FindMaximum.getValue(MoreMath.Arrays.Functions.abs(field)) ;
				SimulationVariable Im_Hz_normalized = new SimulationVariable("Im_Hz_normalized", "Im(Hz)-Normalized", MoreMath.Arrays.times(field, 1/norm)) ;
				simDataBase.addNewVariable(Im_Hz_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Hz_normalized) ;
				showPlot(figWgModeProfile, matlabPane);
			}
			else{
				SimulationVariable Im_Hz = new SimulationVariable("Im_Hz", "Im(Hz)", field) ;
				simDataBase.addNewVariable(Im_Hz);
				figWgModeProfile = createPlot(x_nm, Im_Hz) ;
				showPlot(figWgModeProfile, matlabPane);
			}

		}

	}

//    private void showPlot(MatlabChart fig, Pane pane){
//        int width = 500, height = 400 ;
//        pane.getChildren().remove(fig.getChartSwingNode(width, height)) ;
//        pane.getChildren().add(fig.getChartSwingNode(width, height)) ;
//        pane.setPrefSize((double) width, (double) height);
//    }

    private MatlabChart createPlot(SimulationVariable x, SimulationVariable y){
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x.getAllValues(), y.getAllValues());
        double[] field = y.getAllValues() ;
		double[] x0 = MoreMath.Arrays.setValue(0, 10) ;
		double[] y0 = MoreMath.linspace(MoreMath.Arrays.FindMinimum.getValue(field), MoreMath.Arrays.FindMaximum.getValue(field), x0.length) ;
		fig.plot(x0, y0, ".r");
		double[] x1 = MoreMath.Arrays.setValue(simDataBase.getVariable("width_(nm)").getAllValues()[0], 10) ;
		double[] y1 = MoreMath.linspace(MoreMath.Arrays.FindMinimum.getValue(field), MoreMath.Arrays.FindMaximum.getValue(field), x1.length) ;
		fig.plot(x1, y1, ".r");
        fig.RenderPlot();
        fig.xlabel(x.getAlias());
        fig.ylabel(y.getAlias());
        return fig ;
    }

    @FXML
    public void exportToMatlabPressed() throws IOException {
    	figWgModeProfile.exportToMatlab();
    }

    @FXML
    public void openInPlotterPressed() throws IOException {
        FXMLLoader loader = new FXMLLoader(Object.class.getClass().getResource("/People/Meisam/GUI/Plotters/MainGUI/plotter.fxml")) ;
        WindowBuilder plotter = new WindowBuilder(loader) ;
        plotter.setIcon("/People/Meisam/GUI/Plotters/MainGUI/Extras/plotter.png");
        plotter.build("Plotter v0.5 Beta", true);
        PlotterController controller = (PlotterController) plotter.getController() ;
        controller.setDataBase(simDataBase);
        controller.initialize();
    }

	@Override
	public Tab getTab() {
		return slabWgModeProfileTab ;
	}

	@Override
	public MatlabChart getFig() {
		return figWgModeProfile;
	}

	@Override
	public StatusBar getStatusBar() {
		return statusBar;
	}

	@Override
	public void generateGDS() {
		// TODO Auto-generated method stub

	}

}
