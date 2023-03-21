package FD;

import FastADC.plishard.PliShard;
import FastADC.plishard.PliShardBuilder;
import de.metanome.algorithms.dcfinder.input.Input;
import de.metanome.algorithms.dcfinder.input.RelationalInput;

public class FDFinder {

    // configure of PliShardBuilder
    private final int shardLength;

    // configure of EvidenceSetBuilder
    private final boolean linear;

    // configure of ApproxCoverSearcher
    private final double threshold;

    private String dataFp;
    private Input input;
    private PliShardBuilder pliShardBuilder;
    private DifferenceSetBuilder differenceSetBuilder;

    public FDFinder(double _threshold, int _len, boolean _linear){
        this.threshold = _threshold;
        this.shardLength = _len;
        linear = _linear;
    }

    public void buildApproxFDs(String _dataFp, int sizeLimit){
        dataFp = _dataFp;
        System.out.println("INPUT FILE: " + dataFp);
        System.out.println("ERROR THRESHOLD: " + threshold);

        // Pre-process: load input data
        long t00 = System.currentTimeMillis();
        input = new Input(new RelationalInput(dataFp), sizeLimit);
        pliShardBuilder = new PliShardBuilder(shardLength, input.getParsedColumns());
        PliShard[] pliShards = pliShardBuilder.buildPliShards(input.getIntInput());
        long t_pre = System.currentTimeMillis() - t00;
        System.out.println("[FDFinder] Pre-process time: " + t_pre + "ms");

        //build difference set
        long t10 = System.currentTimeMillis();
        differenceSetBuilder = new DifferenceSetBuilder();
        DifferenceSet differenceSet = differenceSetBuilder.build(pliShards, linear);
        long t_diff = System.currentTimeMillis() - t10;
        System.out.println(" [Difference] # of differences: " + differenceSet.size());
        System.out.println("[FDFinder] Build differenceSet time: " + t_diff + "ms");

        // approx difference inversion
    }

}
