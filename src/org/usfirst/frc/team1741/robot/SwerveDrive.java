package org.usfirst.frc.team1741.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;

public class SwerveDrive 
{
	private static final double PI = 3.14159265358979;
	private CANTalon FR;
	private CANTalon FRa;
	private CANTalon FL;
	private CANTalon FLa;
	private CANTalon BR;
	private CANTalon BRa;
	private CANTalon BL;
	private CANTalon BLa;
	private AnalogInput FRe;
	private PIDController FRc;
	private FakePIDSource FReFake;
	private AnalogInput FLe;
	private PIDController FLc;
	private FakePIDSource FLeFake;
	private AnalogInput BRe;
	private PIDController BRc;
	private FakePIDSource BReFake;
	private AnalogInput BLe;
	private PIDController BLc;
	private FakePIDSource BLeFake;

	@SuppressWarnings("unused")
	private double SpeedP,SpeedI,SpeedD;
	private double SteerP,SteerI,SteerD;
	@SuppressWarnings("unused")
	private double SteerSpeed,SteerTolerance,SteerEncMax,TurningSpeedFactor,DriveCIMMaxRPM;
	private double SteerOffsetFR;

	private double length,width,diameter;
	private double temp;
	private double a,b,c,d;
	private double ws1,ws2,ws3,ws4;
	private double wa1,wa2,wa3,wa4;
	private double max;
	
	//SwerveDrive(CANTalon fr,CANTalon fra, AnalogInput fre)
	SwerveDrive(int fr, int fra, int fre, int fl, int fla, int fle, int br, int bra, int bre, int bl, int bla, int ble)
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
		SteerOffsetFR = Config.GetSetting("SteerEncOffsetFR",0);

		FR = new CANTalon(fr);
		FR.setControlMode(0);
		FL = new CANTalon(fl);
		FL.setControlMode(0);
		BR = new CANTalon(br);
		BR.setControlMode(0);
		BL = new CANTalon(bl);
		BL.setControlMode(0);

		FRa = new CANTalon(fra);
		FRa.setControlMode(0);
		FLa = new CANTalon(fla);
		FLa.setControlMode(0);
		BRa = new CANTalon(bra);
		BRa.setControlMode(0);
		BLa = new CANTalon(bla);
		BLa.setControlMode(0);

		FRe = new AnalogInput(fre);
		FLe = new AnalogInput(fle);
		BRe = new AnalogInput(bre);
		BLe = new AnalogInput(ble);
		
		FReFake = new FakePIDSource(SteerOffsetFR,0,SteerEncMax);
		FLeFake = new FakePIDSource(SteerOffsetFR,0,SteerEncMax);
		BReFake = new FakePIDSource(SteerOffsetFR,0,SteerEncMax);
		BLeFake = new FakePIDSource(SteerOffsetFR,0,SteerEncMax);

		FRc = new PIDController(SteerP,SteerI,SteerD,FReFake,FRa);
		FRc.disable();
		FRc.setContinuous(true);
		FRc.setInputRange(0,SteerEncMax);
		FRc.setOutputRange(-SteerSpeed,SteerSpeed);
		FRc.setPercentTolerance(SteerTolerance);
		FRc.setSetpoint(2.4);
		FRc.enable();
		FLc = new PIDController(SteerP,SteerI,SteerD,FLeFake,FLa);
		FLc.disable();
		FLc.setContinuous(true);
		FLc.setInputRange(0,SteerEncMax);
		FLc.setOutputRange(-SteerSpeed,SteerSpeed);
		FLc.setPercentTolerance(SteerTolerance);
		FLc.setSetpoint(2.4);
		FLc.enable();
		BRc = new PIDController(SteerP,SteerI,SteerD,BReFake,BRa);
		BRc.disable();
		BRc.setContinuous(true);
		BRc.setInputRange(0,SteerEncMax);
		BRc.setOutputRange(-SteerSpeed,SteerSpeed);
		BRc.setPercentTolerance(SteerTolerance);
		BRc.setSetpoint(2.4);
		BRc.enable();
		BLc = new PIDController(SteerP,SteerI,SteerD,BLeFake,BLa);
		BLc.disable();
		BLc.setContinuous(true);
		BLc.setInputRange(0,SteerEncMax);
		BLc.setOutputRange(-SteerSpeed,SteerSpeed);
		BLc.setPercentTolerance(SteerTolerance);
		BLc.setSetpoint(2.4);
		BLc.enable();

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
		max = 0;
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
		if(wa1 < 0){wa1 += 360;}//wa1 = FL
		if(wa2 < 0){wa2 += 360;}//wa2 = FR
		if(wa3 < 0){wa3 += 360;}//wa3 = BR
		if(wa4 < 0){wa4 += 360;}//wa4 = BL
		FReFake.pidSet(FRe.pidGet());
		FLeFake.pidSet(FLe.pidGet());
		BReFake.pidSet(BRe.pidGet());
		BLeFake.pidSet(BLe.pidGet());
		FRc.setSetpoint(wa2*(SteerEncMax/360.0f));
		
		
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

	void Log()
	{
		Robot.logger.Log("FRpos", FR.getEncPosition());
		Robot.logger.Log("FRCurrent", FR.getOutputCurrent());
		Robot.logger.Log("FRspeed", FR.getSpeed());
		Robot.logger.Log("FRApos", FRa.getEncPosition());
		Robot.logger.Log("FRACurrent", FRa.getOutputCurrent());
		Robot.logger.Log("FREncpos", FRe.getVoltage());
		Robot.logger.Log("FREncSetpoint", FRc.getSetpoint());
	}

	void ReloadConfig()
	{
		length = Config.GetSetting("FrameLength",1);
		width = Config.GetSetting("FrameWidth",1);
	/////////////////////////////////////////////////////
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
	/////////////////////////////////////////////////////
		SteerOffsetFR = Config.GetSetting("SteerEncOffsetFR",0);

		FReFake.setOffset(SteerOffsetFR);
	}
}
