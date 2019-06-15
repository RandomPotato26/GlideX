package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveModule  {
    private static final long STALL_TIMEOUT = 2000;

    private long stallTimeBegin = Long.MAX_VALUE;

    private double lastError = 0, lastTargetAngle = 0;

    private final int moduleNumber;

    private final double zeroOffset;

    private final TalonSRX angleMotor;
    private final CANSparkMax driveMotor;

    private boolean driveInverted = false;
    private double driveGearRatio = 1;
    private double driveWheelRadius = 2; //4in wheels
    private boolean angleMotorJam = false;


    public SwerveModule(int moduleNumber, CANSparkMax driveMotor, TalonSRX angleMotor, double zeroOffset) {
        this.moduleNumber = moduleNumber;

        this.angleMotor = angleMotor;
        this.driveMotor = driveMotor;
        this.zeroOffset = zeroOffset;

        angleMotor.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 0);
        angleMotor.setSensorPhase(true);
        angleMotor.config_kP(0, 30, 0);
        angleMotor.config_kI(0, 0.001, 0);
        angleMotor.config_kD(0, 300, 0);
    
        angleMotor.setNeutralMode(NeutralMode.Brake);
        
        angleMotor.set(ControlMode.Position, 0);

        driveMotor.setIdleMode(IdleMode.kBrake);

        driveMotor.getPIDController().setP(15);
        driveMotor.getPIDController().setI(.01);
        driveMotor.getPIDController().setD(.1);
        driveMotor.getPIDController().setFF(.2);

        //smooth acceleration
        driveMotor.setOpenLoopRampRate(35);

        // Set amperage limits
        angleMotor.configContinuousCurrentLimit(30, 0);
        angleMotor.configPeakCurrentLimit(30, 0);
        angleMotor.configPeakCurrentDuration(00, 0);
        angleMotor.enableCurrentLimit(true);

        driveMotor.setSmartCurrentLimit(20);
    }

    public void robotDisabledInit() {
        stallTimeBegin = Long.MAX_VALUE;
    }

    public void zeroDistance() {
        
        driveMotor.getEncoder().getPosition();
    }
    
    public void resetMotor() {
    	angleMotorJam = false;
    	stallTimeBegin = Long.MAX_VALUE;
    }

    //both of these conversions are hard coded, sucks ik
    public double encoderTicksToInches(double ticks) {
        //gear ratio * wheel radius
        return ticks * 5.7778 * 2;
    }

    public double inchesToEncoderTicks(double inches) {

        return inches/ (5.7778 * 2) ;

    }

    
    public double getDriveWheelRadius() {
        return driveWheelRadius;
    }

    public TalonSRX getAngleMotor() {
        return angleMotor;
    }

    /**
     * Get the current angle of the swerve module
     *
     * @return An angle in the range [0, 360)
     */
    public double getCurrentAngle() {
        double angle = angleMotor.getSelectedSensorPosition(0) * (360.0 / 1024.0);
        angle -= zeroOffset;
        angle %= 360;
        if (angle < 0) angle += 360;

        return angle;
    }

    //distance stuff needs to be changed
    public double getDrivePos() {
        double ticks = driveMotor.getEncoder().getPosition();
        if(driveInverted)
            ticks = -ticks;
        return ticks;
    }
    public double getDriveInch() {
        return encoderTicksToInches(getDrivePos());
    }

    public CANSparkMax getDriveMotor() {
        return driveMotor;
        
    }

    public double getTargetAngle() {
        return lastTargetAngle;
    }

    /**
     * @param targetAngle Set the target angle of the swerve module (in degrees)
     */
    public void setTargetAngle(double targetAngle) {
//    	if(angleMotorJam) {
//    		mAngleMotor.set(ControlMode.Disabled, 0);
//    		return;
//    	}
        
        lastTargetAngle = targetAngle;

        targetAngle %= 360;


        targetAngle += zeroOffset;

        double currentAngle = angleMotor.getSelectedSensorPosition(0) * (360.0 / 1024.0);
        double currentAngleMod = currentAngle % 360;
        if (currentAngleMod < 0) currentAngleMod += 360;

        double delta = currentAngleMod - targetAngle;

        if (delta > 180) {
            targetAngle += 360;
        } else if (delta < -180) {
            targetAngle -= 360;
        }

        delta = currentAngleMod - targetAngle;
        if (delta > 90 || delta < -90) {
            if (delta > 90)
                targetAngle += 180;
            else if (delta < -90)
                targetAngle -= 180;
            driveMotor.setInverted(false);
        } else {
            driveMotor.setInverted(true);
        }

        targetAngle += currentAngle - currentAngleMod;

        double currentError = angleMotor.getClosedLoopError(0);
//        if (Math.abs(currentError - mLastError) < 7.5 &&
//                Math.abs(currentAngle - targetAngle) > 5) {
//            if (mStallTimeBegin == Long.MAX_VALUE) {
//            	mStallTimeBegin = System.currentTimeMillis();
//            }
//            if (System.currentTimeMillis() - mStallTimeBegin > STALL_TIMEOUT) {
//            	angleMotorJam = true;
//            	mAngleMotor.set(ControlMode.Disabled, 0);
//            	mDriveMotor.set(ControlMode.Disabled, 0);
//            	SmartDashboard.putBoolean("Motor Jammed" + moduleNumber, angleMotorJam);
//            	return;
//            }
//        } else {
//            mStallTimeBegin = Long.MAX_VALUE;
//        }
        lastError = currentError;
        targetAngle *= 1024.0 / 360.0;
        
        SmartDashboard.putNumber("Module Target Angle " + moduleNumber, targetAngle % 360);

        angleMotor.set(ControlMode.Position, targetAngle);
    }        

    public void setDriveWheelRadius(double radius) {
        driveWheelRadius = radius;
    }

    public void setDriveGearRatio(double ratio) {
        driveGearRatio = ratio;
    }

    public void setDriveInverted(boolean inverted) {
        driveInverted = inverted;
    }

    public void setTargetDistance(double inch) {

        if (driveInverted) inch = -inch;
        driveMotor.getPIDController().setReference(inchesToEncoderTicks(inch), ControlType.kPosition);
    }

    public void setTargetSpeed(double speed) {
   	// if(angleMotorJam) {
   	// 	mDriveMotor.set(ControlMode.Disabled, 0);
   	// 	return;
   	// }
        if (driveInverted) speed = -speed;

        driveMotor.set(speed);
    }
}
