/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */

/*
    * 0 is Front Right
    * 1 is Front Left
    * 2 is Back Left
    * 3 is Back Right
    * 
    *   front
    * 0------1
    * |      |
    * |      |
    * 2------3
 */
public class RobotMap {
  public static final int ELEVATOR_MASTER_MOTOR = 28;
  public static final int ELEVATOR_SLAVE_MOTOR = 27;

  public static final int CARRIAGE_LEFT_MOTOR = 21;
  public static final int CARRIAGE_RIGHT_MOTOR = 22;

  public static final int getDriveMotors(int module){
    int motors[] = {9,1,13,3};
    int motor = motors[module];
    return  motor;
  }
  
  public static final int getAngleMotors(int module){
    int motors[] = {10,2,14,5};
    int motor = motors[module];
    return  motor;
    
  }
}
