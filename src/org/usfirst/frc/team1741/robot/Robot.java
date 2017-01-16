
package org.usfirst.frc.team1741.robot;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
//import edu.wpi.first.wpilibj.CANTalon;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Timer;
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
	
	Targeting targeting;
	PIDController driveAimer;
	FakePIDSource cameraSource;
	FakePIDOutput driveOutput;
	//EdgeDetect config;
	
	double x;
	double y;
	double twist;
	double autoAimOffset;
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
		autoAimOffset = 0;
		fieldOrient = true;
		driveAimer = null;
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
		targeting = new Targeting();
		//config = new EdgeDetect();
		System.out.println("test6");
		////////////////////////////////////////////////
		cameraSource = new FakePIDSource();
		driveOutput = new FakePIDOutput();
		driveAimer = new PIDController(Config.GetSetting("AutoAimP", 0.12),
									   Config.GetSetting("AutoAimI", 0.00),
									   Config.GetSetting("AutoAimD", 0.00),
									   cameraSource,
									   driveOutput);
		driveAimer.setInputRange(-24,24);
		driveAimer.setOutputRange(-.3,.3);
		driveAimer.setAbsoluteTolerance(.5);
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
    { StartLogging("teleop",logger)
    ; SetupLogging()
    ; ReloadConfig()
    ; timer.reset()
    ; timer.start()
    ; }
    
    public void teleopPeriodic() 
    {
    	//System.out.println(gyro.getAngle());
    	
    	x = driver.GetLeftX();
    	y = driver.GetLeftY();
    	twist = driver.GetRightX();
    	
    	if(x >= -0.1 && x <= 0.1){x=0;}
    	else if(!driver.GetRightBumper()) { x=0.6*x; }
    	if(y >= -0.1 && y <= 0.1){y=0;}
    	else if(!driver.GetRightBumper()) { y=0.6*y; }
    	if(twist >= -0.1 && twist <= 0.1){twist=0;}
    	else if(!driver.GetRightBumper()) { twist=0.6*twist; }
    	else { twist=0.8*twist; }
    	if(driveMode.Check(driver.GetStart()))
    	{
    		fieldOrient = !fieldOrient;
    	}
    	
    	if(driver.GetRightBumper())
    	{
    		gyro.reset();
    	}
    	
    	
    	//drive.Swerve(-x,-y,-twist,0,true);
       	if(driver.GetBack())
    	{
    		//configReload = !configReload;
    		ReloadConfig();
    	}
       	
       	if(driver.GetRightBumper())
       	{
    		List<Target> targets;
    		targets = targeting.GetTargets();
    		if(targets.size() != 0)
    		{
    			if (!driveAimer.isEnabled())
    			{
    				driveAimer.enable();
    			}
    			Target closest = targets.get(0);
    			for (int i = 0; i < targets.size(); i++)
    			{
    				if(targets.get(i).getDistance() < closest.getDistance())
    				{
    					closest = targets.get(i);
    				}
    			}
    			cameraSource.pidSet(closest.getPan() + autoAimOffset);
    			double output = driveOutput.pidGet();
    			drive.Swerve(-x,-y,-output,-gyro.getAngle(),false);
//    			//aimLoop->SetSetpoint(targetDegreeToTicks(closest.Tilt()) / 800 + autoAimOffset);
    		}
    		else
    		{
    			//light->Set(Relay::kReverse);
//    			if (driveAimer->IsEnabled())
//    			{
//    				driveAimer->Disable();
//    			}
    			drive.Swerve(0, 0, 0, 0, false);;
    		}
       	}
       	else
       	{
       		drive.Swerve(-x,-y,-twist,-gyro.getAngle(),fieldOrient);
       	}
       	
    	Log(timer.get());
    	System.out.print(drive);
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
		logger.addLoggable(drive);
		logger.addLoggable(targeting);
		logger.AddAttribute("Time");
		logger.AddAttribute("AccX");
		logger.AddAttribute("AccY");
		logger.AddAttribute("AccZ");
		//drive.setupLogging(logger);
		logger.WriteAttributes();
	}

	void Log(double d)
	{
		logger.Log("Time", d);
		logger.Log("AccX", acceler.getX());
		logger.Log("AccY", acceler.getY());
		logger.Log("AccZ", acceler.getZ());
		logger.logAll();
	}

	void ReloadConfig()
	{
		Config.LoadFromFile("/home/lvuser/config.txt");
		autoAimOffset = Config.GetSetting("autoAimOffest", 0);
		drive.ReloadConfig();
	}
}
