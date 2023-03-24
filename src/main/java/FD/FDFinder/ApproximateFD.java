package FD.FDFinder;

import FD.DifferenceSet.Difference;
import FD.DifferenceSet.DifferenceSet;
import FD.FDs.FD;
import FD.FDs.ApproxFDs;
import ch.javasoft.bitset.IBitSet;
import ch.javasoft.bitset.LongBitSet;
//import me.tongfei.progressbar.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ApproximateFD {
    private final DifferenceSet differenceSet;
    private final double error;
    private ApproxCoverTraverser traverser;
    private ApproxFDs approxFDs;
    public ApproximateFD(DifferenceSet differenceSet, double _error){
        error = _error;
        this.differenceSet = differenceSet;
        approxFDs = new ApproxFDs(error);
//        traverser = new ApproxCoverTraverser(differenceSet.getLength(),minCoverCount);
    }
    public ApproxFDs buildApproxFDs(){
        for(int t = 0; t < differenceSet.getLength(); t++) {
            IBitSet rhs = new LongBitSet(differenceSet.getLength());
            rhs.set(t);
            long totalCount = 0;
            List<HyperEdge> partialDifference = new ArrayList<>();

            for (Difference difference : differenceSet.getDifferences()) {
                IBitSet bitset = difference.getBitSet().clone();
                if (!rhs.isSubSetOf(bitset)) continue;
                bitset.clear(t);
                partialDifference.add(new HyperEdge(new LongBitSet(bitset), difference.getCount()));
                totalCount += difference.getCount();
            }

            traverser = new ApproxCoverTraverser(differenceSet.getLength(), totalCount - (int) (error * totalCount));
            List<LongBitSet> minCover = traverser.initiate(partialDifference);
//            System.out.println(rhs + ":" + minCover);
            for (LongBitSet bitset : minCover) {
                approxFDs.add(new FD(bitset, new LongBitSet(rhs)));
            }
        }
        return approxFDs;
    }

    public ApproxFDs getApproxFDs() {
        return approxFDs;
    }

    private List<LongBitSet> minimize( List<LongBitSet> list){
        List<LongBitSet> minilist = new ArrayList<>();
        for(LongBitSet bitset : list){
            List<LongBitSet> cover = new ArrayList<>();
            boolean covered = false;
            for(LongBitSet inserted : minilist){
                if(bitset.isSubSetOf(inserted)){
                    covered = true;
                    break;
                }
                if(inserted.isSubSetOf(bitset)){
                    cover.add(inserted);
                }
            }
            if(covered) continue;
            minilist.add(bitset);
            minilist.removeAll(cover);
        }
        return minilist;
    }

    //    private final App
}
