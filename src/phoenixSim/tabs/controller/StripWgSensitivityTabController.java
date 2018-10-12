package phoenixSim.tabs.controller;

import org.controlsfx.control.StatusBar;

import PhotonicElements.EffectiveIndexMethod.ModeSensitivity.StripWg.NeffVariationStripWgTE;
import PhotonicElements.EffectiveIndexMethod.ModeSensitivity.StripWg.NeffVariationStripWgTM;
import PhotonicElements.Utilities.Wavelength;
import PhotonicElements.Utilities.MathLibraries.MoreMath;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import mathLib.plot.MatlabChart;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;

public class StripWgSensitivityTabController extends AbstractTabController {

    // defining simulation database and the figures in the simulation
    SimulationDataBase simDataBase = new SimulationDataBase() ;

    public MatlabChart fig  ;

    public void setSimDataBase(SimulationDataBase simDataBase){
        this.simDataBase = simDataBase ;
    }

    public SimulationDataBase getSimDataBase(){
        return simDataBase ;
    }

    StatusBar statusBar = new StatusBar() ;

    @FXML Tab tab ;
    @FXML Pane matlabPlot ;
    @FXML RadioButton dneffdlambda ;
    @FXML RadioButton dneffdw ;
    @FXML RadioButton dneffdh ;
    @FXML RadioButton dneffdnSi ;
    @FXML RadioButton dneffdnSiO2 ;
    @FXML RadioButton dngdlambda ;
    @FXML RadioButton dngdw ;
    @FXML RadioButton dngdh ;
    @FXML RadioButton dngdnSi ;
    @FXML RadioButton dngdnSiO2 ;
    @FXML ToggleGroup modePlot ;

	@FXML
	public void initialize() {
        // initialize plot
        double[] x = {} ;
        double[] y = {} ;
        MatlabChart fig = new MatlabChart() ;
        fig.plot(x, y);
        fig.RenderPlot();
        fig.xlabel("");
        fig.ylabel("");
        this.fig = fig ;
        showPlot(fig, matlabPlot);
        matlabPlot.getChildren().add(swingNode) ;
        // status bar
        statusBar.setText("Dependencies:<none>");
	}

	@FXML
	public void plotSensitivity(){
		String mode = simDataBase.getVariable("mode").getAlias() ;
		int m_index = (int) simDataBase.getVariable("mode").getValue(0) ;
		int n_index = (int) simDataBase.getVariable("mode").getValue(1) ;
		double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues() ;
		double[] height_nm = simDataBase.getVariable("height_(nm)").getAllValues() ;
		double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues() ;
		if(mode.contains("TE")){
			if(dneffdlambda.isSelected()){
				double[] dneff_dlambda = new double[lambda_nm.length] ;
				double dlambda = 1e-1 ;
				for(int i=0; i<lambda_nm.length; i++){
					NeffVariationStripWgTE neffVar0 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[i]), width_nm[0], height_nm[0], 0, 0, 0, 0) ;
					double neff0 = neffVar0.getNeffOriginal() ;
					NeffVariationStripWgTE neffVar1 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[i]+dlambda), width_nm[0], height_nm[0], 0, 0, 0, 0) ;
					double neff1 = neffVar1.getNeffOriginal() ;
					dneff_dlambda[i] = (neff1-neff0)/dlambda ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dneffdlambda_(/nm)", "dNeff/dLambda (/nm)", dneff_dlambda));
	            fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("dneffdlambda_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dneffdw.isSelected()){
				double[] dneff_dw = new double[width_nm.length] ;
				double dw = 1 ;
				for(int i=0; i<width_nm.length; i++){
					NeffVariationStripWgTE neffVar = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[i], height_nm[0], dw, 0, 0, 0) ;
					dneff_dw[i] = neffVar.getDNeff()/dw ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dneffdw_(/nm)", "dNeff/dw (/nm)", dneff_dw));
	            fig = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("dneffdw_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dneffdh.isSelected()){
				double[] dneff_dh = new double[height_nm.length] ;
				double dh = 1 ;
				for(int i=0; i<height_nm.length; i++){
					NeffVariationStripWgTE neffVar = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[i], 0, dh, 0, 0) ;
					dneff_dh[i] = neffVar.getDNeff()/dh ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dneffdh_(/nm)", "dNeff/dh (/nm)", dneff_dh));
	            fig = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("dneffdh_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dneffdnSi.isSelected()){
				double[] dnSi = MoreMath.linspace(-1e-2, 1e-2, 20) ;
				double[] dneff_dnSi = new double[dnSi.length] ;
				double[] dneff = new double[dnSi.length] ;
				double dn = 1e-3 ;
				for(int i=0; i<dnSi.length; i++){
					NeffVariationStripWgTE neffVar0 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, dnSi[i], 0) ;
					double neff0 = neffVar0.getNeffPerturbed() ;
					NeffVariationStripWgTE neffVar1 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, dnSi[i]+dn, 0) ;
					double neff1 = neffVar1.getNeffPerturbed() ;
					dneff_dnSi[i] = (neff1-neff0)/dn ;
					dneff[i] = neffVar0.getNeffPerturbed()-neffVar0.getNeffOriginal() ;
				}
				simDataBase.addNewVariable(new SimulationVariable("dnSi_()", "dnSi", dnSi));
				simDataBase.addNewVariable(new SimulationVariable("dneff_dnSi_()", "dNeff", dneff));
	            simDataBase.addNewVariable(new SimulationVariable("dneffdnSi_()", "dNeff/dnSi", dneff_dnSi));
	            fig = createPlot(simDataBase.getVariable("dnSi_()"), simDataBase.getVariable("dneffdnSi_()")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dneffdnSiO2.isSelected()){
				double[] dnSiO2 = MoreMath.linspace(-1e-2, 1e-2, 20) ;
				double[] dneff_dnSiO2 = new double[dnSiO2.length] ;
				double[] dneff = new double[dnSiO2.length] ;
				double dn = 1e-3 ;
				for(int i=0; i<dnSiO2.length; i++){
					NeffVariationStripWgTE neffVar0 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, 0, dnSiO2[i]) ;
					double neff0 = neffVar0.getNeffPerturbed() ;
					NeffVariationStripWgTE neffVar1 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, 0, dnSiO2[i]+dn) ;
					double neff1 = neffVar1.getNeffPerturbed() ;
					dneff_dnSiO2[i] = (neff1-neff0)/dn ;
					dneff[i] = neffVar0.getNeffPerturbed()-neffVar0.getNeffOriginal() ;
				}
				simDataBase.addNewVariable(new SimulationVariable("dnSiO2_()", "dnSiO2", dnSiO2));
				simDataBase.addNewVariable(new SimulationVariable("dneff_dnSiO2_()", "dNeff", dneff));
	            simDataBase.addNewVariable(new SimulationVariable("dneffdnSiO2_()", "dNeff/dnSiO2", dneff_dnSiO2));
	            fig = createPlot(simDataBase.getVariable("dnSiO2_()"), simDataBase.getVariable("dneffdnSiO2_()")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dngdlambda.isSelected()){
				double[] dng_dlambda = new double[lambda_nm.length] ;
				double dlambda = 2 ;
				for(int i=0; i<lambda_nm.length; i++){
					NeffVariationStripWgTE neffVar0 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[i]), width_nm[0], height_nm[0], 0, 0, 0, 0) ;
					double ng0 = neffVar0.getNgOriginal() ;
					NeffVariationStripWgTE neffVar1 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[i]+dlambda), width_nm[0], height_nm[0], 0, 0, 0, 0) ;
					double ng1 = neffVar1.getNgOriginal() ;
					dng_dlambda[i] = (ng1-ng0)/dlambda ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dngdlambda_(/nm)", "dNg/dLambda (/nm)", dng_dlambda));
	            fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("dngdlambda_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dngdw.isSelected()){
				double[] dng_dw = new double[width_nm.length] ;
				double dw = 1 ;
				for(int i=0; i<width_nm.length; i++){
					NeffVariationStripWgTE neffVar = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[i], height_nm[0], dw, 0, 0, 0) ;
					dng_dw[i] = neffVar.getDNg()/dw ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dngdw_(/nm)", "dNg/dw (/nm)", dng_dw));
	            fig = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("dngdw_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dngdh.isSelected()){
				double[] dng_dh = new double[height_nm.length] ;
				double dh = 1 ;
				for(int i=0; i<height_nm.length; i++){
					NeffVariationStripWgTE neffVar = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[i], 0, dh, 0, 0) ;
					dng_dh[i] = neffVar.getDNg()/dh ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dngdh_(/nm)", "dNg/dh (/nm)", dng_dh));
	            fig = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("dngdh_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dngdnSi.isSelected()){
				double[] dnSi = MoreMath.linspace(-1e-2, 1e-2, 20) ;
				double[] dng_dnSi = new double[dnSi.length] ;
				double[] dng = new double[dnSi.length] ;
				double dn = 1e-3 ;
				for(int i=0; i<dnSi.length; i++){
					NeffVariationStripWgTE neffVar0 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, dnSi[i], 0) ;
					double neff0 = neffVar0.getNeffPerturbed() ;
					NeffVariationStripWgTE neffVar1 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, dnSi[i]+dn, 0) ;
					double neff1 = neffVar1.getNeffPerturbed() ;
					dng_dnSi[i] = (neff1-neff0)/dn ;
					dng[i] = neff1-neff0 ;
				}
				simDataBase.addNewVariable(new SimulationVariable("dnSi_()", "dnSi", dnSi));
	            simDataBase.addNewVariable(new SimulationVariable("dngdnSi_()", "dNg/dnSi", dng_dnSi));
	            simDataBase.addNewVariable(new SimulationVariable("dng_dnSi_()", "dNg", dng));
	            fig = createPlot(simDataBase.getVariable("dnSi_()"), simDataBase.getVariable("dngdnSi_()")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dngdnSiO2.isSelected()){
				double[] dnSiO2 = MoreMath.linspace(-1e-2, 1e-2, 20) ;
				double[] dng_dnSiO2 = new double[dnSiO2.length] ;
				double[] dng = new double[dnSiO2.length] ;
				double dn = 1e-3 ;
				for(int i=0; i<dnSiO2.length; i++){
					NeffVariationStripWgTE neffVar0 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, 0, dnSiO2[i]) ;
					double neff0 = neffVar0.getNeffPerturbed() ;
					NeffVariationStripWgTE neffVar1 = new NeffVariationStripWgTE(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, 0, dnSiO2[i]+dn) ;
					double neff1 = neffVar1.getNeffPerturbed() ;
					dng_dnSiO2[i] = (neff1-neff0)/dn ;
					dng[i] = neff1-neff0 ;
				}
				simDataBase.addNewVariable(new SimulationVariable("dnSiO2_()", "dnSiO2", dnSiO2));
				simDataBase.addNewVariable(new SimulationVariable("dng_dnSiO2_()", "dNg", dng));
	            simDataBase.addNewVariable(new SimulationVariable("dngdnSiO2_()", "dNg/dnSiO2", dng_dnSiO2));
	            fig = createPlot(simDataBase.getVariable("dnSiO2_()"), simDataBase.getVariable("dngdnSiO2_()")) ;
	            showPlot(fig, matlabPlot);
			}

		}
		else if(mode.contains("TM")){
			if(dneffdlambda.isSelected()){
				double[] dneff_dlambda = new double[lambda_nm.length] ;
				double dlambda = 1e-1 ;
				for(int i=0; i<lambda_nm.length; i++){
					NeffVariationStripWgTM neffVar0 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[i]), width_nm[0], height_nm[0], 0, 0, 0, 0) ;
					double neff0 = neffVar0.getNeffOriginal() ;
					NeffVariationStripWgTM neffVar1 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[i]+dlambda), width_nm[0], height_nm[0], 0, 0, 0, 0) ;
					double neff1 = neffVar1.getNeffOriginal() ;
					dneff_dlambda[i] = (neff1-neff0)/dlambda ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dneffdlambda_(/nm)", "dNeff/dLambda (/nm)", dneff_dlambda));
	            fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("dneffdlambda_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dneffdw.isSelected()){
				double[] dneff_dw = new double[width_nm.length] ;
				double dw = 1 ;
				for(int i=0; i<width_nm.length; i++){
					NeffVariationStripWgTM neffVar = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[i], height_nm[0], dw, 0, 0, 0) ;
					dneff_dw[i] = neffVar.getDNeff()/dw ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dneffdw_(/nm)", "dNeff/dw (/nm)", dneff_dw));
	            fig = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("dneffdw_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dneffdh.isSelected()){
				double[] dneff_dh = new double[height_nm.length] ;
				double dh = 1 ;
				for(int i=0; i<height_nm.length; i++){
					NeffVariationStripWgTM neffVar = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[i], 0, dh, 0, 0) ;
					dneff_dh[i] = neffVar.getDNeff()/dh ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dneffdh_(/nm)", "dNeff/dh (/nm)", dneff_dh));
	            fig = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("dneffdh_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dneffdnSi.isSelected()){
				double[] dnSi = MoreMath.linspace(-1e-2, 1e-2, 20) ;
				double[] dneff_dnSi = new double[dnSi.length] ;
				double[] dneff = new double[dnSi.length] ;
				double dn = 1e-3 ;
				for(int i=0; i<dnSi.length; i++){
					NeffVariationStripWgTM neffVar0 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, dnSi[i], 0) ;
					double neff0 = neffVar0.getNeffPerturbed() ;
					NeffVariationStripWgTM neffVar1 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, dnSi[i]+dn, 0) ;
					double neff1 = neffVar1.getNeffPerturbed() ;
					dneff_dnSi[i] = (neff1-neff0)/dn ;
					dneff[i] = neffVar0.getNeffPerturbed()-neffVar0.getNeffOriginal() ;
				}
				simDataBase.addNewVariable(new SimulationVariable("dnSi_()", "dnSi", dnSi));
				simDataBase.addNewVariable(new SimulationVariable("dneff_dnSi_()", "dNeff", dneff));
	            simDataBase.addNewVariable(new SimulationVariable("dneffdnSi_()", "dNeff/dnSi", dneff_dnSi));
	            fig = createPlot(simDataBase.getVariable("dnSi_()"), simDataBase.getVariable("dneffdnSi_()")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dneffdnSiO2.isSelected()){
				double[] dnSiO2 = MoreMath.linspace(-1e-2, 1e-2, 20) ;
				double[] dneff_dnSiO2 = new double[dnSiO2.length] ;
				double[] dneff = new double[dnSiO2.length] ;
				double dn = 1e-3 ;
				for(int i=0; i<dnSiO2.length; i++){
					NeffVariationStripWgTM neffVar0 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, 0, dnSiO2[i]) ;
					double neff0 = neffVar0.getNeffPerturbed() ;
					NeffVariationStripWgTM neffVar1 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, 0, dnSiO2[i]+dn) ;
					double neff1 = neffVar1.getNeffPerturbed() ;
					dneff_dnSiO2[i] = (neff1-neff0)/dn ;
					dneff[i] = neffVar0.getNeffPerturbed()-neffVar0.getNeffOriginal() ;
				}
				simDataBase.addNewVariable(new SimulationVariable("dnSiO2_()", "dnSiO2", dnSiO2));
				simDataBase.addNewVariable(new SimulationVariable("dneff_dnSiO2_()", "dNeff", dneff));
	            simDataBase.addNewVariable(new SimulationVariable("dneffdnSiO2_()", "dNeff/dnSiO2", dneff_dnSiO2));
	            fig = createPlot(simDataBase.getVariable("dnSiO2_()"), simDataBase.getVariable("dneffdnSiO2_()")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dngdlambda.isSelected()){
				double[] dng_dlambda = new double[lambda_nm.length] ;
				double dlambda = 2 ;
				for(int i=0; i<lambda_nm.length; i++){
					NeffVariationStripWgTM neffVar0 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[i]), width_nm[0], height_nm[0], 0, 0, 0, 0) ;
					double ng0 = neffVar0.getNgOriginal() ;
					NeffVariationStripWgTM neffVar1 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[i]+dlambda), width_nm[0], height_nm[0], 0, 0, 0, 0) ;
					double ng1 = neffVar1.getNgOriginal() ;
					dng_dlambda[i] = (ng1-ng0)/dlambda ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dngdlambda_(/nm)", "dNg/dLambda (/nm)", dng_dlambda));
	            fig = createPlot(simDataBase.getVariable("lambda_(nm)"), simDataBase.getVariable("dngdlambda_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dngdw.isSelected()){
				double[] dng_dw = new double[width_nm.length] ;
				double dw = 1 ;
				for(int i=0; i<width_nm.length; i++){
					NeffVariationStripWgTM neffVar = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[i], height_nm[0], dw, 0, 0, 0) ;
					dng_dw[i] = neffVar.getDNg()/dw ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dngdw_(/nm)", "dNg/dw (/nm)", dng_dw));
	            fig = createPlot(simDataBase.getVariable("width_(nm)"), simDataBase.getVariable("dngdw_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dngdh.isSelected()){
				double[] dng_dh = new double[height_nm.length] ;
				double dh = 1 ;
				for(int i=0; i<height_nm.length; i++){
					NeffVariationStripWgTM neffVar = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[i], 0, dh, 0, 0) ;
					dng_dh[i] = neffVar.getDNg()/dh ;
				}
	            simDataBase.addNewVariable(new SimulationVariable("dngdh_(/nm)", "dNg/dh (/nm)", dng_dh));
	            fig = createPlot(simDataBase.getVariable("height_(nm)"), simDataBase.getVariable("dngdh_(/nm)")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dngdnSi.isSelected()){
				double[] dnSi = MoreMath.linspace(-1e-2, 1e-2, 20) ;
				double[] dng_dnSi = new double[dnSi.length] ;
				double[] dng = new double[dnSi.length] ;
				double dn = 1e-3 ;
				for(int i=0; i<dnSi.length; i++){
					NeffVariationStripWgTM neffVar0 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, dnSi[i], 0) ;
					double neff0 = neffVar0.getNeffPerturbed() ;
					NeffVariationStripWgTM neffVar1 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, dnSi[i]+dn, 0) ;
					double neff1 = neffVar1.getNeffPerturbed() ;
					dng_dnSi[i] = (neff1-neff0)/dn ;
					dng[i] = neff1-neff0 ;
				}
				simDataBase.addNewVariable(new SimulationVariable("dnSi_()", "dnSi", dnSi));
	            simDataBase.addNewVariable(new SimulationVariable("dngdnSi_()", "dNg/dnSi", dng_dnSi));
	            simDataBase.addNewVariable(new SimulationVariable("dng_dnSi_()", "dNg", dng));
	            fig = createPlot(simDataBase.getVariable("dnSi_()"), simDataBase.getVariable("dngdnSi_()")) ;
	            showPlot(fig, matlabPlot);
			}
			else if(dngdnSiO2.isSelected()){
				double[] dnSiO2 = MoreMath.linspace(-1e-2, 1e-2, 20) ;
				double[] dng_dnSiO2 = new double[dnSiO2.length] ;
				double[] dng = new double[dnSiO2.length] ;
				double dn = 1e-3 ;
				for(int i=0; i<dnSiO2.length; i++){
					NeffVariationStripWgTM neffVar0 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, 0, dnSiO2[i]) ;
					double neff0 = neffVar0.getNeffPerturbed() ;
					NeffVariationStripWgTM neffVar1 = new NeffVariationStripWgTM(m_index, n_index, new Wavelength(lambda_nm[0]), width_nm[0], height_nm[0], 0, 0, 0, dnSiO2[i]+dn) ;
					double neff1 = neffVar1.getNeffPerturbed() ;
					dng_dnSiO2[i] = (neff1-neff0)/dn ;
					dng[i] = neff1-neff0 ;
				}
				simDataBase.addNewVariable(new SimulationVariable("dnSiO2_()", "dnSiO2", dnSiO2));
				simDataBase.addNewVariable(new SimulationVariable("dng_dnSiO2_()", "dNg", dng));
	            simDataBase.addNewVariable(new SimulationVariable("dngdnSiO2_()", "dNg/dnSiO2", dng_dnSiO2));
	            fig = createPlot(simDataBase.getVariable("dnSiO2_()"), simDataBase.getVariable("dngdnSiO2_()")) ;
	            showPlot(fig, matlabPlot);
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
        fig.RenderPlot();
        fig.xlabel(x.getAlias());
        fig.ylabel(y.getAlias());
        return fig ;
    }

	@Override
	public Tab getTab() {
		return tab;
	}

	@Override
	public MatlabChart getFig() {
		return fig;
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
