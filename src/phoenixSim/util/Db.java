package phoenixSim.util;

import flanagan.analysis.ErrorProp;
import flanagan.circuits.Phasor;
import flanagan.complex.Complex;
import flanagan.complex.ComplexErrorProp;
import flanagan.math.Fmath;
import mathLib.util.MathUtils;

import java.awt.Component;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.JOptionPane;

public class Db {
	private static boolean inputTypeInfo = true;

	public static void setTypeInfoOption(int arg) {
		switch (arg) {
			case 1 :
				inputTypeInfo = true;
				break;
			case 2 :
				inputTypeInfo = false;
				break;
			default :
				throw new IllegalArgumentException("Option " + arg + " not recognised");
		}

	}

	public static final synchronized double readDouble(String arg) {
		String arg0 = "";
		double arg1 = 0.0D;
		boolean arg3 = false;
		System.out.flush();
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: double\n";
		}

		while (!arg3) {
			arg0 = JOptionPane.showInputDialog(arg4 + arg);
			if (arg0 != null) {
				try {
					arg1 = MathUtils.evaluate(arg0);
					arg3 = true;
				} catch (NumberFormatException arg6) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized double readDouble(String arg, double arg0) {
		String arg2 = "";
		double arg3 = 0.0D;
		boolean arg5 = false;
		System.out.flush();
		String arg6 = "";
		if (inputTypeInfo) {
			arg6 = "Input type: double\n";
		}

		arg = arg + "\n";
		String arg7 = arg0 + "";

		while (!arg5) {
			arg2 = JOptionPane.showInputDialog(arg6 + arg + " [default value = " + arg0 + "] ", arg7);
			if (arg2 != null) {
				if (arg2.equals("")) {
					arg3 = arg0;
					arg5 = true;
					arg2 = null;
				} else {
					try {
						arg3 = MathUtils.evaluate(arg2);
						arg5 = true;
					} catch (NumberFormatException arg9) {
						;
					}
				}
			}
		}

		return arg3;
	}

	public static final synchronized double readDouble() {
		String arg = "";
		String arg0 = "Input type: double";
		double arg1 = 0.0D;
		boolean arg3 = false;
		System.out.flush();

		while (!arg3) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null) {
				try {
					arg1 = MathUtils.evaluate(arg);
					arg3 = true;
				} catch (NumberFormatException arg5) {
					;
				}
			}
		}

		return arg1;
	}

	@SuppressWarnings("unused")
	public static final synchronized double[] readDoubleArray(String arg) {
		String arg0 = "";
		Object arg1 = null;
		boolean arg2 = false;
		System.out.flush();
		String arg3 = "";
		if (inputTypeInfo) {
			arg3 = "Input type: double[], each element separated by a comma\n";
		}

		arg = arg + "\n";
		String arg4 = " ";
		boolean arg5 = false;
		ArrayList<Double> arg6 = new ArrayList<Double>();

		while (true) {
			while (true) {
				int arg14;
				do {
					if (arg2) {
						int arg13 = arg6.size();
						double[] arg12 = new double[arg13];

						for (arg14 = 0; arg14 < arg13; ++arg14) {
							arg12[arg14] = ((Double) arg6.get(arg14)).doubleValue();
						}

						return arg12;
					}

					arg0 = JOptionPane.showInputDialog(arg3 + arg, arg4);
				} while (arg0 == null);

				if (arg0.equals("")) {
					arg0 = null;
				} else {
					boolean arg7 = true;
					String arg8 = null;
					boolean arg9 = true;

					while (arg9) {
						arg14 = arg0.indexOf(44);
						if (arg14 == -1) {
							arg8 = arg0.trim();
							arg9 = false;
							arg2 = true;
						} else {
							arg8 = arg0.substring(0, arg14).trim();
							arg0 = arg0.substring(arg14 + 1);
						}

						try {
							arg6.add(Double.valueOf(arg8));
						} catch (NumberFormatException arg11) {
							;
						}
					}
				}
			}
		}
	}

	public static final synchronized Complex readComplex(String arg) {
		String arg0 = "";
		Complex arg1 = new Complex();
		boolean arg2 = false;
		String arg3 = "";
		if (inputTypeInfo) {
			arg3 = "Input type: Complex (x + jy)\n";
		}

		System.out.flush();

		while (!arg2) {
			arg0 = JOptionPane.showInputDialog(arg3 + arg);
			if (arg0 != null) {
				try {
					arg1 = Complex.parseComplex(arg0);
					arg2 = true;
				} catch (NumberFormatException arg5) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized Complex readComplex(String arg, Complex arg0) {
		String arg1 = "";
		Complex arg2 = new Complex();
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: Complex (x + jy)\n";
		}

		String arg5 = arg0 + "";
		arg = arg + "\n";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = arg0;
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = Complex.parseComplex(arg1);
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized Complex readComplex(String arg, String arg0) {
		String arg1 = "";
		Complex arg2 = new Complex();
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: Complex (x + jy)\n";
		}

		String arg5 = arg0;
		arg = arg + "\n";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = Complex.parseComplex(arg0);
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = Complex.parseComplex(arg1);
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized Complex readComplex() {
		String arg = "";
		String arg0 = "Input type: Complex (x + jy)";
		Complex arg1 = new Complex();
		boolean arg2 = false;
		System.out.flush();

		while (!arg2) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null) {
				try {
					arg1 = Complex.parseComplex(arg.trim());
					arg2 = true;
				} catch (NumberFormatException arg4) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized Phasor readPhasor(String arg) {
		String arg0 = "";
		Phasor arg1 = new Phasor();
		boolean arg2 = false;
		String arg3 = "";
		if (inputTypeInfo) {
			arg3 = "Input type: Phasor (\'mag\'<\'phase\'deg or \'mag\'<\'phase\'rad)\n";
		}

		System.out.flush();

		while (!arg2) {
			arg0 = JOptionPane.showInputDialog(arg3 + arg);
			if (arg0 != null) {
				try {
					arg1 = Phasor.parsePhasor(arg0);
					arg2 = true;
				} catch (NumberFormatException arg5) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized Phasor readPhasor(String arg, Phasor arg0) {
		String arg1 = "";
		Phasor arg2 = new Phasor();
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: Phasor (\'mag\'<\'phase\'deg or \'mag\'<\'phase\'rad)\n";
		}

		String arg5 = arg0 + "";
		arg = arg + "\n";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = arg0;
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = Phasor.parsePhasor(arg1);
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized Phasor readPhasor(String arg, String arg0) {
		String arg1 = "";
		Phasor arg2 = new Phasor();
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: Phasor (\'mag\'<\'phase\'deg or \'mag\'<\'phase\'rad)\n";
		}

		String arg5 = arg0;
		arg = arg + "\n";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = Phasor.parsePhasor(arg0);
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = Phasor.parsePhasor(arg1);
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized Phasor readPhasor() {
		String arg = "";
		String arg0 = "Input type: Phasor (\'mag\'<\'phase\'deg or \'mag\'<\'phase\'rad)";
		Phasor arg1 = new Phasor();
		boolean arg2 = false;
		System.out.flush();

		while (!arg2) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null) {
				try {
					arg1 = Phasor.parsePhasor(arg.trim());
					arg2 = true;
				} catch (NumberFormatException arg4) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized float readFloat(String arg) {
		String arg0 = "";
		float arg1 = 0.0F;
		boolean arg2 = false;
		System.out.flush();
		String arg3 = "";
		if (inputTypeInfo) {
			arg3 = "Input type: float\n";
		}

		while (!arg2) {
			arg0 = JOptionPane.showInputDialog(arg3 + arg);
			if (arg0 != null) {
				try {
					arg1 = Float.parseFloat(arg0.trim());
					arg2 = true;
				} catch (NumberFormatException arg5) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized float readFloat(String arg, float arg0) {
		String arg1 = "";
		float arg2 = 0.0F;
		boolean arg3 = false;
		System.out.flush();
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: float\n";
		}

		arg = arg + "\n";
		String arg5 = arg0 + "";

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = arg0;
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = Float.parseFloat(arg1.trim());
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized float readFloat() {
		String arg = "";
		String arg0 = "Input type: float";
		float arg1 = 0.0F;
		boolean arg2 = false;
		System.out.flush();

		while (!arg2) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null) {
				try {
					arg1 = Float.parseFloat(arg.trim());
					arg2 = true;
				} catch (NumberFormatException arg4) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized int readInt(String arg) {
		String arg0 = "";
		int arg1 = 0;
		boolean arg2 = false;
		System.out.flush();
		String arg3 = "";
		if (inputTypeInfo) {
			arg3 = "Input type: int\n";
		}

		while (!arg2) {
			arg0 = JOptionPane.showInputDialog(arg3 + arg);
			if (arg0 != null) {
				try {
					arg1 = Integer.parseInt(arg0.trim());
					arg2 = true;
				} catch (NumberFormatException arg5) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized int readInt(String arg, int arg0) {
		String arg1 = "";
		int arg2 = 0;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: int\n";
		}

		arg = arg + "\n";
		String arg5 = arg0 + "";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = arg0;
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = Integer.parseInt(arg1.trim());
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized int readInt() {
		String arg = "";
		String arg0 = "Input type: int";
		int arg1 = 0;
		boolean arg2 = false;
		System.out.flush();

		while (!arg2) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null) {
				try {
					arg1 = Integer.parseInt(arg.trim());
					arg2 = true;
				} catch (NumberFormatException arg4) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized long readLong(String arg) {
		String arg0 = "";
		long arg1 = 0L;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: long\n";
		}

		System.out.flush();

		while (!arg3) {
			arg0 = JOptionPane.showInputDialog(arg4 + arg);
			if (arg0 != null) {
				try {
					arg1 = Long.parseLong(arg0.trim());
					arg3 = true;
				} catch (NumberFormatException arg6) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized long readLong(String arg, long arg0) {
		String arg2 = "";
		long arg3 = 0L;
		boolean arg5 = false;
		String arg6 = "";
		if (inputTypeInfo) {
			arg6 = "Input type: long\n";
		}

		arg = arg + "\n";
		String arg7 = arg0 + "";
		System.out.flush();

		while (!arg5) {
			arg2 = JOptionPane.showInputDialog(arg6 + arg + " [default value = " + arg0 + "] ", arg7);
			if (arg2 != null) {
				if (arg2.equals("")) {
					arg3 = arg0;
					arg5 = true;
					arg2 = null;
				} else {
					try {
						arg3 = Long.parseLong(arg2.trim());
						arg5 = true;
					} catch (NumberFormatException arg9) {
						;
					}
				}
			}
		}

		return arg3;
	}

	public static final synchronized long readLong() {
		String arg = "";
		String arg0 = "Input type: long";
		long arg1 = 0L;
		boolean arg3 = false;
		System.out.flush();

		while (!arg3) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null) {
				try {
					arg1 = Long.parseLong(arg.trim());
					arg3 = true;
				} catch (NumberFormatException arg5) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized long readShort(String arg) {
		String arg0 = "";
		long arg1 = 0L;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: short\n";
		}

		System.out.flush();

		while (!arg3) {
			arg0 = JOptionPane.showInputDialog(arg4 + arg);
			if (arg0 != null) {
				try {
					arg1 = (long) Short.parseShort(arg0.trim());
					arg3 = true;
				} catch (NumberFormatException arg6) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized short readShort(String arg, short arg0) {
		String arg1 = "";
		short arg2 = 0;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: short\n";
		}

		arg = arg + "\n";
		String arg5 = arg0 + "";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = arg0;
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = Short.parseShort(arg1.trim());
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized short readShort() {
		String arg = "";
		String arg0 = "Input type: short";
		short arg1 = 0;
		boolean arg2 = false;
		System.out.flush();

		while (!arg2) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null) {
				try {
					arg1 = Short.parseShort(arg.trim());
					arg2 = true;
				} catch (NumberFormatException arg4) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized long readByte(String arg) {
		String arg0 = "";
		long arg1 = 0L;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: short\n";
		}

		System.out.flush();

		while (!arg3) {
			arg0 = JOptionPane.showInputDialog(arg4 + arg);
			if (arg0 != null) {
				try {
					arg1 = (long) Byte.parseByte(arg0.trim());
					arg3 = true;
				} catch (NumberFormatException arg6) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized byte readByte(String arg, byte arg0) {
		String arg1 = "";
		byte arg2 = 0;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: byte\n";
		}

		arg = arg + "\n";
		String arg5 = arg0 + "";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = arg0;
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = Byte.parseByte(arg1.trim());
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized byte readByte() {
		String arg = "";
		String arg0 = "Input type: byte";
		byte arg1 = 0;
		boolean arg2 = false;
		System.out.flush();

		while (!arg2) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null) {
				try {
					arg1 = Byte.parseByte(arg.trim());
					arg2 = true;
				} catch (NumberFormatException arg4) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized char readChar(String arg) {
		String arg0 = "";
		char arg1 = 32;
		boolean arg2 = false;
		String arg3 = "";
		if (inputTypeInfo) {
			arg3 = "Input type: char\n";
		}

		System.out.flush();

		while (!arg2) {
			arg0 = JOptionPane.showInputDialog(arg3 + arg);
			if (arg0 != null && !arg0.equals("")) {
				arg1 = arg0.charAt(0);
				arg2 = true;
			}
		}

		return arg1;
	}

	public static final synchronized char readChar(String arg, char arg0) {
		String arg1 = "";
		char arg2 = 32;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: char\n";
		}

		arg = arg + "\n";
		String arg5 = arg0 + "";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = arg0;
					arg3 = true;
					arg1 = null;
				} else {
					arg2 = arg1.charAt(0);
					arg3 = true;
				}
			}
		}

		return arg2;
	}

	public static final synchronized char readChar() {
		String arg = "";
		String arg0 = "Input type: char";
		char arg1 = 32;
		boolean arg2 = false;
		System.out.flush();

		while (!arg2) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null && !arg.equals("")) {
				arg1 = arg.charAt(0);
				arg2 = true;
			}
		}

		return arg1;
	}

	public static final synchronized String readLine(String arg) {
		String arg0 = "";
		boolean arg1 = false;
		String arg2 = "";
		if (inputTypeInfo) {
			arg2 = "Input type: String [a line]\n";
		}

		System.out.flush();

		while (!arg1) {
			arg0 = JOptionPane.showInputDialog(arg2 + arg);
			if (arg0 != null) {
				arg1 = true;
			}
		}

		return arg0;
	}

	public static final synchronized String readLine(String arg, String arg0) {
		String arg1 = "";
		boolean arg2 = false;
		String arg3 = "";
		if (inputTypeInfo) {
			arg3 = "Input type: String [a line]\n";
		}

		arg = arg + "\n";
		String arg4 = arg0 + "";
		System.out.flush();

		while (!arg2) {
			arg1 = JOptionPane.showInputDialog(arg3 + arg + " [default value = " + arg0 + "] ", arg4);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg1 = arg0;
					arg2 = true;
				} else {
					arg2 = true;
				}
			}
		}

		return arg1;
	}

	public static final synchronized String readLine() {
		String arg = "";
		String arg0 = "Input type: String [a line]";
		boolean arg1 = false;
		System.out.flush();

		while (!arg1) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null) {
				arg1 = true;
			}
		}

		return arg;
	}

	public static final synchronized boolean readBoolean(String arg, boolean arg0) {
		String arg1 = "";
		boolean arg2 = false;
		boolean arg3 = false;
		System.out.flush();
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input boolean\n";
		}

		arg = arg + "\n";
		String arg5 = arg0 + "";

		while (true) {
			while (true) {
				do {
					if (arg3) {
						return arg2;
					}

					arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
				} while (arg1 == null);

				if (arg1.equals("")) {
					arg2 = arg0;
					arg3 = true;
					arg1 = null;
				} else if (!arg1.equals("true") && !arg1.trim().equals("TRUE")) {
					if (arg1.equals("false") || arg1.trim().equals("FALSE")) {
						arg2 = false;
						arg3 = true;
					}
				} else {
					arg2 = true;
					arg3 = true;
				}
			}
		}
	}

	public static final synchronized boolean readBoolean(String arg) {
		String arg0 = "";
		boolean arg1 = false;
		boolean arg2 = false;
		System.out.flush();
		String arg3 = "";
		if (inputTypeInfo) {
			arg3 = "Input boolean\n";
		}

		while (true) {
			do {
				while (true) {
					do {
						if (arg2) {
							return arg1;
						}

						arg0 = JOptionPane.showInputDialog(arg3 + arg);
					} while (arg0 == null);

					if (!arg0.equals("true") && !arg0.trim().equals("TRUE")) {
						break;
					}

					arg1 = true;
					arg2 = true;
				}
			} while (!arg0.equals("false") && !arg0.trim().equals("FALSE"));

			arg1 = false;
			arg2 = true;
		}
	}

	public static final synchronized boolean readBoolean() {
		String arg = "";
		String arg0 = "Input type: boolean";
		boolean arg1 = false;
		boolean arg2 = false;
		System.out.flush();

		while (true) {
			do {
				while (true) {
					do {
						if (arg2) {
							return arg1;
						}

						arg = JOptionPane.showInputDialog(arg0);
					} while (arg == null);

					if (!arg.equals("true") && !arg.trim().equals("TRUE")) {
						break;
					}

					arg1 = true;
					arg2 = true;
				}
			} while (!arg.equals("false") && !arg.trim().equals("FALSE"));

			arg1 = false;
			arg2 = true;
		}
	}

	public static final synchronized BigDecimal readBigDecimal(String arg) {
		String arg0 = "";
		BigDecimal arg1 = null;
		boolean arg2 = false;
		String arg3 = "";
		if (inputTypeInfo) {
			arg3 = "Input type: BigDecimal\n";
		}

		System.out.flush();

		while (!arg2) {
			arg0 = JOptionPane.showInputDialog(arg3 + arg);
			if (arg0 != null) {
				try {
					arg1 = new BigDecimal(arg0);
					arg2 = true;
				} catch (NumberFormatException arg5) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized BigDecimal readBigDecimal(String arg, BigDecimal arg0) {
		String arg1 = "";
		BigDecimal arg2 = null;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: BigDecimal\n";
		}

		String arg5 = arg0.toString() + "";
		arg = arg + "\n";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = arg0;
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = new BigDecimal(arg1);
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized BigDecimal readBigDecimal(String arg, String arg0) {
		String arg1 = "";
		BigDecimal arg2 = null;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: BigDecimal\n";
		}

		String arg5 = arg0;
		arg = arg + "\n";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = new BigDecimal(arg0);
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = new BigDecimal(arg1);
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized BigDecimal readBigDecimal(String arg, double arg0) {
		String arg2 = "";
		BigDecimal arg3 = null;
		boolean arg4 = false;
		String arg5 = "";
		if (inputTypeInfo) {
			arg5 = "Input type: BigDecimal\n";
		}

		Double arg6 = new Double(arg0);
		String arg7 = arg6.toString();
		arg = arg + "\n";
		System.out.flush();

		while (!arg4) {
			arg2 = JOptionPane.showInputDialog(arg5 + arg + " [default value = " + arg0 + "] ", arg7);
			if (arg2 != null) {
				if (arg2.equals("")) {
					arg3 = new BigDecimal(arg7);
					arg4 = true;
					arg2 = null;
				} else {
					try {
						arg3 = new BigDecimal(arg2);
						arg4 = true;
					} catch (NumberFormatException arg9) {
						;
					}
				}
			}
		}

		return arg3;
	}

	public static final synchronized BigDecimal readBigDecimal(String arg, float arg0) {
		String arg1 = "";
		BigDecimal arg2 = null;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: BigDecimal\n";
		}

		Float arg5 = new Float(arg0);
		String arg6 = arg5.toString();
		arg = arg + "\n";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg6);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = new BigDecimal(arg6);
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = new BigDecimal(arg1);
						arg3 = true;
					} catch (NumberFormatException arg8) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized BigDecimal readBigDecimal(String arg, long arg0) {
		String arg2 = "";
		BigDecimal arg3 = null;
		boolean arg4 = false;
		String arg5 = "";
		if (inputTypeInfo) {
			arg5 = "Input type: BigDecimal\n";
		}

		Long arg6 = new Long(arg0);
		String arg7 = arg6.toString();
		arg = arg + "\n";
		System.out.flush();

		while (!arg4) {
			arg2 = JOptionPane.showInputDialog(arg5 + arg + " [default value = " + arg0 + "] ", arg7);
			if (arg2 != null) {
				if (arg2.equals("")) {
					arg3 = new BigDecimal(arg7);
					arg4 = true;
					arg2 = null;
				} else {
					try {
						arg3 = new BigDecimal(arg2);
						arg4 = true;
					} catch (NumberFormatException arg9) {
						;
					}
				}
			}
		}

		return arg3;
	}

	public static final synchronized BigDecimal readBigDecimal(String arg, int arg0) {
		String arg1 = "";
		BigDecimal arg2 = null;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: BigDecimal\n";
		}

		Integer arg5 = new Integer(arg0);
		String arg6 = arg5.toString();
		arg = arg + "\n";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg6);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = new BigDecimal(arg6);
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = new BigDecimal(arg1);
						arg3 = true;
					} catch (NumberFormatException arg8) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized BigDecimal readBigDecimal() {
		String arg = "";
		String arg0 = "Input type: BigDecimal";
		BigDecimal arg1 = null;
		boolean arg2 = false;
		System.out.flush();

		while (!arg2) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null) {
				try {
					arg1 = new BigDecimal(arg.trim());
					arg2 = true;
				} catch (NumberFormatException arg4) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized BigInteger readBigInteger(String arg) {
		String arg0 = "";
		BigInteger arg1 = null;
		boolean arg2 = false;
		String arg3 = "";
		if (inputTypeInfo) {
			arg3 = "Input type: BigInteger\n";
		}

		System.out.flush();

		while (!arg2) {
			arg0 = JOptionPane.showInputDialog(arg3 + arg);
			if (arg0 != null) {
				try {
					arg1 = new BigInteger(arg0);
					arg2 = true;
				} catch (NumberFormatException arg5) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized BigInteger readBigInteger(String arg, BigInteger arg0) {
		String arg1 = "";
		BigInteger arg2 = null;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: BigInteger\n";
		}

		String arg5 = arg0.toString() + "";
		arg = arg + "\n";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = arg0;
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = new BigInteger(arg1);
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized BigInteger readBigInteger(String arg, String arg0) {
		String arg1 = "";
		BigInteger arg2 = null;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: BigInteger\n";
		}

		String arg5 = arg0;
		arg = arg + "\n";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg5);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = new BigInteger(arg0);
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = new BigInteger(arg1);
						arg3 = true;
					} catch (NumberFormatException arg7) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized BigInteger readBigInteger(String arg, long arg0) {
		String arg2 = "";
		BigInteger arg3 = null;
		boolean arg4 = false;
		String arg5 = "";
		if (inputTypeInfo) {
			arg5 = "Input type: BigInteger\n";
		}

		Long arg6 = new Long(arg0);
		String arg7 = arg6.toString();
		arg = arg + "\n";
		System.out.flush();

		while (!arg4) {
			arg2 = JOptionPane.showInputDialog(arg5 + arg + " [default value = " + arg0 + "] ", arg7);
			if (arg2 != null) {
				if (arg2.equals("")) {
					arg3 = new BigInteger(arg7);
					arg4 = true;
					arg2 = null;
				} else {
					try {
						arg3 = new BigInteger(arg2);
						arg4 = true;
					} catch (NumberFormatException arg9) {
						;
					}
				}
			}
		}

		return arg3;
	}

	public static final synchronized BigInteger readBigInteger(String arg, int arg0) {
		String arg1 = "";
		BigInteger arg2 = null;
		boolean arg3 = false;
		String arg4 = "";
		if (inputTypeInfo) {
			arg4 = "Input type: BigInteger\n";
		}

		Integer arg5 = new Integer(arg0);
		String arg6 = arg5.toString();
		arg = arg + "\n";
		System.out.flush();

		while (!arg3) {
			arg1 = JOptionPane.showInputDialog(arg4 + arg + " [default value = " + arg0 + "] ", arg6);
			if (arg1 != null) {
				if (arg1.equals("")) {
					arg2 = new BigInteger(arg6);
					arg3 = true;
					arg1 = null;
				} else {
					try {
						arg2 = new BigInteger(arg1);
						arg3 = true;
					} catch (NumberFormatException arg8) {
						;
					}
				}
			}
		}

		return arg2;
	}

	public static final synchronized BigInteger readBigInteger() {
		String arg = "";
		String arg0 = "Input type: BigInteger";
		BigInteger arg1 = null;
		boolean arg2 = false;
		System.out.flush();

		while (!arg2) {
			arg = JOptionPane.showInputDialog(arg0);
			if (arg != null) {
				try {
					arg1 = new BigInteger(arg.trim());
					arg2 = true;
				} catch (NumberFormatException arg4) {
					;
				}
			}
		}

		return arg1;
	}

	public static final synchronized boolean yesNo(String arg) {
		int arg0 = JOptionPane.showConfirmDialog((Component) null, arg, "Db Class Yes or No Box", 0, 3);
		boolean arg1 = false;
		if (arg0 == 0) {
			arg1 = true;
		}

		return arg1;
	}

	public static final synchronized boolean noYes(String arg) {
		Object[] arg0 = new Object[]{"Yes", "No"};
		int arg1 = JOptionPane.showOptionDialog((Component) null, arg, "Db Class Yes or No Box", 0, 3, (Icon) null,
				arg0, arg0[1]);
		boolean arg2 = false;
		if (arg1 == 0) {
			arg2 = true;
		}

		return arg2;
	}

	public static final synchronized void show(String arg, double arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (double)", 1);
	}

	public static final synchronized void show(double arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (double)", 1);
	}

	public static final synchronized void show(String arg, double arg0, int arg2) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + Fmath.truncate(arg0, arg2), "Db.show (double)", 1);
	}

	public static final synchronized void show(double arg, int arg1) {
		JOptionPane.showMessageDialog((Component) null, " " + Fmath.truncate(arg, arg1), "Db.show (double)", 1);
	}

	public static final synchronized void show(String arg, Double arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0.doubleValue(), "Db.show (Double)", 1);
	}

	public static final synchronized void show(Double arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg.doubleValue(), "Db.show (Double)", 1);
	}

	public static final synchronized void show(String arg, float arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (float)", 1);
	}

	public static final synchronized void show(float arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (float)", 1);
	}

	public static final synchronized void show(String arg, float arg0, int arg1) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + Fmath.truncate(arg0, arg1), "Db.show (float)", 1);
	}

	public static final synchronized void show(float arg, int arg0) {
		JOptionPane.showMessageDialog((Component) null, " " + Fmath.truncate(arg, arg0), "Db.show (float)", 1);
	}

	public static final synchronized void show(String arg, Float arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0.floatValue(), "Db.show (float)", 1);
	}

	public static final synchronized void show(Float arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg.floatValue(), "Db.show (float)", 1);
	}

	public static final synchronized void show(String arg, BigDecimal arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0.toString(), "Db.show (BigDecimal)", 1);
	}

	public static final synchronized void show(BigDecimal arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg.toString(), "Db.show (BigDecimal)", 1);
	}

	public static final synchronized void show(String arg, BigInteger arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0.toString(), "Db.show (BigInteger)", 1);
	}

	public static final synchronized void show(BigInteger arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg.toString(), "Db.show (BigInteger)", 1);
	}

	public static final synchronized void show(String arg, int arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (int)", 1);
	}

	public static final synchronized void show(int arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (int)", 1);
	}

	public static final synchronized void show(String arg, Integer arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0.intValue(), "Db.show (int)", 1);
	}

	public static final synchronized void show(Integer arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg.intValue(), "Db.show (int)", 1);
	}

	public static final synchronized void show(String arg, long arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (long)", 1);
	}

	public static final synchronized void show(long arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (long)", 1);
	}

	public static final synchronized void show(String arg, Long arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0.longValue(), "Db.show (long)", 1);
	}

	public static final synchronized void show(Long arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg.longValue(), "Db.show (long)", 1);
	}

	public static final synchronized void show(String arg, short arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (short)", 1);
	}

	public static final synchronized void show(short arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (short)", 1);
	}

	public static final synchronized void show(String arg, Short arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0.shortValue(), "Db.show (short)", 1);
	}

	public static final synchronized void show(Short arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg.shortValue(), "Db.show (short)", 1);
	}

	public static final synchronized void show(String arg, byte arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (byte)", 1);
	}

	public static final synchronized void show(byte arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (byte)", 1);
	}

	public static final synchronized void show(String arg, Byte arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0.byteValue(), "Db.show (byte)", 1);
	}

	public static final synchronized void show(Byte arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg.byteValue(), "Db.show (byte)", 1);
	}

	public static final synchronized void show(String arg, Complex arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (Complex)", 1);
	}

	public static final synchronized void show(Complex arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (Complex)", 1);
	}

	public static final synchronized void show(String arg, Complex arg0, int arg1) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + Complex.truncate(arg0, arg1), "Db.show (Complex)",
				1);
	}

	public static final synchronized void show(Complex arg, int arg0) {
		JOptionPane.showMessageDialog((Component) null, " " + Complex.truncate(arg, arg0), "Db.show (Complex)", 1);
	}

	public static final synchronized void show(String arg, Phasor arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (Phasor)", 1);
	}

	public static final synchronized void show(Phasor arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (Phasor)", 1);
	}

	public static final synchronized void show(String arg, Phasor arg0, int arg1) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + Phasor.truncate(arg0, arg1), "Db.show (Phasor)", 1);
	}

	public static final synchronized void show(Phasor arg, int arg0) {
		JOptionPane.showMessageDialog((Component) null, " " + Phasor.truncate(arg, arg0), "Db.show (Phasor)", 1);
	}

	public static final synchronized void show(String arg, ErrorProp arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (ErrorProp)", 1);
	}

	public static final synchronized void show(ErrorProp arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (ErrorProp)", 1);
	}

	public static final synchronized void show(String arg, ErrorProp arg0, int arg1) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + ErrorProp.truncate(arg0, arg1),
				"Db.show (ErrorProp)", 1);
	}

	public static final synchronized void show(ErrorProp arg, int arg0) {
		JOptionPane.showMessageDialog((Component) null, " " + ErrorProp.truncate(arg, arg0), "Db.show (ErrorProp)", 1);
	}

	public static final synchronized void show(String arg, ComplexErrorProp arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (ComplexErrorProp)", 1);
	}

	public static final synchronized void show(ComplexErrorProp arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (ComplexErrorProp)", 1);
	}

	public static final synchronized void show(String arg, ComplexErrorProp arg0, int arg1) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + ComplexErrorProp.truncate(arg0, arg1),
				"Db.show (ComplexErrorProp)", 1);
	}

	public static final synchronized void show(ComplexErrorProp arg, int arg0) {
		JOptionPane.showMessageDialog((Component) null, " " + ComplexErrorProp.truncate(arg, arg0),
				"Db.show (ComplexErrorProp)", 1);
	}

	public static final synchronized void show(String arg, boolean arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (boolean)", 1);
	}

	public static final synchronized void show(boolean arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (boolean)", 1);
	}

	public static final synchronized void show(String arg, Boolean arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0.booleanValue(), "Db.show (boolean)", 1);
	}

	public static final synchronized void show(Boolean arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg.booleanValue(), "Db.show (boolean)", 1);
	}

	public static final synchronized void show(String arg, char arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (char)", 1);
	}

	public static final synchronized void show(char arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg, "Db.show (char)", 1);
	}

	public static final synchronized void show(String arg, Character arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0.charValue(), "Db.show (char)", 1);
	}

	public static final synchronized void show(Character arg) {
		JOptionPane.showMessageDialog((Component) null, " " + arg.charValue(), "Db.show (char)", 1);
	}

	public static final synchronized void show(String arg, String arg0) {
		JOptionPane.showMessageDialog((Component) null, arg + " " + arg0, "Db.show (String)", 1);
	}

	public static final synchronized void show(String arg) {
		JOptionPane.showMessageDialog((Component) null, arg, "Db.show (message only)", 1);
	}

	public static final synchronized int optionBox(String arg, String[] arg0, String[] arg1, int arg2) {
		int arg3 = arg1.length;
		if (arg3 != arg0.length) {
			throw new IllegalArgumentException("There must be the same number of boxTitles and comments");
		} else {
			Object[] arg4 = new Object[arg3];

			for (int arg5 = 0; arg5 < arg3; ++arg5) {
				arg4[arg5] = "(" + (arg5 + 1) + ") " + arg1[arg5];
			}

			String arg7 = "1. " + arg0[0] + "\n";

			for (int arg6 = 1; arg6 < arg3; ++arg6) {
				arg7 = arg7 + (arg6 + 1) + ". " + arg0[arg6] + "\n";
			}

			return 1 + JOptionPane.showOptionDialog((Component) null, arg7, arg, 1, 3, (Icon) null, arg4,
					arg4[arg2 - 1]);
		}
	}

	public static final synchronized int optionBox(String arg, String arg0, String[] arg1, int arg2) {
		int arg3 = arg1.length;
		Object[] arg4 = new Object[arg3];

		for (int arg5 = 0; arg5 < arg3; ++arg5) {
			arg4[arg5] = "(" + (arg5 + 1) + ") " + arg1[arg5];
		}

		return 1 + JOptionPane.showOptionDialog((Component) null, arg0, arg, 1, 3, (Icon) null, arg4, arg4[arg2 - 1]);
	}

	public static final synchronized void endProgram() {
		int arg = JOptionPane.showConfirmDialog((Component) null, "Do you wish to end the program", "End Program", 0,
				3);
		if (arg == 0) {
			System.exit(0);
		} else {
			JOptionPane.showMessageDialog((Component) null,
					"Now you must press the appropriate escape key/s, e.g. Ctrl C, to exit this program");
		}

	}
}
