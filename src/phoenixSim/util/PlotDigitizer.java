package phoenixSim.util;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JFrame;

import flanagan.interpolation.CubicSpline;
import flanagan.io.FileChooser;
import flanagan.io.FileOutput;
import flanagan.math.Fmath;
import mathLib.plot.MatlabChart;

public class PlotDigitizer extends Canvas implements MouseListener {
	private static final long serialVersionUID = 1L;
	private Image pic = null;
	private String imagePath = null;
	private String imageName = null;
	private String outputFile = null;
	private FileOutput fout = null;
	private int trunc = 16;
	private String path = FileChooserFX.path ;
	private int windowWidth = 0;
	private int windowHeight = 0;
	private int xPos = 0;
	private int yPos = 0;
	private int button = 0;
	private int sumX = 0;
	private int sumY = 0;
	private int iSum = 0;
	private double lowYvalue = 0.0D;
	private double lowYaxisXpixel = 0.0D;
	private double lowYaxisYpixel = 0.0D;
	private double highYvalue = 0.0D;
	private double highYaxisXpixel = 0.0D;
	private double highYaxisYpixel = 0.0D;
	private double lowXvalue = 0.0D;
	private double lowXaxisXpixel = 0.0D;
	private double lowXaxisYpixel = 0.0D;
	private double highXvalue = 0.0D;
	private double highXaxisXpixel = 0.0D;
	private double highXaxisYpixel = 0.0D;
	private ArrayList<Integer> xAndYvalues = new ArrayList<Integer>();
	private int iCounter = 0;
	private double angleXaxis = 0.0D;
	private double angleYaxis = 0.0D;
	private double angleMean = 0.0D;
	private double angleTolerance = 0.0D;
	private boolean rotationDone = false;
	private double[] xPosPixel = null;
	private double[] yPosPixel = null;
	private double[] xPositions = null;
	private double[] yPositions = null;
	private int nData = 0;
	private int nInterpPoints = 0;
	private boolean interpOpt = false;
	private double[] xInterp = null;
	private double[] yInterp = null;
	private boolean noIdentical = true;
	private boolean digitizationDone = false;
	private boolean noYlow = true;
	private boolean noXlow = true;
	private boolean noYhigh = true;
	private boolean noXhigh = true;
	private boolean resize = false;
	private JFrame window = new JFrame("Plot Digitizer");

	FileChooserFX fc ;
	private boolean xAxisIsLinear, yAxisIsLinear ;

	public PlotDigitizer() {
		this.setWindowSize();
		this.selectImage();
		this.setImage();
		this.outputFileChoice();
		this.addMouseListener(this);
	}

	public PlotDigitizer(String arg0) {
		this.setWindowSize();
		this.path = arg0;
		this.selectImage();
		this.setImage();
		this.outputFileChoice();
		this.addMouseListener(this);
	}

	public PlotDigitizer(FileChooserFX fc) {
		this.setWindowSize();
		this.selectImage(fc);
		this.setImage();
		this.outputFileChoice();
		this.addMouseListener(this);
		this.fc = fc ;
	}

	public PlotDigitizer(FileChooserFX fc, boolean xAxisIsLinear, boolean yAxisIsLinear) {
		this.setWindowSize();
		this.selectImage(fc);
		this.setImage();
		this.outputFileChoice();
		this.addMouseListener(this);
		this.fc = fc ;
		this.xAxisIsLinear = xAxisIsLinear ;
		this.yAxisIsLinear = yAxisIsLinear ;
	}

	private void setWindowSize() {
		Dimension arg0 = Toolkit.getDefaultToolkit().getScreenSize();
		this.windowWidth = arg0.width - 30;
		this.windowHeight = arg0.height - 40;
	}

	private void selectImage() {
		String arg0 = null;

		try {
			InetAddress arg1 = InetAddress.getLocalHost();
			arg0 = arg1.getHostName();
		} catch (UnknownHostException arg3) {
			System.err.println("Cannot detect local host : " + arg3);
		}

		if (arg0.equals("name")) {
			this.path = "C:\\DigiGraphDirectory";
		}

		FileChooser arg4 = new FileChooser(this.path);
		this.imageName = arg4.selectFile();
		if (!arg4.fileFound()) {
			System.out.println("Class DigiGraph: No successful selection of an image file occurred");
			System.exit(0);
		}

		this.imagePath = arg4.getPathName();
	}

	private void selectImage(FileChooserFX fc) {
		String arg0 = null;

		try {
			InetAddress arg1 = InetAddress.getLocalHost();
			arg0 = arg1.getHostName();
		} catch (UnknownHostException arg3) {
			System.err.println("Cannot detect local host : " + arg3);
		}
		if (arg0.equals("name")) {
			this.path = FileChooserFX.path ;
		}
		this.imageName = fc.getFileName_NoExtension() ;
		this.imagePath = fc.getFilePath() ;
	}

	private void setImage() {
		this.pic = Toolkit.getDefaultToolkit().getImage(this.imagePath);
	}

	private void outputFileChoice() {
		int arg0 = this.imagePath.lastIndexOf(46);
		this.outputFile = this.imagePath.substring(0, arg0) + "_digitized.txt";
		this.outputFile = Db.readLine("Enter output file name ", this.outputFile);
		this.fout = new FileOutput(this.outputFile);
		this.trunc = Db.readInt("Enter number of decimal places required in output data ", this.trunc);
	}

	public void setTruncation(int arg0) {
		this.trunc = arg0;
	}

	public void setRotationTolerance(double arg0) {
		this.angleTolerance = arg0;
	}

	public void setPath(String arg0) {
		this.path = arg0;
	}

	public void setWindowHeight(int arg0) {
		this.windowHeight = arg0;
	}

	public void setWindowWidth(int arg0) {
		this.windowWidth = arg0;
	}

	public void keepIdenticalPoints() {
		this.noIdentical = false;
	}

	public void paint(Graphics arg0) {
		this.graph(arg0);
	}

	public void digitize() {
		this.window.setSize(this.windowWidth, this.windowHeight);
		this.window.getContentPane().setBackground(Color.white);
		this.window.getContentPane().add("Center", this);
		this.window.pack();
		this.window.setResizable(true);
		this.window.toFront();
		this.window.setVisible(true);
	}

	public void digitise() {
		this.digitize();
	}

	private void graph(Graphics arg0) {
		arg0.drawImage(this.pic, 10, 30, this);
		if (!this.resize) {
			arg0.drawString("RIGHT click anywhere on the screen", 5, 10);
			int arg1 = this.pic.getWidth((ImageObserver) null);
			int arg2 = this.pic.getHeight((ImageObserver) null);
			System.out.println(arg1 + " xxx " + arg2);
			arg0.drawString("  ", 5, 10);
			double arg3 = (double) (this.windowHeight - 30) / (double) arg2;
			if ((int) ((double) arg1 * arg3) > this.windowWidth - 10) {
				arg3 = (double) (this.windowWidth - 10) / (double) arg1;
			}

			arg2 = (int) ((double) (arg2 - 30) * arg3 * 0.95D);
			arg1 = (int) ((double) (arg1 - 10) * arg3 + 0.95D);
			this.pic = this.pic.getScaledInstance(arg1, arg2, 1);
			arg0.drawImage(this.pic, 10, 30, this);
			this.resize = true;
		}

		boolean arg5 = true;
		if (this.xPos == 0 && this.yPos == 0) {
			arg5 = false;
		}

		if (arg5) {
			this.cursorDoneSign(arg0, this.xPos, this.yPos);
		}

		if (!this.digitizationDone) {
			switch (this.iCounter) {
				case 0 :
					arg0.drawString("RIGHT click on lower Y-axis calibration point", 5, 10);
					break;
				case 1 :
					if (this.noYlow) {
						this.lowYvalue = Db.readDouble("Enter lower Y-axis calibration value");
						this.noYlow = false;
					}

					arg0.drawString("RIGHT click on higher Y-axis calibration point", 5, 10);
					break;
				case 2 :
					if (this.noYhigh) {
						this.highYvalue = Db.readDouble("Enter higher Y-axis calibration value");
						this.noYhigh = false;
					}

					arg0.drawString("RIGHT click on lower X-axis calibration point", 5, 10);
					break;
				case 3 :
					if (this.noXlow) {
						this.lowXvalue = Db.readDouble("Enter lower X-axis calibration value");
						this.noXlow = false;
					}

					arg0.drawString("RIGHT click on higher X-axis calibration point", 5, 10);
					break;
				case 4 :
					if (this.noXhigh) {
						this.highXvalue = Db.readDouble("Enter higher X-axis calibration value");
						this.noXhigh = false;
					}

					arg0.drawString("LEFT click on points to be digitized [right click when finished digitizing]", 5,
							10);
					break;
				default :
					arg0.drawString("LEFT click on points to be digitized [right click when finished digitizing]", 5,
							10);
			}
		} else {
			arg0.drawString("You may now close this window", 5, 10);
		}

	}

	private void cursorDoneSign(Graphics arg0, int arg1, int arg2) {
		arg0.setColor(Color.BLACK);
		arg0.fillOval(arg1 - 3, arg2 - 3, 6, 6);
		arg0.setColor(Color.RED);
		arg0.drawLine(arg1 - 5, arg2, arg1 + 5, arg2);
		arg0.drawLine(arg1, arg2 - 5, arg1, arg2 + 5);
		arg0.setColor(Color.BLACK);
	}

	public void mouseClicked(MouseEvent arg0) {
		if (!this.digitizationDone) {
			switch (this.iCounter) {
				case 0 :
					this.xPos = arg0.getX();
					this.yPos = arg0.getY();
					this.button = arg0.getButton();
					if (this.button == 1) {
						this.sumX += this.xPos;
						this.sumY += this.yPos;
						++this.iSum;
					} else if (this.button == 3) {
						this.sumX += this.xPos;
						this.sumY += this.yPos;
						++this.iSum;
						this.lowYaxisXpixel = (double) this.sumX / (double) this.iSum;
						this.lowYaxisYpixel = (double) this.windowHeight - (double) this.sumY / (double) this.iSum;
						++this.iCounter;
						this.sumX = 0;
						this.sumY = 0;
						this.iSum = 0;
					}
					break;
				case 1 :
					this.xPos = arg0.getX();
					this.yPos = arg0.getY();
					this.button = arg0.getButton();
					if (this.button == 1) {
						this.sumX += this.xPos;
						this.sumY += this.yPos;
						++this.iSum;
					} else if (this.button == 3) {
						this.sumX += this.xPos;
						this.sumY += this.yPos;
						++this.iSum;
						this.highYaxisXpixel = (double) this.sumX / (double) this.iSum;
						this.highYaxisYpixel = (double) this.windowHeight - (double) this.sumY / (double) this.iSum;
						++this.iCounter;
						this.sumX = 0;
						this.sumY = 0;
						this.iSum = 0;
					}
					break;
				case 2 :
					this.xPos = arg0.getX();
					this.yPos = arg0.getY();
					this.button = arg0.getButton();
					if (this.button == 1) {
						this.sumX += this.xPos;
						this.sumY += this.yPos;
						++this.iSum;
					} else if (this.button == 3) {
						this.sumX += this.xPos;
						this.sumY += this.yPos;
						++this.iSum;
						this.lowXaxisXpixel = (double) this.sumX / (double) this.iSum;
						this.lowXaxisYpixel = (double) this.windowHeight - (double) this.sumY / (double) this.iSum;
						++this.iCounter;
						this.sumX = 0;
						this.sumY = 0;
						this.iSum = 0;
					}
					break;
				case 3 :
					this.xPos = arg0.getX();
					this.yPos = arg0.getY();
					this.button = arg0.getButton();
					new PixelGrabber(this.pic, this.xPos, this.yPos, 1, 1, false);
					if (this.button == 1) {
						this.sumX += this.xPos;
						this.sumY += this.yPos;
						++this.iSum;
					} else if (this.button == 3) {
						this.sumX += this.xPos;
						this.sumY += this.yPos;
						++this.iSum;
						this.highXaxisXpixel = (double) this.sumX / (double) this.iSum;
						this.highXaxisYpixel = (double) this.windowHeight - (double) this.sumY / (double) this.iSum;
						++this.iCounter;
						this.sumX = 0;
						this.sumY = 0;
						this.iSum = 0;
					}
					break;
				default :
					this.xPos = arg0.getX();
					this.yPos = arg0.getY();
					this.button = arg0.getButton();
					if (this.button == 1) {
						this.xAndYvalues.add(new Integer(this.xPos));
						this.xAndYvalues.add(new Integer(this.yPos));
					}

					if (this.button == 3 && this.xAndYvalues.size() / 2 != 0) {
						this.outputData();
						this.digitizationDone = true;
					}
			}
		}

		this.repaint();
	}

	private void calculateDataPoints(){
		this.nData = this.xAndYvalues.size() / 2;
		System.out.println("nData " + this.nData);
		this.xPositions = new double[this.nData];
		this.yPositions = new double[this.nData];
		this.xPosPixel = new double[this.nData];
		this.yPosPixel = new double[this.nData];
		int arg0 = 0;

		int arg1;
		int arg3;
		for (arg1 = 0; arg1 < this.nData; ++arg1) {
			int arg2 = ((Integer) this.xAndYvalues.get(arg0)).intValue();
			++arg0;
			arg3 = ((Integer) this.xAndYvalues.get(arg0)).intValue();
			++arg0;
			this.xPosPixel[arg1] = (double) arg2;
			this.yPosPixel[arg1] = (double) this.windowHeight - (double) arg3;
		}

		this.checkForRotation();

		for (arg1 = 0; arg1 < this.nData; ++arg1) {
			this.xPositions[arg1] = this.lowXvalue + (this.xPosPixel[arg1] - this.lowXaxisXpixel)
					* (this.highXvalue - this.lowXvalue) / (this.highXaxisXpixel - this.lowXaxisXpixel);
			this.yPositions[arg1] = this.lowYvalue + (this.yPosPixel[arg1] - this.lowYaxisYpixel)
					* (this.highYvalue - this.lowYvalue) / (this.highYaxisYpixel - this.lowYaxisYpixel);
		}

		if (this.noIdentical) {
			this.checkForIdenticalPoints();
		}

		String arg4 = "Do you wish to increase number of data points\n";
		arg4 = arg4 + "using cubic spline interpolation?";
		boolean arg5 = Db.noYes(arg4);
		if (arg5) {
			this.nInterpPoints = Db.readInt("Enter number of interpolation points", 200);
			this.interpolation();
			this.interpOpt = true;
		}

		setXAxisType(xAxisIsLinear);
		setYAxisType(yAxisIsLinear);

		if(interpOpt){
			interpolation();
			plotInterpolationPoints();
		}
		else{
			plotDigitisedPoints();
		}
	}

	private void outputData() {
		calculateDataPoints();
		printToFile_NoHeader();
		System.gc();
		this.fout.close();
	}

	@SuppressWarnings("unused")
	private void printToFile(){
		this.fout.println("Digitization Output of Plot (PhoenixSim Toolkit V 1.0)");
		this.fout.println();
		this.fout.dateAndTimeln();
		this.fout.println();
		this.fout.println("Image used in the digitization:                 " + this.imageName);
		this.fout.println("Location of the image used in the digitization: " + this.imagePath);
		this.fout.println();
		this.fout.println("X-axis skew angle    " + Fmath.truncate(this.angleXaxis, 4) + " degrees");
		this.fout.println("Y-axis skew angle    " + Fmath.truncate(this.angleYaxis, 4) + " degrees");
		this.fout.println("Axes mean skew angle " + Fmath.truncate(this.angleMean, 4) + " degrees");
		if (this.rotationDone) {
			this.fout.println("Axes and all points rotated to bring axes to normal position");
		} else {
			this.fout.println("No rotation of axes or points performed");
		}

		this.fout.println();
		this.fout.println("Number of digitized points: " + this.nData);
		this.fout.println();
		this.fout.printtab("X-value");
		this.fout.println("Y-value");

		for (int arg3 = 0; arg3 < this.nData; ++arg3) {
			this.fout.printtab(Fmath.truncate(this.xPositions[arg3], this.trunc));
			this.fout.println(Fmath.truncate(this.yPositions[arg3], this.trunc));
		}

		this.fout.println();
		if (this.interpOpt) {
			this.fout.println();
			this.fout.println("Interpolated data (cubic spline)");
			this.fout.println();
			this.fout.println("Number of interpolated points: " + this.nInterpPoints);
			this.fout.println();
			this.fout.printtab("X-value");
			this.fout.println("Y-value");

			for (int arg3 = 0; arg3 < this.nInterpPoints; ++arg3) {
				this.fout.printtab(Fmath.truncate(this.xInterp[arg3], this.trunc));
				this.fout.println(Fmath.truncate(this.yInterp[arg3], this.trunc));
			}
		}
	}

	private void printToFile_NoHeader(){
		if (this.interpOpt) {
			this.fout.printtab("X-value");
			this.fout.println("Y-value");

			for (int arg3 = 0; arg3 < this.nInterpPoints; ++arg3) {
				this.fout.printtab(Fmath.truncate(this.xInterp[arg3], this.trunc));
				this.fout.println(Fmath.truncate(this.yInterp[arg3], this.trunc));
			}
		}
		else{
			this.fout.printtab("X-value");
			this.fout.println("Y-value");

			for (int arg3 = 0; arg3 < this.nData; ++arg3) {
				this.fout.printtab(Fmath.truncate(this.xPositions[arg3], this.trunc));
				this.fout.println(Fmath.truncate(this.yPositions[arg3], this.trunc));
			}
		}
	}

	private void checkForRotation() {
		double arg0 = (this.highYaxisXpixel - this.lowYaxisXpixel) / (this.highYaxisYpixel - this.lowYaxisYpixel);
		this.angleYaxis = Math.toDegrees(Math.atan(arg0));
		arg0 = (this.lowXaxisYpixel - this.highXaxisYpixel) / (this.highXaxisXpixel - this.lowXaxisXpixel);
		this.angleXaxis = Math.toDegrees(Math.atan(arg0));
		this.angleMean = (this.angleXaxis + this.angleYaxis) / 2.0D;
		double arg2 = Math.abs(this.angleMean);
		if (arg2 != 0.0D && arg2 > this.angleTolerance) {
			this.performRotation();
		}

	}

	private void performRotation() {
		double arg0 = (this.highXaxisYpixel - this.lowXaxisYpixel) / (this.highXaxisXpixel - this.lowXaxisXpixel);
		double arg2 = this.highXaxisYpixel - arg0 * this.highXaxisXpixel;
		double arg4 = (this.highYaxisYpixel - this.lowYaxisYpixel) / (this.highYaxisXpixel - this.lowYaxisXpixel);
		double arg6 = this.highYaxisYpixel - arg4 * this.highYaxisXpixel;
		double arg8 = (arg2 - arg6) / (arg4 - arg0);
		double arg10 = arg4 * arg8 + arg6;
		double arg12 = Math.toRadians(this.angleMean);
		double arg14 = Math.cos(-arg12);
		double arg16 = Math.sin(-arg12);
		double arg18 = (this.highXaxisXpixel - arg8) * arg14 + (this.highXaxisYpixel - arg10) * arg16 + arg8;
		double arg20 = -(this.highXaxisXpixel - arg8) * arg16 + (this.highXaxisYpixel - arg10) * arg14 + arg10;
		double arg22 = (this.lowXaxisXpixel - arg8) * arg14 + (this.lowXaxisYpixel - arg10) * arg16 + arg8;
		double arg24 = -(this.lowXaxisXpixel - arg8) * arg16 + (this.lowXaxisYpixel - arg10) * arg14 + arg10;
		double arg26 = (this.highYaxisXpixel - arg8) * arg14 + (this.highYaxisYpixel - arg10) * arg16 + arg8;
		double arg28 = -(this.highYaxisXpixel - arg8) * arg16 + (this.highYaxisYpixel - arg10) * arg14 + arg10;
		double arg30 = -(this.lowYaxisXpixel - arg8) * arg14 + (this.lowYaxisYpixel - arg10) * arg16 + arg8;
		double arg32 = (this.lowYaxisXpixel - arg8) * arg16 + (this.lowYaxisYpixel - arg10) * arg14 + arg10;
		this.highXaxisXpixel = arg18;
		this.highXaxisYpixel = arg20;
		this.lowXaxisXpixel = arg22;
		this.lowXaxisYpixel = arg24;
		this.highYaxisXpixel = arg26;
		this.highYaxisYpixel = arg28;
		this.lowYaxisXpixel = arg30;
		this.lowYaxisYpixel = arg32;

		for (int arg34 = 0; arg34 < this.nData; ++arg34) {
			double arg35 = (this.xPosPixel[arg34] - arg8) * arg14 + (this.yPosPixel[arg34] - arg10) * arg16 + arg8;
			double arg37 = -(this.xPosPixel[arg34] - arg8) * arg16 + (this.yPosPixel[arg34] - arg10) * arg14 + arg10;
			this.xPosPixel[arg34] = arg35;
			this.yPosPixel[arg34] = arg37;
		}

		this.rotationDone = true;
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
//		this.repaint();
	}

	public void mouseExited(MouseEvent arg0) {
//		this.repaint();
	}

	private void interpolation() {
		this.xInterp = new double[this.nInterpPoints];
		this.yInterp = new double[this.nInterpPoints];
		double arg0 = (this.xPositions[this.nData - 1] - this.xPositions[0]) / (double) (this.nInterpPoints - 1);
		this.xInterp[0] = this.xPositions[0];

		for (int arg2 = 1; arg2 < this.nInterpPoints - 1; ++arg2) {
			this.xInterp[arg2] = this.xInterp[arg2 - 1] + arg0;
		}

		this.xInterp[this.nInterpPoints - 1] = this.xPositions[this.nData - 1];
		CubicSpline arg8 = new CubicSpline(this.xPositions, this.yPositions);

		int arg3;
		for (arg3 = 0; arg3 < this.nInterpPoints; ++arg3) {
			this.yInterp[arg3] = arg8.interpolate(this.xInterp[arg3]);
		}

	}

	public void checkForIdenticalPoints() {
		int arg0 = this.nData;
		boolean arg1 = true;
		int arg2 = 0;

		int arg5;
		while (arg1) {
			boolean arg3 = true;
			int arg4 = arg2 + 1;

			while (true) {
				while (arg3) {
					System.out.println("ii " + arg2 + "  jj  " + arg4);
					if (this.xPositions[arg2] == this.xPositions[arg4]
							&& this.yPositions[arg2] == this.yPositions[arg4]) {
						System.out.print("Class DigiGraph: two identical points, " + this.xPositions[arg2] + ", "
								+ this.yPositions[arg2]);
						System.out
								.println(", in data array at indices " + arg2 + " and " + arg4 + ", one point removed");

						for (arg5 = arg4; arg5 < arg0; ++arg5) {
							this.xPositions[arg5 - 1] = this.xPositions[arg5];
							this.yPositions[arg5 - 1] = this.yPositions[arg5];
						}

						--arg0;
						if (arg0 - 1 == arg2) {
							arg3 = false;
						}
					} else {
						++arg4;
						if (arg4 >= arg0) {
							arg3 = false;
						}
					}
				}

				++arg2;
				if (arg2 >= arg0 - 1) {
					arg1 = false;
				}
				break;
			}
		}

		if (arg0 != this.nData) {
			double[] arg6 = new double[arg0];
			double[] arg7 = new double[arg0];

			for (arg5 = 0; arg5 < arg0; ++arg5) {
				arg6[arg5] = this.xPositions[arg5];
				arg7[arg5] = this.yPositions[arg5];
			}

			this.xPositions = arg6;
			this.yPositions = arg7;
			this.nData = arg0;
		}

	}

	private void plotDigitisedPoints() {
		MatlabChart fig = new MatlabChart() ;
		fig.plot(this.xPositions, this.yPositions, "b", 3f, "Data Points");
		fig.RenderPlot();
		fig.markerON(0);
		fig.setFigLineWidth(0, 0.5f);
//		fig.legendON();
		fig.legendOFF();
		fig.xlabel("X data");
		fig.ylabel("Y data");
		if(!xAxisIsLinear){
			fig.setXAxis_to_Log();
		}
		if(!yAxisIsLinear){
			fig.setYAxis_to_Log();
		}
		fig.run();

	}

	private void plotInterpolationPoints() {
		MatlabChart fig = new MatlabChart() ;
		fig.plot(this.xPositions, this.yPositions, "b", 1f, "Data Points");
		fig.plot(this.xInterp, this.yInterp, "r", 3f, "Interpolation");
		fig.RenderPlot();
		fig.markerON(0);
		fig.setFigLineWidth(0, 0f);
		fig.xlabel("X data");
		fig.ylabel("Y data");
//		fig.legendON();
		fig.legendOFF();
		if(!xAxisIsLinear){
			fig.setXAxis_to_Log();
		}
		if(!yAxisIsLinear){
			fig.setYAxis_to_Log();
		}
		fig.run();
	}

	private void setXAxisType(boolean isLinear){
		if(isLinear){
			// do nothing!
		}
		else{
			int M = xPosPixel.length ;
			double[] xLog = new double[M] ;
			for(int i=0; i<M; i++){
				xLog[i] = Math.log10(this.lowXvalue) + (this.xPosPixel[i] - this.lowXaxisXpixel)
						* (Math.log10(this.highXvalue) - Math.log10(this.lowXvalue)) / (this.highXaxisXpixel - this.lowXaxisXpixel);

				xPositions[i] = Math.pow(10, xLog[i]) ;
			}
		}

	}

	private void setYAxisType(boolean isLinear){
		if(isLinear){
			// do nothing!
		}
		else{
			int M = yPosPixel.length ;
			double[] yLog = new double[M] ;
			for(int i=0; i<M; i++){
				yLog[i] = Math.log10(this.lowYvalue) + (this.yPosPixel[i] - this.lowYaxisYpixel)
						* (Math.log10(this.highYvalue) - Math.log10(this.lowYvalue)) / (this.highYaxisYpixel - this.lowYaxisYpixel);

				yPositions[i] = Math.pow(10, yLog[i]) ;
			}
		}
	}


}
