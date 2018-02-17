package org.usfirst.frc.team694.robot.commands.auton;


import org.usfirst.frc.team694.robot.FieldMapInterface;
import org.usfirst.frc.team694.robot.Robot;
import org.usfirst.frc.team694.robot.commands.DriveStraightWithRampingCommand;
import org.usfirst.frc.team694.robot.commands.DrivetrainLineSensorReachNullCommand;
import org.usfirst.frc.team694.robot.commands.DrivetrainRotateDegreesPIDCommand;
import org.usfirst.frc.team694.robot.commands.GrabberOpenCommand;
import org.usfirst.frc.team694.robot.commands.LiftMoveToHeightCommand;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SameSideRightAngleAutonCommand extends CommandGroup {
    public FieldMapInterface Quad = Robot.getRobotQuadrant();
    public double speed = 0.5;

    public SameSideRightAngleAutonCommand() {
        addParallel(new DrivetrainLineSensorReachNullCommand());
        addSequential(new DriveStraightWithRampingCommand(Quad.getTotalDistanceFromFrontOfBotToNullBump()));
      
        addSequential(new DrivetrainRotateDegreesPIDCommand(Quad.getDistanceFromRobotAfterTwoTurnsToNullTerritory()));
        
        addSequential(new DriveStraightWithRampingCommand(Quad.getDistanceToMoveBackward()));
        
        addSequential(new LiftMoveToHeightCommand(84)); //Unsure about height
        
        addSequential(new DriveStraightWithRampingCommand(Quad.getDistanceToReachScaleEdge())); 
        
        addSequential(new GrabberOpenCommand());
    }
}
