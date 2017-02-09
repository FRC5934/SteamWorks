package org.usfirst.frc.team5934.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Climber {
	private final double climberSpeed = 1;
	
	private Victor climber1;
	private Victor climber2;
	private DoubleSolenoid climberSolenoid;
	
	public Climber(Victor climber1, Victor climber2, DoubleSolenoid climberSolenoid)
	{
		this.climber1 = climber1;
		this.climber2 = climber2;
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
		this.climber1.set(climberSpeed);
		this.climber2.set(climberSpeed);
	}
	
	public void climberStop()
	{
		this.climber1.set(0);
		this.climber2.set(0);
	}

}
