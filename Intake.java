package org.usfirst.frc.team5934.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Intake {

		private final double intakeSpeed = 1;
		private final double agitatorSpeed = 1;
	
		private Victor intake;
		private Victor agitator;
		private DoubleSolenoid intakeSolenoid;
		
		public Intake(Victor intake, Victor agitator, DoubleSolenoid intakeSolenoid)
		{
			this.intake = intake;
			this.intakeSolenoid = intakeSolenoid;
			this.agitator = agitator;
			this.agitator.set(0);
			this.intake.set(0);
		
		}
		
		public void manipulateIntake(Joystick xbox, Joystick leftStick, Joystick rightStick)
		{
			
			
			if(rightStick.getRawButton(2))
			{
				startAgitator();
			}
			
			if(leftStick.getRawButton(2))
			{
				stopAgitator();
			}
			
			if(leftStick.getRawButton(1) || xbox.getRawButton(9))
			{
				System.out.println("This would raise the intake ...");
				raiseIntake();
			}
			
			if(rightStick.getRawButton(1))
			{
				System.out.println("This would lower the intake ...");
				lowerIntake();
			}
			
			if(rightStick.getRawButton(4) || xbox.getRawButton(2))
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


