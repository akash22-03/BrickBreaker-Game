package BrickBreaker;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.swing.JPanel;

public class GamePlay extends JPanel implements KeyListener, ActionListener
{
	private boolean play = false;
	private int score = 0;
	private int brickCount = 21;
	private Timer timer ;
	private int delay = 1;
	private int playerX = 310;
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGenerator map;
	
	public GamePlay()
	{
		map = new MapGenerator(3,7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	public void paint(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(1,1,692,592);
		
		map.draw((Graphics2D)g);
		//for borders
		g.setColor(Color.black);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		//pedal
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		//ball
		g.setColor(Color.black);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		g.setColor(Color.black);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(""+score , 590, 30);
		if(brickCount<=0)
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.green);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You Won", 190, 300);
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart.", 230, 350);
		}
		if(ballposY>570)
		{
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 25));
			g.drawString("Game Over, Score: " + score , 190, 300);
			g.setFont(new Font("serif", Font.BOLD, 25));
			g.drawString("Press Enter to Restart.",  230, 350);
		}
		g.dispose();
	}
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		timer.start();
		if(play)
		{
			//ball-pedal interaction
			if(new Rectangle(ballposX, ballposY, 20, 30).intersects(new Rectangle(playerX, 550, 100, 8)))
			{
				ballYdir = -ballYdir;
			}
			for(int i = 0;i<map.map.length;i++)
			{
				for(int j=0;j<map.map[0].length;j++)
				{
					if(map.map[i][j]>0)
					{
						int brickX = j*map.brickwidth +80;
						int brickY = i*map.brickheight +50;
						int brickwidth = map.brickwidth;
						int brickheight = map.brickheight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickwidth, brickheight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect))
						{
							map.setBrickValue(0, i, j);
							brickCount--;
							score+=5;
							
							if(ballposX +19<= brickRect.x || ballposX+1 >= brickRect.x+ brickRect.width)
							{
								ballXdir = -ballXdir;
							}
							else
							{
								ballYdir = -ballYdir;
							}
						}
					}
				}
			}
			ballposX += ballXdir;
			ballposY += ballYdir;
			if(ballposX < 0) {
				ballXdir = -ballXdir;
			}
			if(ballposY < 0) {
				ballYdir = -ballYdir; //bounce back
			}
			if(ballposX > 670) {
				ballXdir = -ballXdir; //bounce back
			}
		}
		repaint();	
	}
	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			if(playerX >= 600) playerX = 600;
			else 
			{
				play = true;
				playerX += 20;
			}
		}
		if(e.getKeyCode()==KeyEvent.VK_LEFT)
		{
			if(playerX < 10) playerX = 10;
			else 
			{
				play = true;
				playerX -= 20;
			}
		}	
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if(play==false) 
			{
				play=true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				score = 0;
				brickCount = 21;
				map = new MapGenerator(3,7);
				repaint();
			}
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
}
