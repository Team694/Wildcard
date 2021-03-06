package org.usfirst.frc.team694.robot.commands;

import org.usfirst.frc.team694.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LiftMoveToBottomCommand extends Command {

    public LiftMoveToBottomCommand() {
//        requires(Robot.lift);
    }

    @Override
    protected void initialize() {
        System.out.println("[LiftMoveToBottom] start!");
    }

    @Override
    protected void execute() {
        Robot.lift.moveRamp(-1);
    }

    @Override
    protected boolean isFinished() {
        return Robot.lift.isAtBottom();
    }

    @Override
    protected void end() {
        Robot.lift.stop();
        System.out.println("[LiftMoveToBottom] STOP");
    }

}
