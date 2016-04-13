import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
 
import javax.swing.JButton;
import javax.swing.JFrame;
 

public class Remote extends JFrame 
{
 
	private static final long serialVersionUID = -8402983606638099877L;
	
	private Socket socket = null;
 
	private boolean isUpPressed = false;
	private boolean isDownPressed = false;
	private boolean isLeftPressed = false;
	private boolean isRightPressed = false;
	
	private final JButton left;
	private final JButton right;
	private final JButton up;
	private final JButton down;
	private final JButton stop;
 
	private BufferedWriter pw;
 
	public static void main(String[] args) {
		Remote remote = new Remote();
		remote.setSize(500, 500);
		remote.setVisible(true);
	}
 
	public Remote() {
		try {
			socket = new Socket("192.168.2.46", 19231);

			pw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception eee)
		{
			eee.printStackTrace();
		}
 
		setLayout(new BorderLayout());
 
		left = new JButton("LEFT");
		this.getContentPane().add(left, BorderLayout.WEST);
		left.addKeyListener(new KeyAdapter()
		{
			@Override
	        public void keyPressed(KeyEvent event)
	        {
				if (event.getKeyCode() == KeyEvent.VK_LEFT)
					leftPress();
	        }
			
			 @Override
		     public void keyReleased(KeyEvent event) 
			 {
				 if (event.getKeyCode() == KeyEvent.VK_LEFT)
					 leftRelease();
		     }
		});
		
		right = new JButton("RIGHT");
		this.getContentPane().add(right, BorderLayout.EAST);
		
		up = new JButton("UP");
		this.getContentPane().add(up, BorderLayout.NORTH);
		
		down = new JButton("DOWN");
		this.getContentPane().add(down, BorderLayout.SOUTH);
		
		stop = new JButton("STOP\n");
		this.getContentPane().add(stop, BorderLayout.CENTER);
		
		//keypad arrows work only if the focus is on the stop button
		//lame but that's life
		stop.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent event) {
				// TODO Auto-generated method stub
 
			}
 
			@Override
			public void keyReleased(KeyEvent event) 
			{
				if(KeyEvent.VK_UP == event.getKeyCode())
				{
					upRelease();
					isUpPressed = false;
				} 
				else if(KeyEvent.VK_DOWN == event.getKeyCode())
				{
					downRelease();
					isDownPressed = false;
				} 
				else if(KeyEvent.VK_LEFT == event.getKeyCode())
				{
					leftRelease();
					isLeftPressed = false;
				} 
				else if(KeyEvent.VK_RIGHT == event.getKeyCode())
				{
					rightRelease();
					isRightPressed = false;
				} 
				else if(KeyEvent.VK_ESCAPE == event.getKeyCode()){
					sendCommand("STOP");
					if(socket != null)
					{
						try
						{
							socket.close();
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
 
			@Override
			public void keyPressed(KeyEvent event) 
			{
				if(KeyEvent.VK_UP == event.getKeyCode())
				{
					if(!isUpPressed)
					{
						upPress();
						isUpPressed = true;
					}
				} 
				else if(KeyEvent.VK_DOWN == event.getKeyCode())
				{
					if(!isDownPressed)
					{
						downPress();
						isDownPressed = true;
					}
				}
				else if(KeyEvent.VK_LEFT == event.getKeyCode())
				{
					if(!isLeftPressed)
					{
						leftPress();
						isLeftPressed = true;
					}
				} 
				else if(KeyEvent.VK_RIGHT == event.getKeyCode())
				{
					if(!isRightPressed)
					{
						rightPress();
						isRightPressed = true;
					}
				}
			}
		});
 
		pack();
	}
 
	private void downPress() {
		sendCommand("DOWN-PRESS");
	}
 
	private void sendCommand(String command) {
		try 
		{
			System.out.println("Send Command:" + command);
			//pw.write(command+"\n");pw.flush();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
 
	private void downRelease() {
		sendCommand("DOWN-RELEASE");
	}
 
	private void leftPress() {
		sendCommand("LEFT-PRESS");
	}
 
	private void upRelease() {
		sendCommand("UP-RELEASE");
	}
 
	private void leftRelease() {
		sendCommand("LEFT-RELEASE");
	}
	private void rightRelease() {
		sendCommand("RIGHT-RELEASE");
	}
 
	private void upPress() {
		sendCommand("UP-PRESS");
	}
 
	private void rightPress() {
		sendCommand("RIGHT-PRESS");
	}
}