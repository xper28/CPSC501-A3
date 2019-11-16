import java.util.ArrayList;

public class Cgraph {
    ArrayList<Plot> cgraph = new ArrayList<>();
    Coordinate c1 = new Coordinate();
    Coordinate c2 = new Coordinate();
    Plot p1 = new Plot(c1);
    Plot p2 = new Plot(c2);

    public Cgraph() {
        cgraph.add(p1);
        cgraph.add(p2);
    }

}
