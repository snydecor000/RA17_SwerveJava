package org.usfirst.frc.team1741.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;

public class Drive 
{
	private static final double PI = 3.14159265358979;
	private CANTalon FR;
	private CANTalon FRa;
	private AnalogInput FRe;
	private PIDController FRc;

	private double SpeedP,SpeedI,SpeedD;
	private double SteerP,SteerI,SteerD;
	private double SteerSpeed,SteerTolerance,SteerEncMax,TurningSpeedFactor,DriveCIMMaxRPM;

	private double length,width,diameter;
	private double temp;
	private double a,b,c,d;
	private double ws1,ws2,ws3,ws4;
	private double wa1,wa2,wa3,wa4;
	private double max;
	
	Drive(CANTalon fr,CANTalon fra, AnalogInput fre)
	{
		SpeedP = Config.GetSetting("speedP",1);
		SpeedI = Config.GetSetting("speedI",0);
		SpeedD = Config.GetSetting("speedD",0);
		SteerP = Config.GetSetting("steerP",2);
		SteerI = Config.GetSetting("steerI",0);
		SteerD = Config.GetSetting("steerD",0);
		SteerTolerance = Config.GetSetting("Steering%Tolerance", .25);
		SteerSpeed = Config.GetSetting("SteerSpeed", 1);
		SteerEncMax = Config.GetSetting("SteerEncMax",4.792);
		TurningSpeedFactor = Config.GetSetting("turningSpeedFactor", 1);
		DriveCIMMaxRPM = Config.GetSetting("driveCIMmaxRPM",4000);
//		SpeedP = 1;
//		SpeedI = 0;
//		SpeedD = 0;
//		SteerP = 1;
//		SteerI = 0.01;
//		SteerD = 0;
//		SteerTolerance = 0.5;
//		SteerSpeed = 1;
//		SteerEncMax = 5;
//		TurningSpeedFactor = 1;
//		DriveCIMMaxRPM = 4000;

		FR = fr;
		FR.setControlMode(0);

		FRa = fra;
		FRa.setControlMode(0);

		FRe = fre;

		FRc = new PIDController(SteerP,SteerI,SteerD,FRe,FRa);
		FRc.disable();
		FRc.setContinuous(true);
		FRc.setInputRange(0,SteerEncMax);
		FRc.setOutputRange(-SteerSpeed,SteerSpeed);
		FRc.setPercentTolerance(SteerTolerance);
		FRc.setSetpoint(2.4);
		FRc.enable();

		length = Config.GetSetting("FrameLength",1);
		width = Config.GetSetting("FrameWidth",1);
		diameter = Math.sqrt(Math.pow(length,2)+Math.pow(width,2));
		temp = 0.0;
		a = 0.0;b = 0.0;c = 0.0;d = 0.0;
		ws1 = 0.0;ws2 = 0.0;ws3 = 0.0;ws4 = 0.0;
		wa1 = 0.0;wa2 = 0.0;wa3 = 0.0;wa4 = 0.0;
		max = 0.0;
	}
	
	void Swerve(double x, double y, double z, double gyro)
	{
		gyro *= PI/180.0f;
		z *= TurningSpeedFactor;

		temp = y * Math.cos(gyro) + x * Math.sin(gyro);
		x = -y * Math.sin(gyro) + x * Math.cos(gyro);
		y = temp;

		a = x - z * (length/diameter);
		b = x + z * (length/diameter);
		c = y - z * (width/diameter);
		d = y + z * (width/diameter);

		ws1 = Math.sqrt(Math.pow(b,2) + Math.pow(c,2));
		ws2 = Math.sqrt(Math.pow(b,2) + Math.pow(d,2));
		ws3 = Math.sqrt(Math.pow(a,2) + Math.pow(d,2));
		ws4 = Math.sqrt(Math.pow(a,2) + Math.pow(c,2));
		if(ws1 > max){max = ws1;}
		if(ws2 > max){max = ws2;}
		if(ws3 > max){max = ws3;}
		if(ws4 > max){max = ws4;}
		if(max > 1){ws1 /= max;ws2 /= max;ws3 /= max;ws4 /= max;}
		FR.set(ws1);

		wa1 = Math.atan2(b,c) * 180.0f/PI;
		wa2 = Math.atan2(b,d) * 180.0f/PI;
		wa3 = Math.atan2(a,d) * 180.0f/PI;
		wa4 = Math.atan2(a,c) * 180.0f/PI;
		if(wa1 < 0){wa1 += 360;}
		if(wa2 < 0){wa2 += 360;}
		if(wa3 < 0){wa3 += 360;}
		if(wa4 < 0){wa4 += 360;}
		FRc.setSetpoint(wa1*(SteerEncMax/360.0f));
		System.out.println("Actual: " + FRe.getVoltage());
		System.out.println("Setpoint: " + FRc.getSetpoint());
	}
	
	void SetupLogging(Logger logger)
	{
		logger.AddAttribute("Time");
		logger.AddAttribute("FRpos");
		logger.AddAttribute("FRCurrent");
		logger.AddAttribute("FRspeed");
		logger.AddAttribute("FRApos");
		logger.AddAttribute("FRACurrent");
		logger.AddAttribute("FREncpos");
		logger.AddAttribute("FREncSetpoint");
	}

	void Log(Logger logger)
	{
		logger.Log("FRpos", FR.getEncPosition());
		logger.Log("FRCurrent", FR.getOutputCurrent());
		logger.Log("FRspeed", FR.getSpeed());
		logger.Log("FRApos", FRa.getEncPosition());
		logger.Log("FRACurrent", FRa.getOutputCurrent());
		logger.Log("FREncpos", FRe.getVoltage());
		logger.Log("FREncSetpoint", (wa1*(SteerEncMax/360.0f)));
		logger.WriteLine();
	}

	void ReloadConfig()
	{
		SpeedP = Config.GetSetting("speedP",1);
		SpeedI = Config.GetSetting("speedI",0);
		SpeedD = Config.GetSetting("speedD",0);
		//FR.setPID(SpeedP,SpeedI,SpeedD);
	/////////////////////////////////////////////////////
		SteerP = Config.GetSetting("steerP",2);
		SteerI = Config.GetSetting("steerI",0);
		SteerD = Config.GetSetting("steerD",0);
		FRc.setPID(SteerP,SteerI,SteerD);
	///////////////////////////////////////////////////////////////////
		SteerTolerance = Config.GetSetting("Steering%Tolerance", 0.25);
		SteerSpeed = Config.GetSetting("SteerSpeed", 1);
		SteerEncMax = Config.GetSetting("SteerEncMax",4.792);

		FRc.setInputRange(0,SteerEncMax);
		FRc.setOutputRange(-SteerSpeed,SteerSpeed);
		FRc.setPercentTolerance(SteerTolerance);
	/////////////////////////////////////////////////////
		TurningSpeedFactor = Config.GetSetting("turningSpeedFactor", 1);
		DriveCIMMaxRPM = Config.GetSetting("driveCIMmaxRPM",4000);
	}
}
