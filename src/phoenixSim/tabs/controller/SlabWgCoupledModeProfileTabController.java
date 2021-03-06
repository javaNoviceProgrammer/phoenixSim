package phoenixSim.tabs.controller;

import java.io.IOException;

import org.controlsfx.control.StatusBar;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;
import phoenixSim.modules.ExportToMatlabModule;
import phoenixSim.modules.PlotterModule;
import phoenixSim.tabs.AbstractTabController;
import phoenixSim.util.SimulationDataBase;
import phoenixSim.util.SimulationVariable;
import photonics.material.Silica;
import photonics.material.Silicon;
import photonics.slab.CoupledSlabWg;
import photonics.slab.ProfileCoupledSlabWgTE;
import photonics.slab.ProfileCoupledSlabWgTM;
import photonics.util.Wavelength;

public class SlabWgCoupledModeProfileTabController extends AbstractTabController {

	SimulationDataBase simDataBase = new SimulationDataBase();
	MatlabChart figWgModeProfile;

	public void setSimDataBase(SimulationDataBase simDataBase) {
		this.simDataBase = simDataBase;
	}

	public SimulationDataBase getSimDataBase() {
		return simDataBase;
	}

	StatusBar statusBar = new StatusBar();

	@FXML
	Tab slabWgModeProfileTab;
	@FXML
	Pane matlabPane;
	@FXML
	CheckBox normalizedField;
	@FXML
	RadioButton ReEx;
	@FXML
	RadioButton ImEx;
	@FXML
	RadioButton ReEy;
	@FXML
	RadioButton ImEy;
	@FXML
	RadioButton ReEz;
	@FXML
	RadioButton ImEz;
	@FXML
	RadioButton ReHx;
	@FXML
	RadioButton ImHx;
	@FXML
	RadioButton ReHy;
	@FXML
	RadioButton ImHy;
	@FXML
	RadioButton ReHz;
	@FXML
	RadioButton ImHz;

	@FXML
	RadioButton evenMode;
	@FXML
	RadioButton oddMode;

	double[] neff;

	@FXML
	public void initialize() {
		// initialize plot
		double[] x = {};
		double[] y = {};
		MatlabChart fig = new MatlabChart();
		fig.plot(x, y);
		fig.renderPlot();
		fig.xlabel("");
		fig.ylabel("");
		figWgModeProfile = fig;
		showPlot(fig, matlabPane);
		matlabPane.getChildren().add(swingNode);
		// status bar
		statusBar.setText("Dependencies:<Slab Wg Coupled Modes>");

	}

	@FXML
	private void setMode() {
		plotField();
	}

	@FXML
	private void plotField() {
		// first read neff and width from database
		double[] width_nm = simDataBase.getVariable("width_(nm)").getAllValues();
		double[] lambda_nm = simDataBase.getVariable("lambda_(nm)").getAllValues();
		double[] gap_nm = simDataBase.getVariable("gap_(nm)").getAllValues();
		String mode = simDataBase.getVariable("mode").getAlias();
		Silicon si = new Silicon();
		Silica sio2 = new Silica();
		Wavelength inputLambda = new Wavelength(lambda_nm[0]);
		CoupledSlabWg coupledSlab = new CoupledSlabWg(width_nm[0], width_nm[0], gap_nm[0], sio2.getIndex(inputLambda),
				si.getIndex(inputLambda), sio2.getIndex(inputLambda), si.getIndex(inputLambda),
				sio2.getIndex(inputLambda));
		double[] neff = this.neff;
		if (evenMode.isSelected()) {
			neff = simDataBase.getVariable("neff_even_()").getAllValues();
		} else if (oddMode.isSelected()) {
			neff = simDataBase.getVariable("neff_odd_()").getAllValues();
		}
		SimulationVariable x_nm = new SimulationVariable("x_(nm)", "X (nm)",
				MathUtils.linspace(-5 * width_nm[0], 5 * width_nm[0], 5000));
		simDataBase.addNewVariable(x_nm);
		if (ReEx.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ex_field().re();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ex_field().re();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Re_Ex_normalized = new SimulationVariable("Re_Ex_normalized", "Re(Ex)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Re_Ex_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Ex_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Re_Ex = new SimulationVariable("Re_Ex", "Re(Ex)", field);
				simDataBase.addNewVariable(Re_Ex);
				figWgModeProfile = createPlot(x_nm, Re_Ex);
				showPlot(figWgModeProfile, matlabPane);
			}

		} else if (ImEx.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ex_field().im();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ex_field().im();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Im_Ex_normalized = new SimulationVariable("Im_Ex_normalized", "Im(Ex)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Im_Ex_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Ex_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Im_Ex = new SimulationVariable("Im_Ex", "Im(Ex)", field);
				simDataBase.addNewVariable(Im_Ex);
				figWgModeProfile = createPlot(x_nm, Im_Ex);
				showPlot(figWgModeProfile, matlabPane);
			}

		} else if (ReEy.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ey_field().re();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ey_field().re();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Re_Ey_normalized = new SimulationVariable("Re_Ey_normalized", "Re(Ey)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Re_Ey_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Ey_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Re_Ey = new SimulationVariable("Re_Ey", "Re(Ey)", field);
				simDataBase.addNewVariable(Re_Ey);
				figWgModeProfile = createPlot(x_nm, Re_Ey);
				showPlot(figWgModeProfile, matlabPane);
			}

		} else if (ImEy.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ey_field().im();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ey_field().im();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Im_Ey_normalized = new SimulationVariable("Im_Ey_normalized", "Im(Ey)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Im_Ey_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Ey_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Im_Ey = new SimulationVariable("Im_Ey", "Im(Ey)", field);
				simDataBase.addNewVariable(Im_Ey);
				figWgModeProfile = createPlot(x_nm, Im_Ey);
				showPlot(figWgModeProfile, matlabPane);
			}

		} else if (ReEz.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ez_field().re();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ez_field().re();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Re_Ez_normalized = new SimulationVariable("Re_Ez_normalized", "Re(Ez)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Re_Ez_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Ez_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Re_Ez = new SimulationVariable("Re_Ez", "Re(Ez)", field);
				simDataBase.addNewVariable(Re_Ez);
				figWgModeProfile = createPlot(x_nm, Re_Ez);
				showPlot(figWgModeProfile, matlabPane);
			}

		} else if (ImEz.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ez_field().im();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Ez_field().im();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Im_Ez_normalized = new SimulationVariable("Im_Ez_normalized", "Im(Ez)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Im_Ez_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Ez_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Im_Ez = new SimulationVariable("Im_Ez", "Im(Ez)", field);
				simDataBase.addNewVariable(Im_Ez);
				figWgModeProfile = createPlot(x_nm, Im_Ez);
				showPlot(figWgModeProfile, matlabPane);
			}

		} else if (ReHx.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hx_field().re();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hx_field().re();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Re_Hx_normalized = new SimulationVariable("Re_Hx_normalized", "Re(Hx)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Re_Hx_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Hx_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Re_Hx = new SimulationVariable("Re_Hx", "Re(Hx)", field);
				simDataBase.addNewVariable(Re_Hx);
				figWgModeProfile = createPlot(x_nm, Re_Hx);
				showPlot(figWgModeProfile, matlabPane);
			}

		} else if (ImHx.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hx_field().im();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hx_field().im();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Im_Hx_normalized = new SimulationVariable("Im_Hx_normalized", "Im(Hx)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Im_Hx_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Hx_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Im_Hx = new SimulationVariable("Im_Hx", "Im(Hx)", field);
				simDataBase.addNewVariable(Im_Hx);
				figWgModeProfile = createPlot(x_nm, Im_Hx);
				showPlot(figWgModeProfile, matlabPane);
			}

		} else if (ReHy.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hy_field().re();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hy_field().re();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Re_Hy_normalized = new SimulationVariable("Re_Hy_normalized", "Re(Hy)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Re_Hy_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Hy_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Re_Hy = new SimulationVariable("Re_Hy", "Re(Hy)", field);
				simDataBase.addNewVariable(Re_Hy);
				figWgModeProfile = createPlot(x_nm, Re_Hy);
				showPlot(figWgModeProfile, matlabPane);
			}

		} else if (ImHy.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hy_field().im();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hy_field().im();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Im_Hy_normalized = new SimulationVariable("Im_Hy_normalized", "Im(Hy)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Im_Hy_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Hy_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Im_Hy = new SimulationVariable("Im_Hy", "Im(Hy)", field);
				simDataBase.addNewVariable(Im_Hy);
				figWgModeProfile = createPlot(x_nm, Im_Hy);
				showPlot(figWgModeProfile, matlabPane);
			}

		} else if (ReHz.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hz_field().re();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hz_field().re();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Re_Hz_normalized = new SimulationVariable("Re_Hz_normalized", "Re(Hz)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Re_Hz_normalized);
				figWgModeProfile = createPlot(x_nm, Re_Hz_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Re_Hz = new SimulationVariable("Re_Hz", "Re(Hz)", field);
				simDataBase.addNewVariable(Re_Hz);
				figWgModeProfile = createPlot(x_nm, Re_Hz);
				showPlot(figWgModeProfile, matlabPane);
			}

		} else if (ImHz.isSelected()) {
			double[] field = new double[x_nm.getLength()];
			for (int i = 0; i < x_nm.getLength(); i++) {
				if (mode.contains("TE")) {
					ProfileCoupledSlabWgTE profile = new ProfileCoupledSlabWgTE(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hz_field().im();
				} else if (mode.contains("TM")) {
					ProfileCoupledSlabWgTM profile = new ProfileCoupledSlabWgTM(inputLambda, coupledSlab, neff[0],
							x_nm.getValue(i));
					field[i] = profile.get_Hz_field().im();
				}
			}
			if (normalizedField.isSelected()) {
				double norm = MathUtils.Arrays.FindMaximum.getValue(MathUtils.Arrays.Functions.abs(field));
				SimulationVariable Im_Hz_normalized = new SimulationVariable("Im_Hz_normalized", "Im(Hz)-Normalized",
						MathUtils.Arrays.times(field, 1 / norm));
				simDataBase.addNewVariable(Im_Hz_normalized);
				figWgModeProfile = createPlot(x_nm, Im_Hz_normalized);
				showPlot(figWgModeProfile, matlabPane);
			} else {
				SimulationVariable Im_Hz = new SimulationVariable("Im_Hz", "Im(Hz)", field);
				simDataBase.addNewVariable(Im_Hz);
				figWgModeProfile = createPlot(x_nm, Im_Hz);
				showPlot(figWgModeProfile, matlabPane);
			}

		}

	}

	private MatlabChart createPlot(SimulationVariable x, SimulationVariable y) {
		MatlabChart fig = new MatlabChart();
		fig.plot(x.getAllValues(), y.getAllValues());
		double[] field = y.getAllValues();
		double[] x0 = MathUtils.Arrays.setValue(0, 10);
		double[] y0 = MathUtils.linspace(MathUtils.Arrays.FindMinimum.getValue(field),
				MathUtils.Arrays.FindMaximum.getValue(field), x0.length);
		fig.plot(x0, y0, ".r");
		double[] x1 = MathUtils.Arrays.setValue(simDataBase.getVariable("width_(nm)").getAllValues()[0], 10);
		double[] y1 = MathUtils.linspace(MathUtils.Arrays.FindMinimum.getValue(field),
				MathUtils.Arrays.FindMaximum.getValue(field), x1.length);
		fig.plot(x1, y1, ".r");
		double x2_pos = simDataBase.getVariable("width_(nm)").getAllValues()[0]
				+ simDataBase.getVariable("gap_(nm)").getValue(0);
		double[] x2 = MathUtils.Arrays.setValue(x2_pos, 10);
		double[] y2 = MathUtils.linspace(MathUtils.Arrays.FindMinimum.getValue(field),
				MathUtils.Arrays.FindMaximum.getValue(field), x2.length);
		fig.plot(x2, y2, ".r");
		double x3_pos = x2_pos + simDataBase.getVariable("width_(nm)").getAllValues()[0];
		double[] x3 = MathUtils.Arrays.setValue(x3_pos, 10);
		double[] y3 = MathUtils.linspace(MathUtils.Arrays.FindMinimum.getValue(field),
				MathUtils.Arrays.FindMaximum.getValue(field), x1.length);
		fig.plot(x3, y3, ".r");
		fig.renderPlot();
		fig.xlabel(x.getAlias());
		fig.ylabel(y.getAlias());
		return fig;
	}

	@FXML
	public void exportToMatlabPressed() throws IOException {
		new ExportToMatlabModule(figWgModeProfile) ;
	}

	@FXML
	public void openInPlotterPressed() throws IOException {
		new PlotterModule(simDataBase);
	}

	@Override
	public Tab getTab() {
		return slabWgModeProfileTab;
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
