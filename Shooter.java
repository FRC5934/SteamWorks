package org.usfirst.frc.team5934.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
//import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter {
	private final static double DEADZONE = .3;
	private final static int DESIREDRPM = 18000;
	private final static int RPM_MIN = 17000;
	private final static int RPM_MAX = 19000;
	
	private final static double Kp = 1.0/4500;
	private final static double Ki = .85;
	//private final static double Kd = 1.0/1000000;
	
	private int RPM;
	
	private int differential;
	private double previousDifferential;
	private double shooterSpeed;
	//private double integral;
	private double derivative;
	
	private Victor elevator;
	private Spark shooter1;
	private Spark shooter2;
	private Encoder encoder;
	
	
	
	public Shooter(Victor elevator, Spark shooter1, Spark shooter2, Encoder encoder)
	{
		this.elevator = elevator;
		this.shooter1 = shooter1;
		this.shooter2 = shooter2;
		this.encoder = encoder;
		this.RPM = 0;
		
		//this.integral =0;
		this.derivative =0;
		this.shooterSpeed = 0;
		this.differential =1;
		this.previousDifferential=1;
		
		
	}
	
	public void manipulateShooter(Joystick xbox)
	{
		
		SmartDashboard.putNumber("differntial", this.differential);
		SmartDashboard.putNumber("derivative", this.derivative);
		SmartDashboard.putNumber("kp diff", Kp*this.differential);
		SmartDashboard.putNumber("Elevator Value", elevator.get());
		
		
		this.RPM = getShooterRPM(); //Eventually we do some math instead.
		
    	if(xbox.getRawAxis(2) > DEADZONE)
    	{
    		spinUpShooterWheel();
    	}
    	else
    	{
    		stopShooterWheel();
    	}
    	
    	if(xbox.getRawAxis(2) > DEADZONE && xbox.getRawAxis(3) > DEADZONE /*&& checkRPM()*/)
    	{
    		startElevator();
    	}
    	else
    	{
    		stopElevator();
    	}
    }
    	
	public void spinUpShooterWheel()
	{
		this.shooterSpeed = setShooterSpeed();
		this.shooter1.set(this.shooterSpeed);
		this.shooter2.set(this.shooterSpeed);
	}
    	
	
	public void stopShooterWheel()
	{
		this.shooterSpeed = 0;
		this.shooter1.set(shooterSpeed);
		this.shooter2.set(shooterSpeed);
	}
	
	public void startElevator()
	{
		this.elevator.set(1);
	}
	
	public void stopElevator()
	{
		this.elevator.set(0);
	}
	
	public int getShooterRPM()
	{
		 return (int)this.encoder.getRate()*25;
	}
	
	public boolean checkRPM()
	{
		if(this.RPM <= RPM_MAX && this.RPM >= RPM_MIN)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public double setShooterSpeed()
	{
		
		
		  this.differential = DESIREDRPM - getShooterRPM();
		  
		  //this.integral = this.integral + (this.differential*.02);
		  //this.derivative = (this.differential - this.previousDifferential)/.02;
		  double output = (Kp*this.differential) + Ki /*+ (Kd*derivative)*/;
		  this.previousDifferential = this.differential;
	
		  if(output > 1)
		  {
			  output = 1;
		  }
		  if(output <= .6)
		  {
			  output = .6;
		  }
		  
		  return output;
	}
	
	public double getShooterSpeed()
	{
		return this.shooterSpeed;
	}
	

}
