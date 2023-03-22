package FD;

import java.util.List;

public class DifferenceSet {

    List<Difference> differences;

    public DifferenceSet(List<Difference> _differences) {
        differences = _differences;
    }

    public int size(){
        return differences.size();
    }


}
