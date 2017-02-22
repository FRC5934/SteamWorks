package org.usfirst.frc.team5934.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Climber {
	private final double climberSpeed = 1;
	
	private Victor climber;
	private DoubleSolenoid climberSolenoid;
	
	public Climber(Victor climber, DoubleSolenoid climberSolenoid)
	{
		this.climber = climber;
		this.climberSolenoid = climberSolenoid;
	}
	
	public void manipulateClimber(Joystick xbox, Intake intake)
	{
		if(xbox.getRawButton(9))
		{
			intake.raiseIntake();
			Timer.delay(.05);
			climberSolenoidOut();
		}
		else
		{
			climberSolenoidIn();
		}
		
		if(xbox.getRawButton(9) && xbox.getRawButton(1))
		{
			intake.raiseIntake();
			Timer.delay(.05);
			
			climberStart();
		}
		else
		{
			climberStop();
		}
		
	}

	public void climberSolenoidOut()
	{
		this.climberSolenoid.set(DoubleSolenoid.Value.kForward);
	}
	
	public void climberSolenoidIn()
	{
		this.climberSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void climberStart()
	{
		this.climber.set(climberSpeed);
		//this.climber2.set(climberSpeed);
	}
	
	public void climberStop()
	{
		this.climber.set(0);
		//this.climber2.set(0);
	}

}
