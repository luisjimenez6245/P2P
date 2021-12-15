package proyectop2p.common;

import java.io.Serializable;

public class Tiempo implements Runnable, Serializable {

    private int t;

    public Tiempo() {
        this.t = 30;
    }

    public void setTiempo() {
        this.t = 30;
    }
    public void setTiempo(int t) {
        this.t = t;
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
