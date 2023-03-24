package FD.FDs;

import FD.FDFinder.HyperEdge;
import ch.javasoft.bitset.LongBitSet;

public class FD {
    public final LongBitSet lhs;
    public final LongBitSet rhs;

    public FD(LongBitSet lhs, LongBitSet rhs){
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public LongBitSet getLhs() {
        return lhs;
    }

    public LongBitSet getRhs() {
        return rhs;
    }

}
