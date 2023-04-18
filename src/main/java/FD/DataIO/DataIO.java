package FD.DataIO;

import FD.FDs.ApproxFDs;
import FD.FDs.FD;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class DataIO {
    private String[] headers;
    private int columnCount;

    public DataIO(){

    }

    public void configure(String[] headers){
        this.headers = headers;
    }

    public void transfer(ApproxFDs approxFDs){
        for(FD fd : approxFDs.getPartialFDs()){
            if(fd == null)continue;
            BitSet lhs = fd.getLhs().toBitSet();
            BitSet rhs = fd.getRhs().toBitSet();
            List<String> lhsString = new ArrayList<>();
            for(int bit = lhs.nextSetBit(0); bit >= 0; bit = lhs.nextSetBit(bit + 1)){
                lhsString.add(headers[bit]);
            }
            System.out.println(lhsString + "->" + headers[rhs.nextSetBit(0)]);

        }
    }
}
