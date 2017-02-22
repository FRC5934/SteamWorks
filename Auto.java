

package org.usfirst.frc.team5934.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

public class Auto {
	private static double ANGLE_KP = 0.03;
	
	private static double DISTANCE_KI = .2;
	
	
	private RobotDrive mainDrive;
	private Encoder autoEncoder;
	private ADXRS450_Gyro gyro;
	private double distance;
	private double angle;
	private double distanceError;
	private double angleError;
	
	public Auto(RobotDrive mainDrive, Encoder autoEncoder, ADXRS450_Gyro gyro){
		this.mainDrive = mainDrive;
		this.autoEncoder = autoEncoder;
		this.gyro = gyro;
		this.distance = autoEncoder.getDistance();
		this.angle = gyro.getAngle();
		this.distanceError = 0;
		this.angleError = 0;
	}
	
	public boolean turnClockwise(double desiredAngle){
		
		double angle_kp = 1.0 / desiredAngle;
		
		this.angleError = desiredAngle - this.gyro.getAngle();
		this.mainDrive.tankDrive(-angle_kp*this.angleError-.1, angle_kp*this.angleError+.1);
		
		if(angleError < 3)
		{
			this.mainDrive.tankDrive(0.0, 0.0);
			this.autoEncoder.reset();
			this.gyro.reset();
			Timer.delay(2);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	public boolean driveForward(double desiredDistance){
		double distance_kp = 1.0/desiredDistance;
		
		
		this.angle = this.gyro.getAngle();
		this.distance = autoEncoder.getDistance();
		
		this.distanceError = desiredDistance - this.distance;
		this.angle = this.gyro.getAngle();
		this.mainDrive.drive(-distance_kp*distanceError + -DISTANCE_KI, -angle * ANGLE_KP);
		Timer.delay(0.004);
		
		if(this.distanceError < 1.0)
		{
			this.mainDrive.tankDrive(0.0,0.0);
			this.autoEncoder.reset();
			this.gyro.reset();
			Timer.delay(2);
			return true;
		}
		else
		{
			return false;
		}
		
	}
		public boolean driveBackward(double desiredDistance){
			double distance_kp = 1.0/desiredDistance;
			
			
			this.angle = this.gyro.getAngle();
			this.distance = Math.abs(autoEncoder.getDistance());
			
			this.distanceError = desiredDistance - this.distance;
			this.angle = this.gyro.getAngle();
			this.mainDrive.drive((distance_kp*distanceError + DISTANCE_KI)/1.5, angle * ANGLE_KP);
			Timer.delay(0.004);
			
			if(this.distanceError < 1.0)
			{
				this.mainDrive.tankDrive(0.0,0.0);
				this.autoEncoder.reset();
				this.gyro.reset();
				Timer.delay(2);
				return true;
			}
			else
			{
				return false;
			}
		
		
	}

}


