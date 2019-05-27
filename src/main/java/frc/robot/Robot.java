/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.C_HolonomicDrive;
import frc.robot.subsystems.*;


public class Robot extends TimedRobot {
  public static final boolean PRACTICE_BOT = true;

  private static SS_Swerve ss_Swerve;
  private static OI m_oi;


  

  @Override
  public void robotInit() {
    m_oi = new OI(this);
    ss_Swerve = new SS_Swerve();


		m_oi.registerControls();
    // chooser.addOption("My Auto", new MyAutoCommand());
    

    
  }

  public static OI getOI() {
		return m_oi;
  }

  
  public static SS_Swerve getDrivetrain() {
		return ss_Swerve;///
  }


  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {


    for (int i = 0; i < 4; i++) {
      SmartDashboard.putNumber("Module Angle " + i, ss_Swerve.getSwerveModule(i).getCurrentAngle());
      SmartDashboard.putNumber("Module Pos " + i, (ss_Swerve.getSwerveModule(i).getDriveInch()));
      SmartDashboard.putNumber("Module Raw Angle " + i, ss_Swerve.getSwerveModule(i).getAngleMotor().getSelectedSensorPosition(0));
      
    }

  }

  @Override
  public void disabledInit() {
    for (int i = 0; i < 4; i++) { 
			ss_Swerve.getSwerveModule(i).robotDisabledInit();
		}
  }

  @Override
  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
  }

  @Override
  public void autonomousInit() {
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.

  
    

    for (int i = 0; i < 4; i++)
    ss_Swerve.getSwerveModule(i).zeroDistance();
  }
}
