import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class RMIHistogram extends UnicastRemoteObject implements Binder, RemoteHistogram {
    private int lastHistID;
    private final Map<Integer, int[]> histograms;
    private final ReentrantLock lock = new ReentrantLock();

    public  RMIHistogram() throws RemoteException {
        super();
        this.lastHistID = 0;
        this.histograms = new HashMap<>();
    }
    @Override
    public void bind(String serviceName) {
        try{
            Naming.rebind("rmi://localhost:1099/" + serviceName, this);
        } catch (MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createHistogram(int bins) throws RemoteException {
        int newHistID;

        lock.lock();
        try {
            newHistID = lastHistID++;
        } finally {
            lock.unlock();
        }

        int[] newHist = new int[bins];

        lock.lock();
        try {
            histograms.put(newHistID, newHist);
        } finally {
            lock.unlock();
        }

        return newHistID;
    }

    @Override
    public void addToHistogram(int histogramID, int value) throws RemoteException {
        lock.lock();
        try {
            int[] hist = histograms.get(histogramID);
            if (hist != null) {
                if (value >= 0 && value < hist.length) {
                    hist[value]++;
                } else {
                    throw new RemoteException("Given value is invalid.");
                }
            } else {
                throw new RemoteException("Given histogram ID is invalid.");
            }
        }
        finally {
            lock.unlock();
        }
    }
    @Override
    public int[] getHistogram(int histogramID) throws RemoteException {
        int[] hist = histograms.get(histogramID);

        if(hist != null){
            return hist.clone();
        }
        else {
            throw new RemoteException("Given histogram ID is invalid.");
        }
    }
}
