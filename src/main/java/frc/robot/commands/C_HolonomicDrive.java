package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
/**
 * commands drives the swerve drivetrain
 */
public class C_HolonomicDrive extends Command {
	private static final double DEAD_ZONE = 0.5;

	public C_HolonomicDrive() {
		requires(Robot.getDrivetrain());
	}
	/**
	 * @param input
	 * @return 0 if the controler input is less than DEAD_ZONE value
	 */
	private double deadband(double input) {
		if (Math.abs(input) < DEAD_ZONE) return 0;
		return input;
	}
	
	/**
	 * initializes command
	 */
	@Override
	protected void initialize() {

	}

	/**
	 * drives the swerve drivetrain based on the controler input values
	 */
	@Override
	protected void execute() {

		// double forward = -Robot.getOI().getPrimaryController().getLeftYValue(); 
		// double strafe = Robot.getOI().getPrimaryController().getLeftXValue();	
		// double rotation = Robot.getOI().getPrimaryController().getRightXValue();

		if(Robot.getOI().getJoystick().getRawButton(1)){
			Robot.getDrivetrain().setSpeedMultiplier(1);
		}
		else{
			Robot.getDrivetrain().setSpeedMultiplier(.2);

		}
		// System.out.println(Robot.getOI().getJoystick().getRawButton(1));
		double forward = Robot.getOI().getJoystick().getY();
		double strafe = Robot.getOI().getJoystick().getX();
		double rotation = Robot.getOI().getJoystick().getZ();
		// System.out.println(rotation);

		
		forward *= Math.abs(forward); 
		strafe *= Math.abs(strafe);
		rotation *= Math.abs(rotation);

		// forward = deadband(forward); 
		// strafe = deadband(strafe);	
		// rotation = deadband(rotation); 

		// puts values of forward, strafe and rotation to the SmartDashboard
		SmartDashboard.putNumber("Forward", forward);  
		SmartDashboard.putNumber("Strafe", strafe);
		SmartDashboard.putNumber("Rotation", rotation);

		// CANSparkMax tests = new CANSparkMax(RobotMap.getDriveMotors(3), MotorType.kBrushless);
		// tests.set(.4);	

		// TalonSRX testt = new TalonSRX(RobotMap.getAngleMotors(3));
		// testt.set( ControlMode.MotionMagic.Velocity, .3);
		Robot.getDrivetrain().holonomicDrive(forward, strafe, rotation); 
	}

	@Override
	protected void end() {
		Robot.getDrivetrain().stopDriveMotors();
	}

	@Override
	protected void interrupted() {
		// end();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
}
