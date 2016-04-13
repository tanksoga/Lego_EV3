import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;


public class E3vRemoteControlCarApp 
{

	static boolean isRunning = true;

	public static void main(String[] args) throws IOException 
	{
		
		try (
				final EV3LargeRegulatedMotor engineMotor = new EV3LargeRegulatedMotor(MotorPort.C);
				final EV3LargeRegulatedMotor turnMottor = new EV3LargeRegulatedMotor(MotorPort.B);
				ServerSocket serv = new ServerSocket(19231);
				) 
		{

			Socket socket = serv.accept();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

			

			// we copied it from somewhere, not necessary
			Delay.msDelay(3000);


			// the listener with the while readline
			String line;
			while ((line = reader.readLine()) != "STOP" && isRunning) 
			{
				System.out.println("RECIEVED " + line);
				switch (line) 
				{
				case "UP-PRESS":
					engineMotor.setSpeed(6000);
					engineMotor.backward();;
					break;
				case "UP-RELEASE":
					engineMotor.stop(true);
					break;
				case "DOWN-PRESS":
					engineMotor.setSpeed(6000);
					engineMotor.forward();
					break;
				case "DOWN-RELEASE":
					engineMotor.stop(true);
					break;
				case "LEFT-PRESS":
					turnMottor.setSpeed(200);
					turnMottor.rotate(-180, true);
					break;
				case "LEFT-RELEASE":
					turnMottor.setSpeed(200);
					turnMottor.rotate(180, true);
					turnMottor.stop(true);
					break;
				case "RIGHT-PRESS":
					turnMottor.setSpeed(200);
					turnMottor.rotate(180, true);
					break;
				case "RIGHT-RELEASE":
					turnMottor.setSpeed(200);
					turnMottor.rotate(-180, true);
					turnMottor.stop(true);
					break;
				case "STOP":
					E3vRemoteControlCarApp.isRunning = false;
					break;
				}
			}
		}
	}
}
