// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Bobaboard.ControlHub;
import frc.robot.commands.DriveToPose;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.HookConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Hook;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

import java.util.List;
import java.util.Optional;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.PathPlannerLogging;

/*
 * This class is where the bulk of the robot (including the subsystems) should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // 
  /*READ ME:
  The robot's subsystems
  */
  public final DriveSubsystem m_robotDrive;
  public final Arm arm;
  public final Hook hook;
  private static RobotContainer instance = null;

   /*READ ME:
  A static instance of the Robot Container with all its contents
  */

  public static RobotContainer getInstance() {
      if(instance == null) instance = new RobotContainer();
      return instance;
  }
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    arm = Arm.getInstance();
    hook = Hook.getInstance();
    m_robotDrive = new DriveSubsystem();

  m_robotDrive.setDefaultCommand(new RunCommand(
      () -> m_robotDrive.drive(
          -MathUtil.applyDeadband(ControlHub.driverController.getLeftY(), OIConstants.kDriveDeadband),
          -MathUtil.applyDeadband(ControlHub.driverController.getLeftX(), OIConstants.kDriveDeadband),
          -MathUtil.applyDeadband(ControlHub.driverController.getRightX(), OIConstants.kDriveDeadband),
          true, true),
      m_robotDrive));
    // Configure default commands
  }


  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
   * subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling
   * passing it to a
   * {@link JoystickButton}.
   */

  public void RunArmPositive(){
    new RunCommand(() -> arm.setOpenLoop(.2), arm);
  }

  public void RunArmNegative(){
    new RunCommand(() -> arm.setOpenLoop(-0.2), arm);
  }

  public void RunHookPositive(){
    new RunCommand(() -> hook.setOpenLoop(.1), hook);
  }

  public void RunHookNegative(){
    new RunCommand(() -> hook.setOpenLoop(-0.1), hook);
  }

  public void IntakeNotePrep(){
    new RunCommand(() -> hook.setHookState(States.HookPos.OPEN), hook);
  }

  public void IntakeNoteStow(){
    new RunCommand(() -> hook.setHookState(States.HookPos.STOW), hook);
  }

  public void ScoreNote(){
    new ParallelCommandGroup(
          new RunCommand(() -> {
            arm.setArmState(States.ArmPos.SCORE);
            }, arm),
          new SequentialCommandGroup(
            new WaitCommand(HookConstants.delay),
            new RunCommand(() -> {
              hook.setHookState(States.HookPos.SCORE);
            }, hook
            )
          )
        );
  }

  public void StowArm(){
    new RunCommand(() -> {
        arm.setArmState(States.ArmPos.STOW); 
        hook.setHookState(States.HookPos.STOW);
       }, arm, hook);
  }

  public void ClimbChain(){
    new RunCommand(() -> {
        arm.setArmState(States.ArmPos.CLIMB_UP); 
       }, arm);
  }

  public void FallOffChain(){
  new RunCommand(() -> {
          arm.setArmState(States.ArmPos.CLIMB_DOWN); 
        }, arm);
  }

  /* READ ME:
     * This command runs the SCORE command for the AMP shot in AUTO
     * By condensing the entire score command into one method we no longer have to keep defining it everywhere and we set the standard for each attempt
     * Utilizes Constants.java for realtive and absoulte scoring encoder values.
     * Parrallel Command Group - The command runs at the same time but we put a time delay to calculate the exact timing
     * We needed the wait command bc we need the momentum from the swinign arm to score into the AMP
     */

  public Command scoreHookDelay() {
    return new ParallelCommandGroup(
          new RunCommand(() -> {
            arm.setArmState(States.ArmPos.SCORE);
            }, arm),
          new SequentialCommandGroup(
            new WaitCommand(HookConstants.delay),
            new RunCommand(() -> {
              hook.setHookState(States.HookPos.SCORE);
            }, hook
            )
          )
        );
  }

  /**
   * Returns the current alliance, with false indicating blue and true indicating red.
   * If there is no alliance, blue alliance is assumed. ie: not in match
   *
   * @return The current alliance of the robot.
   */
  public boolean getAlliance() {
     var alliance = DriverStation.getAlliance();
    if (alliance.isPresent()) {
      return alliance.get() == DriverStation.Alliance.Red;
    }
    return false;
  }

  //Returns isRed or isBlue from FMS @ Start of the Match
  public static Optional<Alliance> isRedAlliance(){
    return DriverStation.getAlliance();
  }

  public Command ampAutoDrive() {
    return new DriveToPose(FieldSetup.allianceAmpEntryPoseSupplier, FieldSetup.ampEntryTolerance);
  }



}


/* 2023-2024 For-TEA-Simo Java Code by:

Kaden J. Chow - Programming Lead - MHS 2026 - https://github.com/Kachow2323
Ronit Barman - Tech Captain - MHS 2024
Joshua Seo - Programming - MHS 2027
Adam Situ - Asst. Programming Lead - MHS 2027
Michelle Y - Asst. Programming Lead - MHS 2025

With Invaluable Help from:
  Mentor John :D
  Mentor Lauren :D
  Mentor Katie :D
  Everyone else from inside and outside of 253
*/
