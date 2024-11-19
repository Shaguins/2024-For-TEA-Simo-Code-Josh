package frc.robot.Bobaboard;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.Autonomous.AutoModeManager.DesiredMode;

public class ControlHub {
    private static ControlHub mInstance = null;

	public static ControlHub getInstance() {
		if (mInstance == null) {
			mInstance = new ControlHub();
        }
		return mInstance;
	}

	public XboxControllerSetup driverController;
	public XboxControllerSetup operatorController;

	private ControlHub() {
		driverController = new XboxControllerSetup(Constants.OIConstants.kDriverControllerPort);
		operatorController = new XboxControllerSetup(Constants.OIConstants.kOperatorControllerPort);
	}


	public void update() {
		driverController.update();
		operatorController.update();
	}

}
