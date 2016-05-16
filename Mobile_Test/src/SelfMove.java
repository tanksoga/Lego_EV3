
import java.io.IOException;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class SelfMove
{
     private float centerAngle = 0;
     
     private EV3MediumRegulatedMotor steerMotor = null;
     private EV3MediumRegulatedMotor moveMotor = null;
     private EV3UltrasonicSensor sonicSensor = null;
     private EV3GyroSensor gyroSensor = null;
     private SampleProvider angleProvider = null;

     public SelfMove() throws IOException 
     {
          try
          {
               steerMotor = new EV3MediumRegulatedMotor(MotorPort.C);
               moveMotor = new EV3MediumRegulatedMotor(MotorPort.B);
               //sonicSensor = new EV3UltrasonicSensor(SensorPort.S2);
               gyroSensor = new EV3GyroSensor(SensorPort.S3);
               
               angleProvider = gyroSensor.getAngleMode();
               
               System.out.println("Start...");
               
               initSteerToCenter();
               move();
               gyroSensor.close();
          }
          catch(Exception e)
          {
               e.printStackTrace();
               throw(e);
          }
     }
     
     private void move() throws IOException
     {
          try
          {
               moveMotor.setSpeed(4000);
               moveMotor.backward();
               
               while (!Button.ESCAPE.isDown())
               {
                    
                    float[] angle = { 0 };
                    angleProvider.fetchSample(angle, 0);
                    
                    System.out.println("Angle" + angle[0]);
                  
                    Delay.msDelay(100);
               }
               
               moveMotor.stop();
          }
          catch(Exception e)
          {
               e.printStackTrace();
          }
     }
     
     private void initSteerToCenter() throws IOException 
     {
          try
          {
               steerMotor.forward();               
               while(steerMotor.isMoving()){}
               steerMotor.stop(true);            
               float maxRightAngle = steerMotor.getTachoCount();               
               steerMotor.resetTachoCount();
               
               steerMotor.backward();               
               while(steerMotor.isMoving()){}
               steerMotor.stop(true);
               float maxLeftAngle = steerMotor.getTachoCount();               
               steerMotor.resetTachoCount();
               
               centerAngle = maxLeftAngle / 2;
               steerMotor.rotate(Math.abs(Math.round(centerAngle)));
               while(steerMotor.isMoving()){}
               steerMotor.stop(true);
               
               System.out.println("Max right angle=" + maxRightAngle);
               System.out.println("Max left angle=" + maxLeftAngle);
               System.out.println("Center angle=" + Math.abs(Math.round(centerAngle)));
          }
          catch(Exception e)
          {
               e.printStackTrace();
               throw(e);
          }
     }

     public static void main(String[] args) throws IOException 
     {
          new SelfMove();
     }

}
