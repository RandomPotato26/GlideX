package frc.robot.util;

/**
 * Measures elapsed time.
 */
public class Stopwatch {
    private long lastTime;

    public Stopwatch() {
        reset();
    }

    /**
     * Reset the stopwatch to measure from the current time.
     */
    public void reset() {
        lastTime = System.currentTimeMillis();
    }

    /**
     * Return the elapsed time in milliseconds
     */
    public long getMilliseconds() {
        return System.currentTimeMillis() - lastTime;
    }

    /**
     * Return the elapsed time in seconds
     */
    public double getSeconds() {
        return getMilliseconds() / 1000.0;
    }
}