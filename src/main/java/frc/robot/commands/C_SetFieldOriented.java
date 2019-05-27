package frc.robot.commands;


import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.SS_Swerve;;
/**
 * sets field orientation
 */

public final class C_SetFieldOriented extends Command {

    private boolean fieldOriented;

    @Deprecated
    public C_SetFieldOriented(boolean fieldOriented) {
        requires(Robot.getDrivetrain());
        this.fieldOriented = fieldOriented;
    }

    /**
     * sets field orientation to fieldOriented
     */
    @Override
    protected void execute() {
        Robot.getDrivetrain().setFieldOriented(fieldOriented);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }
}
