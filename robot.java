package org.usfirst.frc.team5934.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
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
	final String backwardNoTurn = "Backward No Turn";
	final String backwardTurnRight= "Backward & Turn Right";
	final String forwardTurnRight = "Forward & Turn Right";
	String autoSelected;
	
	//Create all the objects!
	SendableChooser<String> chooser = new SendableChooser<>();
	
	/*        PWM
	 *    0 - victorDriveRight
	 *    1 - victorDriveLeft 
	 *    2 - spark775Shooter1
	 *    3 - spark775Shooter2
	 *    4 - victorAgitator   NOT YET INSTALLED
	 *    5 -    
	 *    6 - victorIntake
	 *    7 -
	 *    8 - victorClimber
	 *    9 - victorElevator
	 */
	Victor victorDriveLeft = new Victor(1), victorDriveRight = new Victor(0), 
			victorAgitator = new Victor(4), victorElevator = new Victor(9), victorIntake = new Victor(6),
		    victorClimber = new Victor(8);
	
	Spark spark775Shooter1 = new Spark(2), spark775Shooter2 = new Spark(3);
	
	DoubleSolenoid climberSolenoid = new DoubleSolenoid(6, 7);
	DoubleSolenoid intakeSolenoid = new DoubleSolenoid(0,1);
	
	//Encoders
	Encoder shooterEncoder = new Encoder(0, 1, false, Encoder.EncodingType.k4X);
	Encoder autoEncoder = new Encoder(2, 3, false, Encoder.EncodingType.k4X);
	PIDController goForwardPID = new PIDController(1, 1, 1, autoEncoder, victorDriveLeft);
	double distanceError = 0; 
	double distance_kp = 1.0/15;
	double distance_ki = .2;
	
	
	
	//Robot Objects
	Shooter shooter = new Shooter(victorElevator, spark775Shooter1, spark775Shooter2, shooterEncoder);
	Intake intake = new Intake(victorIntake, victorAgitator, intakeSolenoid);
	RobotDrive mainDrive = new RobotDrive(victorDriveLeft,victorDriveRight);
	Climber climber = new Climber(victorClimber, climberSolenoid);
	
	
	//Extras!
	Joystick xbox = new Joystick(0), leftStick = new Joystick(1), rightStick = new Joystick(2);
	Compressor airCompressor = new Compressor();
	ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	
	//Auto
	Auto auto = new Auto(mainDrive, autoEncoder, gyro);
	boolean distance1Achieved = false;
	boolean angleAchieved = false;
	boolean distance2Achieved = false;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		victorDriveLeft.setInverted(true);
		victorDriveRight.setInverted(true);
		//victorClimber.setInverted(true);
		spark775Shooter1.setInverted(true);
		spark775Shooter2.setInverted(true);
		victorElevator.setInverted(true);
		victorIntake.setInverted(true);
			
		autoEncoder.setDistancePerPulse(.12);
		
		mainDrive.setSafetyEnabled(false);
		
		gyro.calibrate();
		Timer.delay(10);
		gyro.reset();
		
		//Encoders!
		//sampleEncoder.setMaxPeriod(.1);
		//sampleEncoder.setMinRate(10);
		//sampleEncoder.setDistancePerPulse(5);
		//sampleEncoder.setReverseDirection(true);
		//sampleEncoder.setSamplesToAverage(7);
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
		distance1Achieved = false;
		distance2Achieved = false;
		angleAchieved = false;
		gyro.reset();	
		autoEncoder.reset();
		
		if(leftStick.getRawAxis(3) < -0.5)
			{
			autoSelected = backwardTurnRight;
			}
		if(leftStick.getRawAxis(3) > -0.5 && leftStick.getRawAxis(3) < 0.5)
			{
			autoSelected = backwardNoTurn;
			}
		if(leftStick.getRawAxis(3) > 0.5)
			{
			autoSelected = forwardTurnRight;
			}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	
	@Override
	public void autonomousPeriodic() {
		
		SmartDashboard.putNumber("Left Wheel Speed ", victorDriveLeft.get());
		SmartDashboard.putNumber("Right Wheel Speed ", victorDriveRight.get());
		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		SmartDashboard.putString("You picked the Auto : ", autoSelected);
	
		switch (autoSelected) {
		case backwardNoTurn:
			if(!distance1Achieved)
			{
				distance1Achieved = auto.driveBackward(85);
			}
			break;
			
		case backwardTurnRight:
			
			if(!distance1Achieved)
				distance1Achieved = auto.driveBackward(93);
			if(distance1Achieved && !angleAchieved)
				angleAchieved = auto.turnClockwise(60);
			if(distance1Achieved && angleAchieved && !distance2Achieved)
				distance2Achieved = auto.driveBackward(48 + 93);
			break;
			
		case forwardTurnRight:
			if(!distance1Achieved)
				distance1Achieved = auto.driveForward(0);
			if(distance1Achieved && !angleAchieved)
				angleAchieved = auto.turnClockwise(0);
			if(distance1Achieved && angleAchieved&& !distance2Achieved)
				distance2Achieved = auto.driveBackward(0);
			break;
			
			
		default:
			mainDrive.tankDrive(0, 0);
			break;
		
		}
		
	}
	

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		//SmartDashboard.putNumber("Left Wheel Speed ", victorDriveLeft.get());
		//SmartDashboard.putNumber("Right Wheel Speed ", victorDriveRight.get());
		//SmartDashboard.putNumber("Gyro Angle ", gyro.getAngle());
		SmartDashboard.putNumber("Encoder getRate ", shooterEncoder.getRate());
		SmartDashboard.putNumber("Shooter Encoder RPM ", shooter.getShooterRPM());
		SmartDashboard.putNumber("Shooter Speed ", shooter.getShooterSpeed());
		SmartDashboard.putNumber("Drive Encoder Rate", autoEncoder.getRate());
		SmartDashboard.putNumber("Drive Encoder Count", autoEncoder.get());
		SmartDashboard.putNumber("Drive Encoder Distance", autoEncoder.getDistance());
		
		
		
	
		if(rightStick.getRawButton(12))
		{
			autoEncoder.reset();
		}
		
    	airCompressor.start();
    	
    	mainDrive.tankDrive(leftStick.getRawAxis(1), rightStick.getRawAxis(1));
    	Timer.delay(.004);
    	shooter.manipulateShooter(xbox);
    	intake.manipulateIntake(xbox, leftStick, rightStick);
    	climber.manipulateClimber(xbox, intake);
    	
    	
    	/**
    	if(leftStick.getRawButton(5))
    	{
    		gyro.reset();
    	}
    	
    	if(rightStick.getRawButton(5))
    	{
    		gyro.calibrate();
    		Timer.delay(10);
    	}
    	
    	**/
    	
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
		
    	//leftStickSpeed = leftStick.getRawAxis(1) * Math.abs(leftStick.getRawAxis(1));
    	//rightStickSpeed = rightStick.getRawAxis(1) * Math.abs(rightStick.getRawAxis(1));
	}
}

