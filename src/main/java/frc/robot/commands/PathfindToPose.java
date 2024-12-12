package frc.robot.commands;

import java.util.function.Supplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.PathPlannerTrajectory;
import com.pathplanner.lib.util.PPLibTelemetry;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;

public class PathfindToPose extends Command {
    private Supplier<Pose2d> target;
    private Pose2d tolerance;
    private boolean runCommand = false;
    private final DriveSubsystem driveRequire = RobotContainer.getInstance().m_robotDrive;

    private final HolonomicDriveController holonomicDriveController;
    private final PIDController xController;
    private final PIDController yController;
    private final ProfiledPIDController rotController;
    private PathPlannerPath currentPath;
    private PathPlannerTrajectory currentTrajectory;
    private double timeOffset = 0;
    public RunCommand newCommand;

    //Note: Possibel Fix for Invalid Static Reference to DriveSubsys which has been causing the runtime crash
    // Vision Pose Estimation works but gets interefered by "estimated velocities"
    // Sometimes the position gets flipped which is unideal (find fix later)
    public PathfindToPose(Supplier<Pose2d> target, Pose2d tolerance, boolean runCommand) {
        this.target = target;
        this.tolerance = tolerance;
        this.runCommand = runCommand;

        xController = new PIDController(.1, 0, 0);
        yController = new PIDController(.1, 0, 0);

        rotController = new ProfiledPIDController(1, 0, 0, new TrapezoidProfile.Constraints(3.5, 3.5));


        holonomicDriveController = new HolonomicDriveController(xController, yController, rotController);
        holonomicDriveController.setTolerance(this.tolerance);
        addRequirements(driveRequire);
    }


    @Override
    public void execute() {
        currentTrajectory = null;
        timeOffset = 0;

        Pose2d currentPose = driveRequire.getPoseVision();
        Translation2d targetTranslation2d = ((Pose2d) target).getTranslation();
        if (currentPose.getTranslation().getDistance(targetTranslation2d) > 0.25){
            ChassisSpeeds currentSpeeds = driveRequire.getRobotRelativeSpeeds();
            //PPLibTelemetry.setCurrentPose(currentPose);
        }
        PathConstraints constraints = new PathConstraints(3.0, 4.0,
        Units.degreesToRadians(540),
        Units.degreesToRadians(720));

        Command pathfindingCommand = AutoBuilder.pathfindToPose(
        (Pose2d) target,
        constraints,
        0.0, // Goal end velocity in meters/sec
        0.0 // Rotation delay distance in meters. This is how far the robot should travel before attempting to rotate.
        );
        if (runCommand == false){
            pathfindingCommand.end(true);
            System.out.println("PathFinding_Ended_Early");
        } else if (runCommand == true) {
            newCommand = new RunCommand((Runnable) pathfindingCommand, driveRequire);
            newCommand.schedule();
        }

    }

    @Override
    public void end(boolean interrupted) {
        driveRequire.Xmode();
    }

    @Override
    public boolean isFinished() {
        System.out.println(driveRequire.getCurrentPose().getX());
        System.out.println(holonomicDriveController.atReference());
        return holonomicDriveController.atReference();
    }
}

