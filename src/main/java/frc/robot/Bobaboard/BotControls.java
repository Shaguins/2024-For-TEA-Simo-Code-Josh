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
    static ControlHub controlHub = ControlHub.getInstance();
    final static SendableChooser<Boolean> ControllerMode = new SendableChooser<>();
    static boolean OneControllerQuery = true;


    public final void PutControllerOption(){
        ControllerMode.addOption("One Controller", true);
        ControllerMode.addOption("Two Controller(s)", false);
        SmartDashboard.putData("Controller Selection", ControllerMode);
    }

    public final void selectControllerOption(){
        if (ControllerMode.getSelected() == null){
            OneControllerQuery = true;
        }else{
        Boolean m_ControllerSelected = ControllerMode.getSelected();
        OneControllerQuery = m_ControllerSelected;
        }
    }


    public void oneControllerMode() {
        if (OneControllerQuery){
            if (controlHub.driverController.A_Button.wasActivated() == true) {
                rContainer.FallOffChain();
            }else if (controlHub.driverController.B_Button.wasActivated() == true){
                rContainer.ClimbChain();
            }else if (controlHub.driverController.X_Button.wasActivated() == true){
                rContainer.StowArm();
            }else if (controlHub.driverController.Y_Button.wasActivated() == true){
                rContainer.ScoreNote();
            }else if (controlHub.driverController.L_Bumper.wasActivated() == true){
                rContainer.IntakeNotePrep();
            }else if (controlHub.driverController.R_Bumper.wasActivated() == true){
                rContainer.IntakeNoteStow();
            }
        }else{
            if (controlHub.operatorController.L_Bumper.wasActivated() == true) {
                rContainer.FallOffChain();
            }else if (controlHub.operatorController.R_Bumper.wasActivated() == true){
                rContainer.ClimbChain();
            }else if (controlHub.operatorController.X_Button.wasActivated() == true){
                rContainer.StowArm();
            }else if (controlHub.operatorController.Y_Button.wasActivated() == true){
                rContainer.ScoreNote();
            }else if (controlHub.driverController.L_Bumper.wasActivated() == true){
                rContainer.IntakeNotePrep();
            }else if (controlHub.driverController.R_Bumper.wasActivated() == true){
                rContainer.IntakeNoteStow();
            }
        }
    }


}
