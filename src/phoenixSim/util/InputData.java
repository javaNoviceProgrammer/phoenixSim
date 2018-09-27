package phoenixSim.util;

/**
 * Created by meisam on 6/24/17.
 */
public class InputData {

    String name ;
    double value ;

    public InputData(
            String name,
            double value
    ){
        this.name = name ;
        this.value = value ;
    }

    public String getName(){
        return name ;
    }

    public double getValue() {
        return value;
    }
}
