/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.ConfigParameter;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.C_HolonomicDrive;

public class SS_Swerve extends Subsystem {

	private double mAdjustmentAngle = 0;
	private boolean mFieldOriented = true;

    private final double gearRatio = 5.777;

    public static final double WHEELBASE = 23.63;  
    public static final double TRACKWIDTH = 23.5; 
  
    public static final double WIDTH = 21; 
    public static final double LENGTH = 21; 

    private double speedMultiplier = 1;
    PIDController displayPID;
    
    double[] pid = {0, 0, 0};


    
    public SS_Swerve() {
        

        double FR = -13;
        double FL = 67;
        double BR = -2;
        double BL = 82;
        SmartDashboard.putNumber("0 Offset", FR);
        SmartDashboard.putNumber("1 Offset", FL);
        SmartDashboard.putNumber("2 Offset", BR);
        SmartDashboard.putNumber("3 Offset", BL);
        

        mSwerveModules = new SwerveModule[]  {
            new SwerveModule(0, new CANSparkMax(RobotMap.getDriveMotors(0), MotorType.kBrushless), new TalonSRX(RobotMap.getAngleMotors(0)), FL),
            new SwerveModule(1, new CANSparkMax(RobotMap.getDriveMotors(1), MotorType.kBrushless), new TalonSRX(RobotMap.getAngleMotors(1)), FR),
            new SwerveModule(2, new CANSparkMax(RobotMap.getDriveMotors(3), MotorType.kBrushless), new TalonSRX(RobotMap.getAngleMotors(3)), BR),
            new SwerveModule(3, new CANSparkMax(RobotMap.getDriveMotors(2), MotorType.kBrushless), new TalonSRX(RobotMap.getAngleMotors(2)), BL),
        };
        mSwerveModules[3].setDriveInverted(true);
        mSwerveModules[2].setDriveInverted(false);
        mSwerveModules[1].setDriveInverted(false);
        mSwerveModules[0].setDriveInverted(false);
        

        for (SwerveModule module : mSwerveModules) {
            //module.setTargetAngle(0);
            module.setDriveGearRatio(gearRatio);
            // module.setDriveWheelRadius(radius);
        }


    }

    
    public final double getGearRatio(){
        return gearRatio;
    }

	public double getSpeedMultiplier() {
		return speedMultiplier;
    }
    
    public void setSpeedMultiplier( double speed){
        speedMultiplier = speed;
    }

	public double getAdjustmentAngle() {
		return mAdjustmentAngle;
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new C_HolonomicDrive());
	}

	public boolean isFieldOriented() {
		return mFieldOriented;
	}

	public void setAdjustmentAngle(double adjustmentAngle) {
		System.out.printf("New Adjustment Angle: % .3f\n", adjustmentAngle);
		mAdjustmentAngle = adjustmentAngle;
	}

	public void setFieldOriented(boolean fieldOriented) {
		mFieldOriented = fieldOriented;
	}


	
  /////////////////////////////////////////////////////
  /////////////////////////////////////////////////////


  /////////////////////////////////////////////////////


    
	/*
	 *  NOTE: this has since been proven wrong, i honestly have no idea what is front anymore,or what is life
     *  just check if the things moves right, if not play with it till it does
     *  
	 * 0 is Front Right
	 * 1 is Front Left
	 * 2 is Back Left
	 * 3 is Back Right
	 *
     * 
     *   front
     * 1------0
     * |      |
     * |      |
     * 2------3
	 */
	private SwerveModule[] mSwerveModules;

    private double offset = 0;
    private boolean firstTime = true;
    /**
     * @deprecated
     * @param forward
     * @param strafe
     * @param rotation
     * @return
     */
    public double[] calculateSwerveModuleAngles(double forward, double strafe, double rotation) {
        // if (isFieldOriented()) {
        //     double angleRad = Math.toRadians(getGyroAngle());
        //     double temp = forward * Math.cos(angleRad) + strafe * Math.sin(angleRad);
        //     strafe = -forward * Math.sin(angleRad) + strafe * Math.cos(angleRad);
        //     forward = temp;
        // }

        double a = strafe - rotation * (WHEELBASE / TRACKWIDTH);
        double b = strafe + rotation * (WHEELBASE / TRACKWIDTH);
        double c = forward - rotation * (TRACKWIDTH / WHEELBASE);
        double d = forward + rotation * (TRACKWIDTH / WHEELBASE);

        return new double[]{
                Math.atan2(b, c) * 180 / Math.PI,
                Math.atan2(b, d) * 180 / Math.PI,
                Math.atan2(a, d) * 180 / Math.PI,
                Math.atan2(a, c) * 180 / Math.PI
        };
    }


    public SwerveModule getSwerveModule(int i) {
        return mSwerveModules[i];
    }

    public void holonomicDrive(double forward, double strafe, double rotation) {
        // slows everything down
        forward *= getSpeedMultiplier();
        strafe *= getSpeedMultiplier();
        rotation *= getSpeedMultiplier();

        boolean fieldOriented = isFieldOriented();

        // if (fieldOriented) {
        //     double angleRad = Math.toRadians(getGyroAngle());
        //     double temp = forward * Math.cos(angleRad) +
        //             strafe * Math.sin(angleRad);
        //     strafe = -forward * Math.sin(angleRad) + strafe * Math.cos(angleRad);
        //     forward = temp;
        // }

        double a = strafe - rotation * (WHEELBASE / TRACKWIDTH);
        double b = strafe + rotation * (WHEELBASE / TRACKWIDTH);
        double c = forward - rotation * (TRACKWIDTH / WHEELBASE);
        double d = forward + rotation * (TRACKWIDTH / WHEELBASE);

        double[] angles = new double[]{
                Math.atan2(b, c) * 180 / Math.PI,
                Math.atan2(b, d) * 180 / Math.PI,
                Math.atan2(a, d) * 180 / Math.PI,
                Math.atan2(a, c) * 180 / Math.PI
        };

        double[] speeds = new double[]{
                Math.sqrt(b * b + c * c),
                Math.sqrt(b * b + d * d),
                Math.sqrt(a * a + d * d),
                Math.sqrt(a * a + c * c)
        };

        double max = speeds[0];

        for (double speed : speeds) {
            if (speed > max) {
                max = speed;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (Math.abs(forward) > 0.05 ||
                    Math.abs(strafe) > 0.05 ||
                    Math.abs(rotation) > 0.05) {
                mSwerveModules[i].setTargetAngle(angles[i] + 180);
            } else {
                mSwerveModules[i].setTargetAngle(mSwerveModules[i].getTargetAngle());
            }
            mSwerveModules[i].setTargetSpeed(speeds[i]);
        }
    }

    
    public void stopDriveMotors() {
        for (SwerveModule module : mSwerveModules) {
            module.setTargetSpeed(0);
        }
    }
    
    public void resetMotors() {
    	for(int i = 0; i < mSwerveModules.length; i++) {
    		mSwerveModules[i].resetMotor();
    	}
    }

    public SwerveModule[] getSwerveModules() {
        return mSwerveModules;
    }

}
