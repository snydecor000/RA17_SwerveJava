package org.usfirst.frc.team1741.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;

public class SwerveDrive 
{
	private static final double PI = 3.14159265358979;
	
	private double TurningSpeedFactor;
	private double length,width,diameter;
	private double temp;
	private double a,b,c,d;
	private double ws1,ws2,ws3,ws4;
	private double wa1,wa2,wa3,wa4;
	private double max;
	
	protected SwerveModule FRM;
	protected SwerveModule FLM;
	protected SwerveModule BRM;
	protected SwerveModule BLM;
	
	//SwerveDrive(CANTalon fr,CANTalon fra, AnalogInput fre)
	SwerveDrive(CANTalon fr, CANTalon fra, AnalogInput fre, CANTalon fl, CANTalon fla, AnalogInput fle, CANTalon br, CANTalon bra, AnalogInput bre, CANTalon bl, CANTalon bla, AnalogInput ble)
	{
		FRM = new SwerveModule(fr, fra, fre);
		FLM = new SwerveModule(fl, fla, fle);
		BRM = new SwerveModule(br, bra, bre);
		BLM = new SwerveModule(bl, bla, ble);

		length = Config.GetSetting("FrameLength",1);
		width = Config.GetSetting("FrameWidth",1);
		diameter = Math.sqrt(Math.pow(length,2)+Math.pow(width,2));
		temp = 0.0;
		a = 0.0;b = 0.0;c = 0.0;d = 0.0;
		ws1 = 0.0;ws2 = 0.0;ws3 = 0.0;ws4 = 0.0;
		wa1 = 0.0;wa2 = 0.0;wa3 = 0.0;wa4 = 0.0;
		max = 0.0;
	}
	
	void Swerve(double x, double y, double z, double gyro, boolean fieldOrient)
	{
		gyro *= PI/180.0f;
		z *= TurningSpeedFactor;
		
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
		FRM.setDrive(ws2);
		FLM.setDrive(-ws1);
		BRM.setDrive(ws3);
		BLM.setDrive(-ws4);

		wa1 = Math.atan2(b,c) * 180.0f/PI;
		wa2 = Math.atan2(b,d) * 180.0f/PI;
		wa3 = Math.atan2(a,d) * 180.0f/PI;
		wa4 = Math.atan2(a,c) * 180.0f/PI;
		if(wa1 < 0){wa1 += 360;}//wa1 = FL
		if(wa2 < 0){wa2 += 360;}//wa2 = FR
		if(wa3 < 0){wa3 += 360;}//wa3 = BR
		if(wa4 < 0){wa4 += 360;}//wa4 = BL
		FRM.PIDSet();
		FLM.PIDSet();
		BRM.PIDSet();
		BLM.PIDSet();
		
		if(Math.abs(FRM.pidGet()/(FRM.getEncMax()/360.0f) - wa2) > 90)
		{
			wa2 = (wa2 + 180)%360;
			ws2 = -ws2;
		}
		if(Math.abs(FLM.pidGet()/(FLM.getEncMax()/360.0f) - wa1) > 90)
		{
			wa1 = (wa1 + 180)%360;
			ws1 = -ws1;
		}
		if(Math.abs(BRM.pidGet()/(BRM.getEncMax()/360.0f) - wa3) > 90)
		{
			wa3 = (wa3 + 180)%360;
			ws3 = -ws3;
		}
		if(Math.abs(BLM.pidGet()/(BLM.getEncMax()/360.0f) - wa4) > 90)
		{
			wa4 = (wa4 + 180)%360;
			ws4 = -ws4;
		}
		
		FRM.setAngle(wa2);
		FLM.setAngle(wa1);
		BRM.setAngle(wa3);
		BLM.setAngle(wa4);
//		FRM.setAngleDrive(wa2,ws2);
//		FLM.setAngleDrive(wa1,-ws1);
//		BRM.setAngleDrive(wa3,ws3);
//		BLM.setAngleDrive(wa4,-ws4);
	}
	
	void SetupLogging(Logger logger)
	{
		FRM.SetupLogging(logger, "FR");
		FLM.SetupLogging(logger, "FL");
		BRM.SetupLogging(logger, "BR");
		BLM.SetupLogging(logger, "BL");
	}

	void Log(Logger logger)
	{
		FRM.Log(logger, "FR");
		FLM.Log(logger, "FL");
		BRM.Log(logger, "BR");
		BLM.Log(logger, "BL");
	}

	void ReloadConfig()
	{
		length = Config.GetSetting("FrameLength",1);
		width = Config.GetSetting("FrameWidth",1);
		TurningSpeedFactor = Config.GetSetting("turningSpeedFactor", 1);
	/////////////////////////////////////////////////////
		FRM.ReloadConfig("FR");
		FLM.ReloadConfig("FL");
		BRM.ReloadConfig("BR");
		BLM.ReloadConfig("BL");
	}
	
	public String toString()
	{
		return "FR Speed: " + FRM.getTurnSpeed() + "\n" +"FL Speed: " + FLM.getTurnSpeed() + "\n" +"BR Speed: " + BRM.getTurnSpeed() + "\n" +"BL Speed: " + BLM.getTurnSpeed() + "\n";
	}
}
