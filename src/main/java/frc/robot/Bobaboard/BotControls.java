package frc.robot.Bobaboard;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.Hook;

public class BotControls {

	RobotContainer rContainer = RobotContainer.getInstance();
    ControlHub controlHub = ControlHub.getInstance();

    Arm arm = Arm.getInstance();
    Hook hook = Hook.getInstance();

    final static SendableChooser<Boolean> ControllerMode = new SendableChooser<>();
    boolean OneControllerQuery = true;


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

    public final void o_reportBotControlData(){
        SmartDashboard.putBoolean("m_ControllerSelected", OneControllerQuery);
        SmartDashboard.putBoolean("Driver A Button", controlHub.driverController.getFaceButtonA());
        SmartDashboard.putBoolean("Driver B Button", controlHub.driverController.getFaceButtonB());
        SmartDashboard.putBoolean("Driver X Button", controlHub.driverController.getFaceButtonX());
        SmartDashboard.putBoolean("Driver Y Button", controlHub.driverController.getFaceButtonY());
        SmartDashboard.putBoolean("Operator X Button", controlHub.operatorController.getFaceButtonX());
        SmartDashboard.putBoolean("Operator Y Button", controlHub.operatorController.getFaceButtonY());
    }

    public void runControllerMode() {
        if (OneControllerQuery){
            if (controlHub.driverController.getFaceButtonA()) {
                rContainer.FallOffChain();
            }else if (controlHub.driverController.getFaceButtonB()){
                rContainer.ClimbChain();
            }else if (controlHub.driverController.getFaceButtonX()){
                rContainer.StowArm();
            }else if (controlHub.driverController.getYButton()){
                rContainer.ScoreNote();
            }else if (controlHub.driverController.L_Bumper.wasActivated()){
                rContainer.IntakeNotePrep();
            }else if (controlHub.driverController.R_Bumper.wasActivated()){
                rContainer.IntakeNoteStow();
            }
        }else{
            if (controlHub.operatorController.L_Bumper.wasActivated()) {
                rContainer.FallOffChain();
            }else if (controlHub.operatorController.R_Bumper.wasActivated()){
                rContainer.ClimbChain();
            }else if (controlHub.operatorController.getFaceButtonX()){
                rContainer.StowArm();
            }else if (controlHub.operatorController.getFaceButtonY()){
                rContainer.ScoreNote();
            }else if (controlHub.driverController.L_Bumper.wasActivated()){
                rContainer.IntakeNotePrep();
            }else if (controlHub.driverController.R_Bumper.wasActivated()){
                rContainer.IntakeNoteStow();
            }
        }
    }


}
