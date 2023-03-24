package FD.DifferenceSet;

import FD.util.Utils;
import ch.javasoft.bitset.IBitSet;
import ch.javasoft.bitset.LongBitSet;
import jdk.jshell.execution.Util;

import java.util.BitSet;

public class Difference {

    public long count;
    long differenceValue;
    public BitSet bitset;
    int nAttributes;
    IBitSet mask;

    public Difference(long _differenceValue, long _count, int _nAttributes) {
        differenceValue = _differenceValue;
        count = _count;
        nAttributes = _nAttributes;
        mask = new LongBitSet(nAttributes);
        for(int index = 0; index < nAttributes; index ++)
            mask.set(index);
    }

    public LongBitSet getBitSet() {

        bitset = Utils.longToBitSet(nAttributes, differenceValue);
        return new LongBitSet(bitset).getXor(mask);
    }

    public long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Difference difference = (Difference) o;
        return differenceValue == difference.differenceValue;
    }

    @Override
    public int hashCode() {
        return (int) (differenceValue ^ (differenceValue >>> 32));
    }

    public long getDifferenceValue() {
        return differenceValue;
    }
}
