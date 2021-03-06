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
	private int movecount = 0;

	@SuppressWarnings("unused")
	private double SpeedP,SpeedI,SpeedD;
	private double SteerP,SteerI,SteerD;
	@SuppressWarnings("unused")
	private double SteerSpeed,SteerTolerance,SteerEncMax,TurningSpeedFactor,DriveCIMMaxRPM;
	private double SteerOffsetFR,SteerOffsetFL,SteerOffsetBR,SteerOffsetBL;

	private double length,width,diameter;
	private double temp;
	private double a,b,c,d;
	private double ws1,ws2,ws3,ws4;
	private double wa1,wa2,wa3,wa4;
	private double max;
	@SuppressWarnings("unused")
	private double frEnc,flEnc,brEnc,blEnc;
	private double frEncPos,flEncPos,brEncPos,blEncPos;
	private double frEncSetpoint,flEncSetpoint,brEncSetpoint,blEncSetpoint;
	private double SteerEncMaxFR,SteerEncMaxFL,SteerEncMaxBR,SteerEncMaxBL;
	
	//SwerveDrive(CANTalon fr,CANTalon fra, AnalogInput fre)
	SwerveDrive(CANTalon fr, CANTalon fra, AnalogInput fre, CANTalon fl, CANTalon fla, AnalogInput fle, CANTalon br, CANTalon bra, AnalogInput bre, CANTalon bl, CANTalon bla, AnalogInput ble)
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
		SteerOffsetFL = Config.GetSetting("SteerEncOffsetFL",0);
		SteerOffsetBR = Config.GetSetting("SteerEncOffsetBR",0);
		SteerOffsetBL = Config.GetSetting("SteerEncOffsetBL",0);

		FR = fr;
		FR.setControlMode(0);
		FR.enableBrakeMode(true);
		FL = fl;
		FL.setControlMode(0);
		FL.enableBrakeMode(true);
		BR = br;
		BR.setControlMode(0);
		BR.enableBrakeMode(true);
		BL = bl;
		BL.setControlMode(0);
		BL.enableBrakeMode(true);

		FRa = fra;
		FRa.setControlMode(0);
		FLa = fla;
		FLa.setControlMode(0);
		BRa = bra;
		BRa.setControlMode(0);
		BLa = bla;
		BLa.setControlMode(0);

		FRe = fre;
		FLe = fle;
		BRe = bre;
		BLe = ble;
		
		SteerEncMaxFR = Config.GetSetting("SteerEncMaxFR",0);
		SteerEncMaxFL = Config.GetSetting("SteerEncMaxFL",0);
		SteerEncMaxBR = Config.GetSetting("SteerEncMaxBR",0);
		SteerEncMaxBL = Config.GetSetting("SteerEncMaxBL",0);
		
		FReFake = new FakePIDSource(SteerOffsetFR,0,SteerEncMaxFR);
		FLeFake = new FakePIDSource(SteerOffsetFL,0,SteerEncMaxFL);
		BReFake = new FakePIDSource(SteerOffsetBR,0,SteerEncMaxBR);
		BLeFake = new FakePIDSource(SteerOffsetBL,0,SteerEncMaxBL);

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
		frEncPos = 0;flEncPos = 0;brEncPos = 0;blEncPos = 0;
		frEncSetpoint = 0;flEncSetpoint = 0;brEncSetpoint = 0;blEncSetpoint = 0;
	}
	
	void Swerve(double x, double y, double z, double gyro, boolean fieldOrient)
	{
		gyro *= PI/180.0f;
		z *= TurningSpeedFactor;
		if((x!=0 || y!=0) || z!=0)
		{
			movecount = 100;
			//System.out.println("go");
			if(fieldOrient)
			{
				temp = y * Math.cos(gyro) + x * Math.sin(gyro);
				x = -y * Math.sin(gyro) + x * Math.cos(gyro);
				y = temp;
			}
	
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

	
			wa1 = Math.atan2(b,c) * 180.0f/PI;
			wa2 = Math.atan2(b,d) * 180.0f/PI;
			wa3 = Math.atan2(a,d) * 180.0f/PI;
			wa4 = Math.atan2(a,c) * 180.0f/PI;
			if(wa1 < 0){wa1 += 360;}//wa1 = FL
			if(wa2 < 0){wa2 += 360;}//wa2 = FR
			if(wa3 < 0){wa3 += 360;}//wa3 = BR
			if(wa4 < 0){wa4 += 360;}//wa4 = BL
			
			//System.out.println(wa1 + " " + wa2 + " " + wa3 + " " + wa4);
			FReFake.pidSet(FRe.pidGet());
			FLeFake.pidSet(FLe.pidGet());
			BReFake.pidSet(BRe.pidGet());
			BLeFake.pidSet(BLe.pidGet());
			frEnc = FReFake.pidGet();
			flEnc = FLeFake.pidGet();
			brEnc = BReFake.pidGet();
			blEnc = BLeFake.pidGet();
			//System.out.println(wa2 + " " + (FReFake.pidGet()/(SteerEncMaxFR/360.0f)));
			if(Math.abs(FReFake.pidGet()/(SteerEncMaxFR/360.0f) - wa2) > 90)
			{
				//System.out.println("switch " + Math.abs(FReFake.pidGet()/(SteerEncMaxFR/360.0f) - wa2));
				wa2 = (wa2 + 180)%360;
				ws2 = -ws2;
			}
			if(Math.abs(FLeFake.pidGet()/(SteerEncMaxFL/360.0f) - wa1) > 90)
			{
				wa1 = (wa1 + 180)%360;
				ws1 = -ws1;
			}
			if(Math.abs(BReFake.pidGet()/(SteerEncMaxBR/360.0f) - wa3) > 90)
			{
				wa3 = (wa3 + 180)%360;
				ws3 = -ws3;
			}
			if(Math.abs(BLeFake.pidGet()/(SteerEncMaxBL/360.0f) - wa4) > 90)
			{
				wa4 = (wa4 + 180)%360;
				ws4 = -ws4;
			}
			FR.set(ws2);
			FL.set(-ws1);
			BR.set(ws3);
			BL.set(-ws4);
			FRc.setSetpoint(wa2*(SteerEncMaxFR/360.0f));
			FLc.setSetpoint(wa1*(SteerEncMaxFL/360.0f));
			BRc.setSetpoint(wa3*(SteerEncMaxBR/360.0f));
			BLc.setSetpoint(wa4*(SteerEncMaxBL/360.0f));
		}
		else
		{
			movecount--;
			System.out.println(movecount);
			if(movecount < 0)
			{
				a = 0 - 0 * (length/diameter);
				b = 0 + 0 * (length/diameter);
				c = 0 - 0 * (width/diameter);
				d = 0 + 0 * (width/diameter);
		
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
				frEnc = FReFake.pidGet();
				flEnc = FLeFake.pidGet();
				brEnc = BReFake.pidGet();
				blEnc = BLeFake.pidGet();
				FR.set(ws2);
				FL.set(-ws1);
				BR.set(ws3);
				BL.set(-ws4);
				FRc.setSetpoint(wa2*(SteerEncMaxFR/360.0f));
				FLc.setSetpoint(wa1*(SteerEncMaxFL/360.0f));
				BRc.setSetpoint(wa3*(SteerEncMaxBR/360.0f));
				BLc.setSetpoint(wa4*(SteerEncMaxBL/360.0f));
			}
			else
			{
				FR.set(0);
				FL.set(0);
				BR.set(0);
				BL.set(0);
				FReFake.pidSet(FRe.pidGet());
				FLeFake.pidSet(FLe.pidGet());
				BReFake.pidSet(BRe.pidGet());
				BLeFake.pidSet(BLe.pidGet());
				frEnc = FReFake.pidGet();
				flEnc = FLeFake.pidGet();
				brEnc = BReFake.pidGet();
				blEnc = BLeFake.pidGet();
				FRc.setSetpoint(FRc.getSetpoint());
				FLc.setSetpoint(FLc.getSetpoint());
				BRc.setSetpoint(BRc.getSetpoint());
				BLc.setSetpoint(BLc.getSetpoint());
			}
		}
		
//		System.out.println("Actual: " + FRe.getVoltage());
//		System.out.println("Setpoint: " + FRc.getSetpoint());
	}
	
	void SetupLogging(Logger logger)
	{
		Robot.logger.AddAttribute("FRpos");
		Robot.logger.AddAttribute("FRCurrent");
		Robot.logger.AddAttribute("FRspeed");
		Robot.logger.AddAttribute("FRApos");
		Robot.logger.AddAttribute("FRACurrent");
		Robot.logger.AddAttribute("FREncpos");
		Robot.logger.AddAttribute("FREncSetpoint");

		Robot.logger.AddAttribute("FLpos");
		Robot.logger.AddAttribute("FLCurrent");
		Robot.logger.AddAttribute("FLspeed");
		Robot.logger.AddAttribute("FLApos");
		Robot.logger.AddAttribute("FLACurrent");
		Robot.logger.AddAttribute("FLEncpos");
		Robot.logger.AddAttribute("FLEncSetpoint");
		
		Robot.logger.AddAttribute("BRpos");
		Robot.logger.AddAttribute("BRCurrent");
		Robot.logger.AddAttribute("BRspeed");
		Robot.logger.AddAttribute("BRApos");
		Robot.logger.AddAttribute("BRACurrent");
		Robot.logger.AddAttribute("BREncpos");
		Robot.logger.AddAttribute("BREncSetpoint");
		
		Robot.logger.AddAttribute("BLpos");
		Robot.logger.AddAttribute("BLCurrent");
		Robot.logger.AddAttribute("BLspeed");
		Robot.logger.AddAttribute("BLApos");
		Robot.logger.AddAttribute("BLACurrent");
		Robot.logger.AddAttribute("BLEncpos");
		Robot.logger.AddAttribute("BLEncSetpoint");
	}

	void Log()
	{
		Robot.logger.Log("FRpos", FR.getEncPosition());
		Robot.logger.Log("FRCurrent", FR.getOutputCurrent());
		Robot.logger.Log("FRspeed", FR.getSpeed());
		Robot.logger.Log("FRApos", FRa.getEncPosition());
		Robot.logger.Log("FRACurrent", FRa.getOutputCurrent());
		Robot.logger.Log("FREncpos", FRe.getVoltage() + FReFake.m_offset);
		Robot.logger.Log("FREncSetpoint", FRc.getSetpoint());
		
		Robot.logger.Log("FLpos", FL.getEncPosition());
		Robot.logger.Log("FLCurrent", FL.getOutputCurrent());
		Robot.logger.Log("FLspeed", FL.getSpeed());
		Robot.logger.Log("FLApos", FLa.getEncPosition());
		Robot.logger.Log("FLACurrent", FLa.getOutputCurrent());
		Robot.logger.Log("FLEncpos", FLe.getVoltage() + FLeFake.m_offset);
		Robot.logger.Log("FLEncSetpoint", FLc.getSetpoint());
		
		Robot.logger.Log("BRpos", BR.getEncPosition());
		Robot.logger.Log("BRCurrent", BR.getOutputCurrent());
		Robot.logger.Log("BRspeed", BR.getSpeed());
		Robot.logger.Log("BRApos", BRa.getEncPosition());
		Robot.logger.Log("BRACurrent", BRa.getOutputCurrent());
		Robot.logger.Log("BREncpos", BRe.getVoltage() + BReFake.m_offset);
		Robot.logger.Log("BREncSetpoint", BRc.getSetpoint());
		
		Robot.logger.Log("BLpos", BL.getEncPosition());
		Robot.logger.Log("BLCurrent", BL.getOutputCurrent());
		Robot.logger.Log("BLspeed", BL.getSpeed());
		Robot.logger.Log("BLApos", BLa.getEncPosition());
		Robot.logger.Log("BLACurrent", BLa.getOutputCurrent());
		Robot.logger.Log("BLEncpos", BLe.getVoltage() - BLeFake.m_offset);
		Robot.logger.Log("BLEncSetpoint", BLc.getSetpoint());
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
		
		SteerEncMaxFR = Config.GetSetting("SteerEncMaxFR",0);
		SteerEncMaxFL = Config.GetSetting("SteerEncMaxFL",0);
		SteerEncMaxBR = Config.GetSetting("SteerEncMaxBR",0);
		SteerEncMaxBL = Config.GetSetting("SteerEncMaxBL",0);

		FRc.setInputRange(0,SteerEncMaxFR);
		FRc.setOutputRange(-SteerSpeed,SteerSpeed);
		FRc.setPercentTolerance(SteerTolerance);
		
		FLc.setInputRange(0,SteerEncMaxFL);
		FLc.setOutputRange(-SteerSpeed,SteerSpeed);
		FLc.setPercentTolerance(SteerTolerance);
		
		BRc.setInputRange(0,SteerEncMaxBR);
		BRc.setOutputRange(-SteerSpeed,SteerSpeed);
		BRc.setPercentTolerance(SteerTolerance);
		
		BLc.setInputRange(0,SteerEncMaxBL);
		BLc.setOutputRange(-SteerSpeed,SteerSpeed);
		BLc.setPercentTolerance(SteerTolerance);
	/////////////////////////////////////////////////////
		TurningSpeedFactor = Config.GetSetting("turningSpeedFactor", 1);
		DriveCIMMaxRPM = Config.GetSetting("driveCIMmaxRPM",4000);
	/////////////////////////////////////////////////////
		SteerOffsetFR = Config.GetSetting("SteerEncOffsetFR",4.92);
		SteerOffsetFL = Config.GetSetting("SteerEncOffsetFL",4.92);
		SteerOffsetBR = Config.GetSetting("SteerEncOffsetBR",4.92);
		SteerOffsetBL = Config.GetSetting("SteerEncOffsetBL",4.92);

		FReFake.setOffset(SteerOffsetFR);
		FLeFake.setOffset(SteerOffsetFL);
		BReFake.setOffset(SteerOffsetBR);
		BLeFake.setOffset(SteerOffsetBL);
	}
}
