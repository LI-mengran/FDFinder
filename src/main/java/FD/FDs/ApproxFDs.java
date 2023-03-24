package FD.FDs;

import java.util.ArrayList;
import java.util.List;

public class ApproxFDs {
    public final double error;
    private List<FD> partialFDs;
    public ApproxFDs(double error){
        this.error = error;
        this.partialFDs = new ArrayList<>();
    }
    public ApproxFDs(double error, List<FD> partialFDs)
    {
        this.error = error;
        this.partialFDs = partialFDs;
    }

    public  void add(List<FD> fds){
        for(FD fd : fds){
            if(!partialFDs.contains(fd))
                partialFDs.add(fd);
        }
    }

    public void add(FD fd){
        if(!partialFDs.contains(fd))
            partialFDs.add(fd);
    }

    public List<FD> getPartialFDs() {
        return partialFDs;
    }
}
