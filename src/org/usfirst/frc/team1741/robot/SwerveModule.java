package org.usfirst.frc.team1741.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;

public class SwerveModule 
{
	private double SteerP,SteerI,SteerD;
	@SuppressWarnings("unused")
	private double SpeedP,SpeedI,SpeedD;
	@SuppressWarnings("unused")
	private double SteerSpeed,SteerTolerance,SteerEncMax,TurningSpeedFactor,DriveCIMMaxRPM;
	private double SteerOffset;
	
	private CANTalon drive;
	private CANTalon angle;
	private AnalogInput encoder;
	private PIDController PIDc;
	private FakePIDSource encFake;
	
	public SwerveModule(CANTalon d, CANTalon a, AnalogInput e)
	{
		SteerP = Config.GetSetting("steerP",2);
		SteerI = Config.GetSetting("steerI",0);
		SteerD = Config.GetSetting("steerD",0);
		SteerTolerance = Config.GetSetting("Steering%Tolerance", .25);
		SteerSpeed = Config.GetSetting("SteerSpeed", 1);
		SteerEncMax = Config.GetSetting("SteerEncMax",4.792);
		TurningSpeedFactor = Config.GetSetting("turningSpeedFactor", 1);
		DriveCIMMaxRPM = Config.GetSetting("driveCIMmaxRPM",4000);
		SteerOffset = Config.GetSetting("SteerEncOffset",0);
		
		drive = d;
		drive.setControlMode(0);
		
		angle = a;
		angle.setControlMode(0);
		
		encoder = e;
		
		encFake = new FakePIDSource(SteerOffset,0,SteerEncMax);
		
		PIDc = new PIDController(SteerP,SteerI,SteerD,encFake,angle);
		PIDc.disable();
		PIDc.setContinuous(true);
		PIDc.setInputRange(0,SteerEncMax);
		PIDc.setOutputRange(-SteerSpeed,SteerSpeed);
		PIDc.setPercentTolerance(SteerTolerance);
		PIDc.setSetpoint(2.4);
		PIDc.enable();
	}
	
	public void SetDrive(double speed)
	{
		drive.set(speed);
	}
	
	public void PIDSet()
	{
		encFake.pidSet(encoder.pidGet());
	}
	
	public void SetAngle(double angle)
	{
		PIDc.setSetpoint(angle*(SteerEncMax/360.0f));
	}
	
	public void SetupLogging(Logger logger, String s)
	{
		logger.AddAttribute(s + "pos");
		logger.AddAttribute(s + "Current");
		logger.AddAttribute(s + "speed");
		logger.AddAttribute(s + "Apos");
		logger.AddAttribute(s + "ACurrent");
		logger.AddAttribute(s + "Encpos");
		logger.AddAttribute(s + "EncSetpoint");
	}
	
	public void Log(Logger logger, String s)
	{
		logger.Log(s + "pos", drive.getEncPosition());
		logger.Log(s + "Current", drive.getOutputCurrent());
		logger.Log(s + "speed", drive.getSpeed());
		logger.Log(s + "Apos", angle.getEncPosition());
		logger.Log(s + "ACurrent", angle.getOutputCurrent());
		logger.Log(s + "Encpos", encoder.getVoltage() + encFake.m_offset);
		logger.Log(s + "EncSetpoint", PIDc.getSetpoint());
	}
	
	public void ReloadConfig(String s)
	{
	/////////////////////////////////////////////////////
		SpeedP = Config.GetSetting("speedP",1);
		SpeedI = Config.GetSetting("speedI",0);
		SpeedD = Config.GetSetting("speedD",0);
	/////////////////////////////////////////////////////
		SteerP = Config.GetSetting("steerP",2);
		SteerI = Config.GetSetting("steerI",0);
		SteerD = Config.GetSetting("steerD",0);
		PIDc.setPID(SteerP,SteerI,SteerD);
	///////////////////////////////////////////////////////////////////
		SteerTolerance = Config.GetSetting("Steering%Tolerance", 0.25);
		SteerSpeed = Config.GetSetting("SteerSpeed", 1);
		SteerEncMax = Config.GetSetting("SteerEncMax" + s,4.792);
		PIDc.setInputRange(0,SteerEncMax);
		PIDc.setOutputRange(-SteerSpeed,SteerSpeed);
		PIDc.setPercentTolerance(SteerTolerance);
	/////////////////////////////////////////////////////
		TurningSpeedFactor = Config.GetSetting("turningSpeedFactor", 1);
		DriveCIMMaxRPM = Config.GetSetting("driveCIMmaxRPM",4000);
	/////////////////////////////////////////////////////
		SteerOffset = Config.GetSetting("SteerEncOffset" + s,0);
		encFake.setOffset(SteerOffset);
	}
}
