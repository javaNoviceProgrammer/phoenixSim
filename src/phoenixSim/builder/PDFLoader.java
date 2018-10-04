package phoenixSim.builder;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import com.sun.pdfview.PDFViewer;

import phoenixSim.util.FileChooserFX;

public class PDFLoader {

	public static void loadPDF(){
		FileChooserFX fc = new FileChooserFX() ;
		fc.setExtension("pdf");
		fc.openFile() ;
		String pdfFilePath = fc.getFilePath() ;

		File pdfFile = new File(pdfFilePath) ;
		if(Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().open(pdfFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void loadPDF(File pdfFile){
		if(Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().open(pdfFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void loadPDF(String pdfFilePath){
		File pdfFile = new File(pdfFilePath) ;
		if(Desktop.isDesktopSupported()){
			try {
				Desktop.getDesktop().open(pdfFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// Loading pdf files using the internal pdf loader
	public static void loadPDF_useInternalPDFLoader(String pdfFilePath){

		Runnable runnable = new Runnable(){
			@Override
			public void run() {
				File pdfFile;
				try {
					pdfFile = new File(Object.class.getClass().getResource(pdfFilePath).toURI());
					PDFViewer pdf = new PDFViewer(true) ;
					try {
						pdf.openFile(pdfFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}

			}

		} ;
		EventQueue.invokeLater(runnable);
	}





}
