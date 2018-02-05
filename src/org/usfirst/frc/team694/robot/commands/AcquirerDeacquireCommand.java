package org.usfirst.frc.team694.robot.commands;

import org.usfirst.frc.team694.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AcquirerDeacquireCommand extends Command {

    public AcquirerDeacquireCommand() {
        requires(Robot.acquirer);
    }
    
    protected void initialize() {
    }

    protected void execute() {
        Robot.acquirer.deacquire();
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
