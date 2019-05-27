package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.*;
import frc.robot.input.IGamepad;
import frc.robot.input.XboxGamepad;


/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {

    // private IGamepad primaryController = new XboxGamepad(0);
    private Joystick joystick = new Joystick(1);

    private Robot mRobot;

    public OI(Robot robot) {
        mRobot = robot;
    }

    public void registerControls() {
        
        // primaryController.getLeftBumperButton().whenPressed(new C_SetFieldOriented(false));
        // primaryController.getLeftBumperButton().whenReleased(new C_SetFieldOriented( true));
        // primaryController.getStartButton().whenPressed(new C_HolonomicDrive());
        // primaryController.getRightBumperButton().whenPressed(new C_SlowDrivetrain(.2));
        // primaryController.getRightBumperButton().whenReleased(new C_SlowDrivetrain(1));
        
        
        
    }

    public Joystick getJoystick(){
        return joystick;
    }
    // public IGamepad getPrimaryController() {
    //     return primaryController;
    // }

}
