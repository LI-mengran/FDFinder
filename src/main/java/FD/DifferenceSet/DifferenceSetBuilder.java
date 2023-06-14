package FD.DifferenceSet;

import FD.util.Utils;
import FastADC.plishard.PliShard;
import ch.javasoft.bitset.IBitSet;
import ch.javasoft.bitset.LongBitSet;
import com.koloboke.collect.map.hash.HashLongLongMap;
import com.koloboke.collect.map.hash.HashLongLongMaps;
import com.koloboke.function.LongLongConsumer;

import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

public class DifferenceSetBuilder {
    private DifferenceSet fullDifferenceSet;
    private int nAttributes;

    public DifferenceSetBuilder(){}

    public DifferenceSet build(PliShard[] pliShards, boolean linear) {

        if (pliShards.length != 0) {
            fullDifferenceSet = linear ? linearBuildDifferenceSet(pliShards) : buildDifferenceSet(pliShards);
        }

        return fullDifferenceSet;
    }
    public DifferenceSetBuilder(int _nAttributes){
        nAttributes = _nAttributes;
    }
    public DifferenceSet buildFromFile(String dsFilePath, int rowLimit){
        List<Difference> differences = new ArrayList<>();

        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dsFilePath))) {
            String s;
            while ((s = br.readLine()) != null)
                lines.add(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(rowLimit > 0){
            String s;
            for(int i = 0; i < rowLimit; i++){
                s = lines.get(i);
                int index = s.indexOf('}');
                LongBitSet bitSet = new LongBitSet();
                for (String str : s.substring(1, index).split(", ")) {
                    if (str != null && str.length() > 0) bitSet.set(Integer.parseInt(str));
                }
                long count = Long.parseLong(s.substring(index + 2));
                differences.add(new Difference(bitSet, count, nAttributes));
            }
        }
        else{
            for (String s : lines) {
                int index = s.indexOf('}');
                LongBitSet bitSet = new LongBitSet();
                for (String str : s.substring(1, index).split(", ")) {
                    if (str != null && str.length() > 0) bitSet.set(Integer.parseInt(str));
                }
                long count = Long.parseLong(s.substring(index + 2));
                differences.add(new Difference(bitSet, count, nAttributes));
            }
        }
        return new DifferenceSet(differences, nAttributes);
    }
    private DifferenceSet linearBuildDifferenceSet(PliShard[] pliShards) {

        HashLongLongMap diffMap = HashLongLongMaps.newMutableMap();
        List<Difference> differences = new ArrayList<>();

        LongLongConsumer add = (k, v) -> diffMap.addValue(k, v, 0L);

        for (int i = 0; i < pliShards.length; i++) {
            for (int j = i; j < pliShards.length; j++) {
                HashLongLongMap partialDiffMap;
                if(i == j){
                    SingleDiffMapBuilder singleDiffMapBuilder = new SingleDiffMapBuilder(pliShards[i]);
                    partialDiffMap = singleDiffMapBuilder.buildDiffMap();
                }
                else{
                    CrossDiffMapBuilder crossDiffMapBuilder = new CrossDiffMapBuilder(pliShards[i], pliShards[j]);
                    partialDiffMap = crossDiffMapBuilder.buildDiffMap();
                }
                partialDiffMap.forEach(add);
            }
        }

        for (var entry : diffMap.entrySet()) {
            differences.add(new Difference(entry.getKey(), entry.getValue(), pliShards[0].plis.size()));
        }


        return new DifferenceSet(differences, pliShards[0].plis.size());
    }


    private DifferenceSet buildDifferenceSet(PliShard[] pliShards) {
        HashLongLongMap diffMap;
        List<Difference> differences = new ArrayList<>();

        int taskCount = (pliShards.length * (pliShards.length + 1)) / 2;

        DifferenceSetTask rootTask = new DifferenceSetTask(null, pliShards, 0, taskCount);
        diffMap = rootTask.invoke();

        for (var entry : diffMap.entrySet()) {
            differences.add(new Difference(entry.getKey(), entry.getValue(),pliShards[0].plis.size()));
//            System.out.println("BitSet: " + Utils.longToBitSet(pliShards[0].plis.size(),entry.getKey()) + "\n Long:" + entry.getKey() + "\n IBitset:" + new LongBitSet(Utils.longToBitSet(pliShards[0].plis.size(),entry.getKey())));
        }

        return new DifferenceSet(differences,pliShards[0].plis.size());
    }
}
