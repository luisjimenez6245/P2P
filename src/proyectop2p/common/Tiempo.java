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

    private void action() {
        try {
            while (t > 0) {
                t--;

                Thread.sleep(1000);
            }

        } catch (Exception e) {
        }

    }

    @Override
    public void run() {
        action();
    }
}
