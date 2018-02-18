package org.usfirst.frc.team694.robot.commands.auton;

import org.usfirst.frc.team694.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class DriveStraightWithRampingCommand extends PIDCommand {
    
    protected static final double DRIVE_DISTANCE_THRESHOLD = 1;

    protected double output;
    protected double targetDistance;
    protected double startEncoderValue;
    protected static double angleOutput;
    protected static boolean isSet = false;
    protected static double timeFirstInRange;
    PIDController gyroControl;
    public DriveStraightWithRampingCommand(double targetDistance) {
        super(0, 0, 0);
        gyroControl = new PIDController(
                0, 
                0, 
                0, 
                new Source(),
                new Output()
        );
        gyroControl.setSetpoint(0);
        this.targetDistance = targetDistance;
        requires(Robot.drivetrain); 
        
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() { 
        Robot.drivetrain.resetEncoders();
        Robot.drivetrain.resetGyro();
        System.out.println("[DriveStraight] Init");
        //startEncoderValue = Robot.drivetrain.getRightEncoderDistance();
        setSetpoint(targetDistance);
        this.getPIDController().setPID(
                SmartDashboard.getNumber("DriveDistanceEncodersPID P", 0),
                SmartDashboard.getNumber("DriveDistanceEncodersPID I", 0),
                SmartDashboard.getNumber("DriveDistanceEncodersPID D", 0)
                );

        gyroControl.enable();
        gyroControl.setPID(
                SmartDashboard.getNumber("DriveStraightGyroPID P", 0),
                SmartDashboard.getNumber("DriveStraightGyroPID I", 0),
                SmartDashboard.getNumber("DriveStraightGyroPID D", 0)
                );
        //targetDistance = SmartDashboard.getNumber("Test Distance", 170);
        this.getPIDController().setSetpoint(targetDistance);
        double rampSeconds = SmartDashboard.getNumber("DriveStraight RampSeconds", 2.5);
        Robot.drivetrain.setRamp(rampSeconds);
        this.getPIDController().setAbsoluteTolerance(DRIVE_DISTANCE_THRESHOLD);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        //System.out.println("[DriveDistanceEncodersCommand] distance left:" + (targetDistance - Robot.drivetrain.getEncoderDistance()));
        SmartDashboard.putNumber("DriveStraight Encoder Vel", Robot.drivetrain.getSpeed());
        //System.out.println(angleOutput);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {

        boolean inRange = Math.abs(Robot.drivetrain.getRightEncoderDistance() - targetDistance) <= 1;
        //return (Robot.drivetrain.getRightEncoderDistance() > targetDistance && Math.abs(output) < PID_CLOSE_ENOUGH_THRESHOLD);
        if(inRange && !isSet) {
            System.out.println("[DriveStraight] SET!");
            timeFirstInRange = Timer.getFPGATimestamp();
            isSet = true;
        } else if(!inRange){
            isSet = false;
        }
        return inRange && Timer.getFPGATimestamp() - timeFirstInRange > 2;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.drivetrain.tankDrive(0, 0);
        gyroControl.disable();
        SmartDashboard.putNumber("DriveStraight FINAL Right Distance", Robot.drivetrain.getRightEncoderDistance());
        SmartDashboard.putNumber("DriveStraight FINAL Left Distance", Robot.drivetrain.getLeftEncoderDistance());
        this.getPIDController().setPID(0, 0, 0);
        gyroControl.setPID(0, 0, 0);
        System.out.println("[DriveStraight] END");
        Robot.drivetrain.setRamp(0);
    }

    @Override
    protected double returnPIDInput() {
        //return Robot.drivetrain.getRightEncoderDistance() + startEncoderValue;
        return Robot.drivetrain.getRightEncoderDistance();
    }
    protected double returnPIDInputGyro() {
        return Robot.drivetrain.getGyroAngle();
    }
    @Override
    protected void usePIDOutput(double output) {
        //if (!isFinished()) {
            Robot.drivetrain.tankDrive(output + DriveStraightWithRampingCommand.angleOutput, output - DriveStraightWithRampingCommand.angleOutput);
            this.output = output;
        //}
    }
    protected class Source implements PIDSource {
        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return PIDSourceType.kDisplacement;
        }

        @Override
        public double pidGet() {
            return returnPIDInputGyro();
        }       
    }
    protected class Output implements PIDOutput {
        @Override
        public void pidWrite(double output) {
            DriveStraightWithRampingCommand.angleOutput = output;
        }       
    }
}

// P of 0.006, and a D of 0.03
//For turning, make it 0.04
