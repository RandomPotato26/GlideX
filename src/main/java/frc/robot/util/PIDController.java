package frc.robot.util;

/**
 * Self-sufficient PID Controller module.
 * 
 * The great majority of this code was from the codebase of FTC 11120 CPR Eagles from the 2017-18 season.
 * 
 * @see <a href="https://github.com/CPCRobotics/ftc/blob/ftc_2017/TeamCode/src/main/java/org/firstinspires/ftc/teamcode/util/PIDTuner.java">
 *         Github CPCRobotics/ftc PIDTuner.java</a>
 */
public final class PIDController {
    // K-values
    private final double kP;
    private final double kI;
    private final double kD;

    private final double speed; // Maximum speed of device

    private boolean firstTime = true;
    private double errSum = 0;
    private final Stopwatch timer = new Stopwatch();

    private double lastTime = 0;
    private double lastErr = 0;

    /**
     * Limit val to the maximum speed
     * @param val the velocity to limit
     * @return the velocity, corrected
     */
    private double clamp(double val) {
        if (val < -speed) return -speed;
        if (val > speed) return speed;
        return val;
    }

    public PIDController(double speed, double kP, double kI, double kD) {
        this.speed = speed;
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    /**
     * Return the velocity that the device should go, according to the PID controller
     * @param error the total distance from the target, calculated via (targetPosition - realPosition)
     * @return the calculated velocity
     */
    public double get(double error) {
        double curTime = timer.getSeconds();

        double slope = 0;
        if (!firstTime) {
            double dt = curTime - lastTime;

            // Integral portion
            errSum += dt * error;

            // Derivative portion
            slope = (error - lastErr) / dt;
        }

        // Setting up for next iteration
        lastTime = curTime;
        lastErr = error;
        if (firstTime) {
            timer.reset();
            firstTime = false;
        }

        // Clamp sum of all portions to maximum speed and return value
        return clamp(error * kP + errSum * kI + slope * kD);
    }
}
