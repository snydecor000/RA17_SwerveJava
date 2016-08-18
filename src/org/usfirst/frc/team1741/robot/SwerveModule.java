package org.usfirst.frc.team1741.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;

public class SwerveModule 
{
	private double SteerP,SteerI,SteerD;
	
	CANTalon d;
	CANTalon a;
	AnalogInput e;
	
	public SwerveModule(CANTalon drive, CANTalon angle, AnalogInput encoder)
	{
		d = drive;
		a = angle;
		e = encoder;
		d.setControlMode(0);
		a.setControlMode(0);
	}
}
