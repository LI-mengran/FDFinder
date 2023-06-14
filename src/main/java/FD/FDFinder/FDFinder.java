package FD.FDFinder;

import FD.DifferenceSet.DifferenceSet;
import FD.DifferenceSet.DifferenceSetBuilder;
import FD.FDs.ApproxFDs;
import FastADC.plishard.PliShard;
import FastADC.plishard.PliShardBuilder;
import de.metanome.algorithms.dcfinder.input.Input;
import de.metanome.algorithms.dcfinder.input.RelationalInput;
import FD.DataIO.DataIO;


public class FDFinder {

    // configure of PliShardBuilder
    private final int shardLength;

    // configure of EvidenceSetBuilder
    private final boolean linear;

    // configure of ApproxCoverSearcher
    private final double threshold;

    private String dataFp;
    private Input input;
    public static int nAttributes;
    private PliShardBuilder pliShardBuilder;
    private DifferenceSetBuilder differenceSetBuilder;

    public FDFinder(double _threshold, int _len, boolean _linear,int _nAttributes){
        this.threshold = _threshold;
        this.shardLength = _len;
        linear = _linear;
        nAttributes = _nAttributes;
    }
    public void buildApproxFDsFromFile(String _dsFp, int sizeLimit){
        System.out.println("INPUT FILE: " + dataFp);
        System.out.println("ERROR THRESHOLD: " + threshold);

        // Pre-process: load input data and build difference set
        long t00 = System.currentTimeMillis();
        differenceSetBuilder = new DifferenceSetBuilder(nAttributes);
        DifferenceSet differenceSet = differenceSetBuilder.buildFromFile(_dsFp, sizeLimit);
        long t_pre = System.currentTimeMillis() - t00;
        System.out.println(" [Attribute] Attribute number: " + nAttributes);
        System.out.println("[FDFinder] Pre-process time: " + t_pre + "ms");
        System.out.println(" [Difference] # of differences: " + differenceSet.size());
        System.out.println(" [Difference] Accumulated difference count: " + differenceSet.getTotalcount());

        // approx difference inversion
        long t20 = System.currentTimeMillis();
        ApproximateFD approximateFD = new ApproximateFD(differenceSet, threshold);
        ApproxFDs approxFDs = approximateFD.buildApproxFDs();
        long t_FD = System.currentTimeMillis() - t20;
        System.out.println("[FDs] # of FDs: " + approxFDs.getPartialFDs().size());
        System.out.println("[FDs] builder time : " + t_FD +"ms");

    }
    public void buildApproxFDs(String _dataFp, int sizeLimit){
        dataFp = _dataFp;
        System.out.println("INPUT FILE: " + dataFp);
        System.out.println("ERROR THRESHOLD: " + threshold);
        DataIO dataIO = new DataIO();

        // Pre-process: load input data
        long t00 = System.currentTimeMillis();
        input = new Input(new RelationalInput(dataFp), sizeLimit);
        dataIO.configure(input.getHeaders());
        pliShardBuilder = new PliShardBuilder(shardLength, input.getParsedColumns());
        PliShard[] pliShards = pliShardBuilder.buildPliShards(input.getIntInput());
        long t_pre = System.currentTimeMillis() - t00;
        System.out.println("[FDFinder] Pre-process time: " + t_pre + "ms");

        //build difference set
        long t10 = System.currentTimeMillis();
        differenceSetBuilder = new DifferenceSetBuilder();
        DifferenceSet differenceSet = differenceSetBuilder.build(pliShards, linear);
        long t_diff = System.currentTimeMillis() - t10;
//        for(Difference difference : differenceSet.getDifferences())
//            System.out.println(difference.getBitSet() + ":" + difference.getDifferenceValue());
        System.out.println(" [Difference] # of differences: " + differenceSet.size());
        System.out.println("[FDFinder] Build differenceSet time: " + t_diff + "ms");

        // approx difference inversion
        long t20 = System.currentTimeMillis();
        ApproximateFD approximateFD = new ApproximateFD(differenceSet, threshold);
        ApproxFDs approxFDs = approximateFD.buildApproxFDs();
        long t_FD = System.currentTimeMillis() - t20;
        dataIO.transfer(approxFDs);
        System.out.println("[FDs] # of FDs: " + approxFDs.getPartialFDs().size());
        System.out.println("[FDs] builder time : " + t_FD +"ms");
        
    }

}
