package org.usfirst.frc.team5934.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Intake {

		private final double intakeSpeed = .8;
		private final double agitatorSpeed = 1;
	
		private Victor intake;
		private Victor agitator;
		private DoubleSolenoid intakeSolenoid;
		
		private int agitatorCounter;
		
		public Intake(Victor intake, Victor agitator, DoubleSolenoid intakeSolenoid)
		{
			this.intake = intake;
			this.intakeSolenoid = intakeSolenoid;
			this.agitator = agitator;
			this.agitatorCounter = 2;
		}
		
		public void manipulateIntake(Joystick leftStick, Joystick rightStick)
		{
			
			if(this.agitatorCounter % 2 == 0)
			{
				startAgitator();
			}
			else
			{
				stopAgitator();
			}
			
			if(leftStick.getRawButton(6))
			{
				this.agitatorCounter++;
				Timer.delay(.05);
			}
			
			if(leftStick.getRawButton(1))
			{
				raiseIntake();
			}
			
			if(rightStick.getRawButton(1))
			{
				lowerIntake();
			}
			
			if(rightStick.getRawButton(4))
			{
				startIntake();
			}
			else
			{
				stopIntake();
			}
		}
		
		public void raiseIntake()
		{
			this.intakeSolenoid.set(DoubleSolenoid.Value.kForward);
		}
		
		public void lowerIntake()
		{
			this.intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
		}
		
		public void startIntake()
		{
			this.intake.set(intakeSpeed);
		}
		
		public void stopIntake()
		{
			this.intake.set(0);
		}
		
		public void startAgitator()
		{
			this.agitator.set(agitatorSpeed);
		}

		public void stopAgitator()
		{
			this.agitator.set(0);
		}
}


