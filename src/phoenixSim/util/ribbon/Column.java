package phoenixSim.util.ribbon;

import javafx.scene.layout.VBox;

/**
 * Created by pedro_000 on 11/20/2014.
 */
public class Column extends VBox{
    public final static String DEFAULT_STYLE_CLASS = "column";

    public Column()
    {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    }

}
