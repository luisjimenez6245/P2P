package proyectop2p.common;

public class Tiempo implements Runnable {

    private int t;

    public Tiempo() {
        this.t = 30;
    }
    

    public void setTiempo() {
        this.t = 30;
    }

    public int getTiempo() {
        return this.t;
    }

    @Override
    public void run() {
        try {
            while (t > 0) {
                t--;

                Thread.sleep(1000);
            }
        } catch (Exception e) {
        }
    }
}