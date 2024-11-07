package frc.robot.subsystems;

import java.lang.reflect.Array;

import edu.wpi.first.apriltag.AprilTagDetection;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.RawFiducial;
import frc.utils.CoordinateSpace;

public class Eyes {
      
    // public static Array returnRawFiduicals{
    //     RawFiducial[] fiducials = LimelightHelpers.getRawFiducials("");
    // for (RawFiducial fiducial : fiducials) {
    //     int id = fiducial.id;                    // Tag ID
    //     double txnc = fiducial.txnc;             // X offset (no crosshair)
    //     double tync = fiducial.tync;             // Y offset (no crosshair)
    //     double ta = fiducial.ta;                 // Target area
    //     double distToCamera = fiducial.distToCamera;  // Distance to camera
    //     double distToRobot = fiducial.distToRobot;    // Distance to robot
    //     double ambiguity = fiducial.ambiguity;   // Tag pose ambiguity
    // }
    //     return fiducials;
    // }

    

    // 1. Compute direction offset from tag
    public static double widthOffset(AprilTagDetection aprilTag, CoordinateSpace coordinateSpace) {
        double coordinateCenter = coordinateSpace.width/2;
        double aprilTagcenter = aprilTag.getCenterX();
        double offset = aprilTagcenter - coordinateCenter;
        return offset; // / coordinateSpace.width
    }

    // 2. turn offset into movement direction
    public static double rotationRate(double offset)
    {
        double zeroAccuracy = 0.04;
        //double multipliedOffset = 200*offset; 
        SmartDashboard.putNumber("Offset", offset);
        if (offset < zeroAccuracy && offset > -zeroAccuracy) {
            SmartDashboard.putNumber("Centered", 1);
            return 0.0;
            
        } else if (offset < 0) {
            SmartDashboard.putNumber("Going right", 1);
            return 0.1;
        } else {
            SmartDashboard.putNumber("Going left", 1);
            return -0.1;
        }
        
    }
}