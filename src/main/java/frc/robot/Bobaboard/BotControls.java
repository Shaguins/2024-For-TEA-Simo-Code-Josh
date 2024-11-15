package frc.robot.Bobaboard;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Hook;

public class BotControls {

	// ControlHub mControlBoard = ControlHub.getInstance();

	static RobotContainer rContainer = RobotContainer.getInstance();
    Arm arm = Arm.getInstance();
    Hook hook = Hook.getInstance();
    DriveSubsystem m_robotDrive = new DriveSubsystem();
    final static SendableChooser<Boolean> ControllerMode = new SendableChooser<>();
    static boolean OneControllerQuery;

    public final static void ChooseControllers(){
        ControllerMode.addOption("One Controller", true);
        ControllerMode.addOption("Two Controller(s)", false);
        SmartDashboard.putData("Controller Selection", ControllerMode);
        Boolean m_ControllerSelected = ControllerMode.getSelected();
        OneControllerQuery = m_ControllerSelected;
    }

    public static void oneControllerMode() {
        if (OneControllerQuery){
            if (ControlHub.driverController.A_Button.wasActivated()) {
                rContainer.FallOffChain();
            }else if (ControlHub.driverController.B_Button.wasActivated()){
                rContainer.ClimbChain();
            }else if (ControlHub.driverController.X_Button.wasActivated()){
                rContainer.StowArm();
            }else if (ControlHub.driverController.Y_Button.wasActivated()){
                rContainer.ScoreNote();
            }else if (ControlHub.driverController.L_Bumper.wasActivated()){
                rContainer.IntakeNotePrep();
            }else if (ControlHub.driverController.R_Bumper.wasActivated()){
                rContainer.IntakeNoteStow();
            }else if (ControlHub.driverController.R_Trigger.wasActivated()){
                rContainer.ampAutoDrive();
            }
        }else{
            if (ControlHub.operatorController.L_Bumper.wasActivated()) {
                rContainer.FallOffChain();
            }else if (ControlHub.operatorController.R_Bumper.wasActivated()){
                rContainer.ClimbChain();
            }else if (ControlHub.operatorController.X_Button.wasActivated()){
                rContainer.StowArm();
            }else if (ControlHub.operatorController.Y_Button.wasActivated()){
                rContainer.ScoreNote();
            }else if (ControlHub.driverController.L_Bumper.wasActivated()){
                rContainer.IntakeNotePrep();
            }else if (ControlHub.driverController.R_Bumper.wasActivated()){
                rContainer.IntakeNoteStow();
            }
        }
    }


}
