package sharedClasses;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

public class CustomTimer implements Serializable {
    private transient Timer timer; // Use transient to prevent serialization of Timer
    private int timeLeft;

    public CustomTimer() {
        timer = new Timer();
    }

    public void schedule(Runnable action, int delayInSeconds) {
        this.timeLeft = delayInSeconds;

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeLeft--;

                if (timeLeft <= 0) {
                    timer.cancel();
                    action.run();
                }
            }
        }, 0, 1000);
    }

    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        timer.scheduleAtFixedRate(task, delay, period);
    }

    public void cancel() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
}

