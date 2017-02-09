package org.usfirst.frc.team5934.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Shooter {
	private final double shooterSpeed = .8;
	private final double deadZone = .3;
	private Victor elevator;
	private Spark shooter1;
	private Spark shooter2;
	
	
	
	public Shooter(Victor elevator, Spark shooter1, Spark shooter2)
	{
		this.elevator = elevator;
		this.shooter1 = shooter1;
		this.shooter2 = shooter2;
	}
	
	public void manipulateShooter(Joystick xbox)
	{
    	if(xbox.getRawAxis(2) > deadZone)
    	{
    		spinUpShooterWheel();
    	}
    	else
    	{
    		stopShooterWheel();
    	}
    	
    	if(xbox.getRawAxis(2) > deadZone && xbox.getRawAxis(3) > deadZone)
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
		this.shooter1.set(shooterSpeed);
		this.shooter2.set(shooterSpeed);
	}
    	
	
	public void stopShooterWheel()
	{
		this.shooter1.set(0);
		this.shooter2.set(0);
	}
	
	public void startElevator()
	{
		this.elevator.set(1);
	}
	
	public void stopElevator()
	{
		this.elevator.set(0);
	}

}
