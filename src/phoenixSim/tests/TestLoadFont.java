package phoenixSim.tests;

import javafx.scene.text.Font;

public class TestLoadFont {
	public static void main(String[] args) {
		Font font = Font.loadFont(Object.class.getClass().getResourceAsStream("/phoenixSim/extras/fonts/segoeui.ttf"), 12) ;
		System.out.println(font);
	}
}
