# For-TEA-Simo Code - 2024 Crescendo - SVR & MBR
See [the online changelog](https://github.com/REVrobotics/MAXSwerve-Java-Template/blob/main/CHANGELOG.md) for information about updates to the template that may have been released since you created your project.

## Description
Official 2024 Code for FRC 253

- Auto: PathPlanner Routines with Scoring
- Tele-Op: Field Centric Swerve Drivetrain with WIP Auto-Align, Amp Scoring Only
- Endgame: Climb Only

## Robot Subsystems:
* MAXSwerveModules - Creates the 4 Swerve Modules - DRIVE
* Drive Subsystem - Defines the characteristic of our drive type and setups drive type for Auto + Optimization - DRIVE
* Arm - PIDF Controlled, Relative Encoders, AMP SCORE
* Hook - PIDF Controlled, Absolute Encoders, AMP SCORE
* LEDS - State-Check with Arm to select colors, VISUALS
* Eyes - WIP Vision with RoboRIO - VISION

## Prerequisites
* SPARK MAX Firmware v1.6.2 - Adds features that are required for swerve
* REVLib v2023.1.2 - Includes APIs for the new firmware features
* Template Ver: v202(4).1
