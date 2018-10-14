package phoenixSim.tests;

import java.io.IOException;

import com.sun.pdfview.PDFViewer;

import mathLib.util.CustomJFileChooser;

public class TestPdfLoader {
	public static void main(String[] args) {
		CustomJFileChooser fChooser = new CustomJFileChooser() ;
		fChooser.openFile(); 
		String file = fChooser.getSelectedFile().getAbsolutePath() ;
//		PDFLoader.loadPDF_useInternalPDFLoader(fChooser.getSelectedFile().getAbsolutePath());
		System.out.println(file);
		PDFViewer viewer = new PDFViewer(false) ;
		try {
			viewer.openFile(fChooser.getSelectedFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
