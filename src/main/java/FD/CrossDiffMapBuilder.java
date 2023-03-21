package FD;

import FastADC.plishard.Pli;
import FastADC.plishard.PliShard;
import com.koloboke.collect.map.hash.HashLongLongMap;
import com.koloboke.collect.map.hash.HashLongLongMaps;

import java.util.List;

public class CrossDiffMapBuilder {
    private final List<Pli> plis1, plis2;
    private final int differenceCount;
    private final int nAttributes;

    public CrossDiffMapBuilder(PliShard shard1, PliShard shard2) {
        plis1 = shard1.plis;
        plis2 = shard2.plis;
        differenceCount = (shard1.end - shard1.beg) * (shard2.end - shard2.beg);
        nAttributes = shard1.plis.size();
    }

    public HashLongLongMap buildDiffMap(){
        long[] forwardDifferenceValues = new long[differenceCount];   // plis1 -> plis2
        long[] reverseDifferenceValues = new long[differenceCount];   // plis2 -> plis1

        HashLongLongMap diffMap = HashLongLongMaps.newMutableMap();

        for(int e = 0; e < nAttributes; e++){
            Pli pivotPli = plis1.get(e);
            Pli probePli = plis2.get(e);
            long mask = 1l << e;
            /** for every cluster in pli*/
            int[] pivotKeys = pivotPli.getKeys();
            /** for every cluster in pli*/
            for (int i = 0; i < pivotKeys.length; i++) {
                Integer j = probePli.getClusterIdByKey(pivotKeys[i]);
                if (j != null) {
                    int beg1 = pivotPli.pliShard.beg, range1 = pivotPli.pliShard.end - beg1;
                    int beg2 = probePli.pliShard.beg, range2 = probePli.pliShard.end - beg2;
                    /** for every tuple in cluster*/
                    for (int tid1 : pivotPli.get(i).getRawCluster()) {
                        int t1 = tid1 - beg1, r1 = t1 * range2 - beg2;
                        for (int tid2 : probePli.get(j).getRawCluster()) {
                            forwardDifferenceValues[r1 + tid2] |= mask;
                            reverseDifferenceValues[(tid2 - beg2) * range1 + t1] |= mask;
                        }
                    }
                }
            }

        }

        /** accumulate differenceValues to differenceSet*/

        /** first put differenceValue and count to diffMap*/
        for(long differenceValue :forwardDifferenceValues){
            diffMap.addValue(differenceValue, 1L, 0L);
        }
        for(long differenceValue :reverseDifferenceValues){
            diffMap.addValue(differenceValue, 1L, 0L);
        }

        return diffMap;

    }

}
