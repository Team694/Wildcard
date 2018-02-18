package org.usfirst.frc.team694.robot.commands;

import org.usfirst.frc.team694.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DrivetrainDriveSystemCommand extends Command {

    boolean tankDrive;
    boolean driveModeToggleButtonWasPressed;
    
    public DrivetrainDriveSystemCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        tankDrive = false;
        driveModeToggleButtonWasPressed = false;
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        
        //Preliminary Values from the triggers used for Curvature Drive
        double rightTrigger = Robot.oi.driverGamepad.getRawRightTriggerAxis();
        double leftTrigger = Robot.oi.driverGamepad.getRawLeftTriggerAxis();

        //Values Used for Curvature Drive
        double rightTriggerSquared = Math.pow(rightTrigger, 3);
        double leftTriggerSquared = Math.pow(leftTrigger, 3);
        double leftJoystickX = Robot.oi.driverGamepad.getLeftX();
        
        //Values used for Tank Drive
        double rightJoystickY = Robot.oi.driverGamepad.getRightY();
        double leftJoystickY = Robot.oi.driverGamepad.getLeftY();
        
        if(Robot.oi.driverGamepad.getRawOptionButton() && !driveModeToggleButtonWasPressed) {
            tankDrive = !tankDrive;
        }
        
        if(!tankDrive) {
            if(Math.abs(-1.0 * rightTrigger + leftTrigger) > 0.05) {
                Robot.drivetrain.curvatureDrive(rightTriggerSquared - leftTriggerSquared, leftJoystickX, false);
            } else {
                Robot.drivetrain.curvatureDrive(rightTriggerSquared - leftTriggerSquared, leftJoystickX, true);
            }   
        }
        if(tankDrive) {
            Robot.drivetrain.tankDrive(leftJoystickY, rightJoystickY);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
