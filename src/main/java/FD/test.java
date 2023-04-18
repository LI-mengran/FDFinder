package FD;

import FD.FDFinder.FDFinder;

public class test {
    public static void main(String[] args) {
//        String fp = "./dataset/tax500k.csv";
//        String fp = "./dataset/atom.csv";
//        String fp = "./dataset/airport3.csv";
//        String fp = "./dataset/ncvoter.csv";
//        String fp = "./dataset/Hospital.csv";
        String fp = "./dataset/food.csv";
//        String fp = "./dataset/flights.csv";

        double threshold = 0.01  ;
        int rowLimit = -1;              // limit the number of tuples in dataset, -1 means no limit
        int shardLength = 400;
        boolean linear = false;         // linear single-thread in DifferenceSetBuilder

        FDFinder fdFinder = new FDFinder(threshold, shardLength, linear);
        fdFinder.buildApproxFDs(fp, rowLimit);
        System.out.println();
    }
}
