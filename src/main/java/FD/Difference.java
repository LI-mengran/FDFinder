package FD;

import FastADC.evidence.evidenceSet.Evidence;
import ch.javasoft.bitset.LongBitSet;

public class Difference {

    public long count;
    long differenceValue;
    public LongBitSet bitset;

    public Difference(long _differenceValue, long _count) {
        differenceValue = _differenceValue;
        count = _count;
    }

    public LongBitSet getBitSet() {
        return bitset;
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
}
