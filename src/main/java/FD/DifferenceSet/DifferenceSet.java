package FD.DifferenceSet;

import ch.javasoft.bitset.IBitSet;

import java.util.List;

public class DifferenceSet {

    List<Difference> differences;
    Integer length;


    public DifferenceSet(List<Difference> _differences, Integer _length) {
        differences = _differences;
        length = _length;
    }

    public int size(){
        return differences.size();
    }

    public Integer getLength() {
        return length;
    }

    public List<Difference> getDifferences() {
        return differences;
    }
}
