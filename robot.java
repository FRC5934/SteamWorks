package org.usfirst.frc.team5934.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	
	//Create all the objects!
	SendableChooser<String> chooser = new SendableChooser<>();
	
	/*        PWM
	 *    0 - victorDriveRight
	 *    1 - victorDriveLeft 
	 *    2 - spark775Shooter1
	 *    3 - spark775Shooter2
	 *    4 - victorAgitator   NOT YET INSTALLED
	 *    5 - victorElevator   NOT YET INSTALLED
	 *    6 - victorIntake
	 *    7 -
	 *    8 - victorClimber1 Hey! This is defective!
	 *    9 - victorClimber2
	 */
	Victor victorDriveLeft = new Victor(1), victorDriveRight = new Victor(0), 
			victorAgitator = new Victor(4), victorElevator = new Victor(5), victorIntake = new Victor(6),
			victorClimber1 = new Victor(8), victorClimber2 = new Victor(9);
	
	Spark spark775Shooter1 = new Spark(2), spark775Shooter2 = new Spark(3);
	
	DoubleSolenoid climberSolenoid = new DoubleSolenoid(0, 1);
	DoubleSolenoid intakeSolenoid = new DoubleSolenoid(6,7);
	
	//Robot Objects
	Shooter shooter = new Shooter(victorElevator, spark775Shooter1, spark775Shooter2);
	Intake intake = new Intake(victorIntake, victorAgitator, intakeSolenoid);
	RobotDrive mainDrive = new RobotDrive(victorDriveLeft,victorDriveRight);
	Climber climber = new Climber(victorClimber1, victorClimber2, climberSolenoid);
	
	//Extras!
	Joystick xbox = new Joystick(0), leftStick = new Joystick(1), rightStick = new Joystick(2);
	Compressor airCompressor = new Compressor();
	ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	static float Kp = (float) 0.03;
	double angle;
	double leftStickSpeed = 0;
	double rightStickSpeed = 0;
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto choices", chooser);
		
		victorDriveLeft.setInverted(true);
		victorDriveRight.setInverted(true);
		victorClimber1.setInverted(true);
		victorClimber2.setInverted(true);
		
		//gyro.calibrate();
		//Timer.delay(10);
		gyro.reset();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autoSelected = chooser.getSelected();
		autoSelected = SmartDashboard.getString("Auto Selector",
		defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		gyro.reset();	
	}

	/**
	 * This function is called periodically during autonomous
	 */
	
	@Override
	public void autonomousPeriodic() {
		
		SmartDashboard.putNumber("Left Wheel Speed ", victorDriveLeft.get());
		SmartDashboard.putNumber("Right Wheel Speed ", victorDriveRight.get());
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		
		if(Timer.getMatchTime() >= 10)
		{
		angle = gyro.getAngle();
		mainDrive.drive(-.4, -angle * Kp);
    	Timer.delay(0.004);
		}
		
		if(Timer.getMatchTime() >= 7 && Timer.getMatchTime() < 10 && gyro.getAngle() < 60)
		{
			mainDrive.tankDrive(-.7, .7);
		}
		
		if(Timer.getMatchTime() >= 6.8 && Timer.getMatchTime() < 7)
		gyro.reset();
		
		if(Timer.getMatchTime() >= .5 && Timer.getMatchTime() < 6.8)
		{
		angle = gyro.getAngle();
		mainDrive.drive(-.4, -angle * Kp);
    	Timer.delay(0.004);
		}

		/*
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		
		}
		*/
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		SmartDashboard.putNumber("Left Wheel Speed ", victorDriveLeft.get());
		SmartDashboard.putNumber("Right Wheel Speed ", victorDriveRight.get());
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
    	
    	airCompressor.start();
    	
    	leftStickSpeed = leftStick.getRawAxis(1) * Math.abs(leftStick.getRawAxis(1));
    	rightStickSpeed = rightStick.getRawAxis(1) * Math.abs(rightStick.getRawAxis(1));
    	
    	mainDrive.tankDrive(leftStickSpeed, rightStickSpeed);
    	shooter.manipulateShooter(xbox);
    	intake.manipulateIntake(leftStick, rightStick);
    	climber.manipulateClimber(xbox, intake);
    	
    	if(leftStick.getRawButton(5))
    	{
    		gyro.reset();
    	}
    	
    	if(rightStick.getRawButton(5))
    	{
    		gyro.calibrate();
    		Timer.delay(10);
    	}
    	
	}
	

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {
    	//double leftJoyX =xbox.getRawAxis(0);
    	//double leftJoyY =xbox.getRawAxis(1);
    	//double leftTrigger = xbox.getRawAxis(2);
    	//double rightTrigger = xbox.getRawAxis(3);
	}
}

