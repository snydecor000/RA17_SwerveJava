
package org.usfirst.frc.team1741.robot;

import java.io.File;
import java.util.Calendar;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    
	AnalogGyro gyro;
	BuiltInAccelerometer acceler;
	public static Logger logger;
	Timer timer;
	SwerveDrive drive;
	Gamepad driver;
	EdgeDetect driveMode;
	
	CANTalon FR;
	CANTalon FRa;
	CANTalon FL;
	CANTalon FLa;
	CANTalon BR;
	CANTalon BRa;
	CANTalon BL;
	CANTalon BLa;
	AnalogInput FRe;
	AnalogInput FLe;
	AnalogInput BRe;
	AnalogInput BLe;
	//EdgeDetect config;
	
	double x;
	double y;
	double twist;
	boolean fieldOrient;
	boolean configReload;
    
	
    public Robot()
    {
    	FRe = null;
    	FLe = null;
    	BRe = null;
    	BLe = null;
		FR = null;
		FRa = null;
		FL = null;
		FLa = null;
		BR = null;
		BRa = null;
		BL = null;
		BLa = null;
    	System.out.println("test0s");
    	chooser = null;
		gyro = null;
		acceler = null;
		logger = null;
		timer = null;
		drive = null;
		driver = null;
		driveMode = null;
		x = 0;
		y = 0;
		twist = 0;
		fieldOrient = true;
		//config = null;
    }

    public void robotInit() 
    {
    	System.out.println("test");
    	Config.LoadFromFile("/home/lvuser/config.txt");
    	chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        System.out.println("test1");
		////////////////////////////////////////////////
		gyro = new AnalogGyro(1);
		gyro.setSensitivity(0.007);
		gyro.reset();
		gyro.calibrate();
		acceler = new BuiltInAccelerometer();
		System.out.println("test2");
		////////////////////////////////////////////////
		logger = new Logger();
		
		timer = new Timer();
		System.out.println("test3");
		////////////////////////////////////////////////
		FRe = new AnalogInput(0);
		FLe = new AnalogInput(3);
		BRe = new AnalogInput(2);
		BLe = new AnalogInput(4);
	   	FR = new CANTalon(1);
    	FRa = new CANTalon(2);
    	FL = new CANTalon(3);
    	FLa = new CANTalon(4);
    	BR = new CANTalon(5);
    	BRa = new CANTalon(6);
    	BL = new CANTalon(7);
    	BLa = new CANTalon(8);
		drive = new SwerveDrive(FR, FRa, FRe, FL, FLa, FLe, BR, BRa, BRe, BL, BLa, BLe);
		System.out.println("test4");
		////////////////////////////////////////////////
		driver = new Gamepad(4);
		System.out.println("test5");
		////////////////////////////////////////////////
		driveMode = new EdgeDetect();
		//config = new EdgeDetect();
		System.out.println("test6");
    }

	public void autonomousInit() 
    {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    public void autonomousPeriodic() 
    {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}
    }

    public void teleopInit()
    {
    	StartLogging("teleop",logger);
    	SetupLogging();
    	ReloadConfig();
    	timer.reset();
    	timer.start();
    }
    
    public void teleopPeriodic() 
    {
    	System.out.println(gyro.getAngle());
    	
    	x = driver.GetLeftX();
    	y = driver.GetLeftY();
    	twist = driver.GetRightX();
    	
    	if(x >= -0.1 && x <= 0.1){x=0;}
    	else { x=0.6*x; }
    	if(y >= -0.1 && y <= 0.1){y=0;}
    	else { y=0.6*y; }
    	if(twist >= -0.1 && twist <= 0.1){twist=0;}
    	else { twist=0.6*twist; }
    	if(driveMode.Check(driver.GetStart()))
    	{
    		gyro.reset();
    		fieldOrient = !fieldOrient;
    	}
    	
    	drive.Swerve(-x,-y,-twist,-gyro.getAngle(),fieldOrient);
    	//drive.Swerve(-x,-y,-twist,0,true);
       	if(driver.GetBack())
    	{
    		//configReload = !configReload;
    		ReloadConfig();
    	}
    	
    	Log(timer.get());
    }
    
    public void testInit() 
    {
    	StartLogging("test",logger);
    	SetupLogging();
    	ReloadConfig();
    	timer.reset();
    	timer.start();
    }
    
    public void testPeriodic() 
    {
    	drive.Swerve(0,0.1,0,0,fieldOrient);
    	if(driver.GetBack())
    	{
    		//configReload = !configReload;
    		ReloadConfig();
    	}
    }
    
    public void disabledInit() 
    {
    
    }
    
    public void disabledPeriodic() 
    {
    
    }
    
	void StartLogging(String mode, Logger l)
	{
		String robot = !(Config.GetSetting("isPrototype", 0) == 0) ? "_proto" : "_comp";
		l.Close();
		Calendar calendar = Calendar.getInstance();
		String dir = "/home/lvuser";
		if(new File("/media/sda").exists())
		{
			dir = "/media/sda";
		}
		String name = dir + "/log-" + calendar.get(Calendar.YEAR) + "-" +
				calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + "_" +
				calendar.get(Calendar.HOUR_OF_DAY) + "-" + calendar.get(Calendar.MINUTE) + "-" +
				calendar.get(Calendar.SECOND) + "_" + mode + robot + ".csv";
		System.out.println(name);
		l.Open(name);
	}

	void SetupLogging()
	{
		logger.AddAttribute("Time");
		logger.AddAttribute("AccX");
		logger.AddAttribute("AccY");
		logger.AddAttribute("AccZ");
		drive.SetupLogging(logger);
		logger.WriteAttributes();
	}

	void Log(double d)
	{
		logger.Log("Time", d);
		logger.Log("AccX", acceler.getX());
		logger.Log("AccY", acceler.getY());
		logger.Log("AccZ", acceler.getZ());
		drive.Log();
		logger.WriteLine();
	}

	void ReloadConfig()
	{
		Config.LoadFromFile("/home/lvuser/config.txt");
		drive.ReloadConfig();
	}
}
