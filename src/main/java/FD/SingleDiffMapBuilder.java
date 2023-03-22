package FD;

import FastADC.plishard.Pli;
import FastADC.plishard.PliShard;
import com.koloboke.collect.map.hash.HashLongLongMap;
import com.koloboke.collect.map.hash.HashLongLongMaps;

import java.util.List;

public class SingleDiffMapBuilder {

    private final List<Pli> plis;
    private final int tidBeg, tidRange;
    private final int differenceCount;
    private final int nAttributes;

    public SingleDiffMapBuilder(PliShard shard) {
        plis = shard.plis;
        tidBeg = shard.beg;
        tidRange = shard.end - shard.beg;
        differenceCount = tidRange * tidRange;
        nAttributes = shard.plis.size();
    }

    public HashLongLongMap buildDiffMap(){
        long[] differenceValues = new long[differenceCount];
        HashLongLongMap diffMap = HashLongLongMaps.newMutableMap();


        /** get all differenceValue*/
        /** for every attribute*/
        for(int e = 0; e < nAttributes; e++){
            Pli pliE = plis.get(e);
            long mask = 1L << e;
            /** for every cluster in pli*/
            for(int k = 0; k < pliE.size(); k++){
                if(pliE.get(k).size() > 1){
                    List<Integer> rawCluster = pliE.get(k).getRawCluster();
                    /** for every tuple in cluster*/
                    for(int i = 0; i < rawCluster.size() - 1; i++){
                        int t1 = rawCluster.get(i) - tidBeg, r1 = t1 * tidRange;
                        for (int j = i + 1; j < rawCluster.size(); j++) {
                            int tid2 = rawCluster.get(j) - tidBeg;
                            differenceValues[r1 + tid2] |= mask;               // (cluster.get(i)-tidBeg)*tidRange + (cluster.get(j)-tidBeg)
                            differenceValues[tid2 * tidRange + t1] |= mask;    // (cluster.get(j)-tidBeg)*tidRange + (cluster.get(i)-tidBeg)
                        }
                    }
                }
            }
        }

        /** accumulate differenceValues to differenceSet*/

        /** first put differenceValue and count to diffMap*/
        for(long differenceValue :differenceValues){
            diffMap.addValue(differenceValue, 1L, 0L);
        }

        /** second remove reflex difference*/
        if(0L == diffMap.addValue(0L, -tidRange)){
            diffMap.remove(0L);
        }

        return diffMap;
    }

}
