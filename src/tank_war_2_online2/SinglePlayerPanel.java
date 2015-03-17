package tank_war_2_online2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// Panel-----------------------------------------------------------------------------------------------
class Draw extends JPanel implements KeyListener,Runnable
{

	// Definition my tank.......................
	MyTank tank1=null;
	
	// Definition enemy tank....................
	public Vector<EnemyTank> enemys=new Vector<EnemyTank>();
	int enSize=5;
	//load game node
	public Vector<Node>nodes=new Vector <Node>();
	
	// Bang's process
	Vector<Bang> ba=new Vector<Bang>();
	
	Color c1,c2;
	
	// Biagbang's  three picture
	Image bang1=null;
	Image bang2=null;
	Image bang3=null;
	
    	
	// 战场面板
	public Draw(String flag)
	{
        this.setSize(1000,500);
	
		//初始化　TANK
		if(flag.equals("NewGame"))
		{
			//恢复数据
			Recorder.ReadAllBang();
			//初始化MY TANK
			tank1=new MyTank(15,320);
			//ENEMy
		    for(int i=0;i<enSize;i++)
		    {
		    	
			    // A enemy tank.....................
			    EnemyTank enemy=new EnemyTank((i+1)*50+200,10);
			    
			    enemy.setColor(1);
			    enemy.setDirection(2);
			    // Let enemy can see each other
			    enemy.setets(enemys);
			    tank1.setets(enemys);
			    // Start enemy
			    Thread t=new Thread(enemy);
			    t.start();
			
			    // Add bullet
			    Shot s=new Shot(enemy.x+11,enemy.y+36,2);
			    enemy.ss.add(s);
			    Thread t2=new Thread(s);
			    t2.start();
			
			    enemys.add(enemy);
			    
			
		   }
		}
		
		//Load Game 初始化坦克
		if(flag.equals("LoadGame"))
		{
			//恢复数据
			Recorder.ReadAllBang();
			//初始化MY TANK
			tank1=new MyTank(15,320);
			
	        nodes=new Recorder().LoadGame();
			for(int i=0;i<nodes.size();i++)
			{
				
				Node node=nodes.get(i);

				// A enemy tank.....................
				EnemyTank enemy=new EnemyTank(node.x,node.y);
				enemy.setColor(node.color);
				enemy.setDirection(node.drection);
				// Let enemy can see each other
				enemy.setets(enemys);
				tank1.setets(enemys);
				// Start enemy
				Thread t=new Thread(enemy);
				t.start();
				
				// Add bullet
				Shot s=new Shot(enemy.x+11,enemy.y+36,2);
				enemy.ss.add(s);
				Thread t2=new Thread(s);
				t2.start();
				
				enemys.add(enemy);
		}
		}

		// Bang picture
		try {
			bang1=ImageIO.read(new File("bang1.gif"));//image in Tank war
			bang2=ImageIO.read(new File("bang2.gif"));
			bang3=ImageIO.read(new File("bang3.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AePlayWave apw=new AePlayWave("auStart.wav");
		apw.start();
//					bang1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bang1.gif"));
//					bang2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bang2.gif"));
//					bang3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/bang3.gif"));
//		            image in src
	}
	
	
	public void paint(Graphics g)
	{   
		// Add tank........................................
		super.paint(g);

		g.fillRect(0, 0, 700, 400);
		
		// Add massage
		this.Massage(g);
		
        // Add my tank.....................................
		if(Recorder.getMyLife()>0)
		{
		this.DrawTank(tank1.getX(), tank1.getY(), g,tank1.getDirection(),0);
		}
		if(!tank1.noBang&&Recorder.getMyLife()>0)
		{
			tank1.setX(15);
			tank1.setY(320);
			tank1.setDirection(0);
			tank1.noBang=true;
			
		}
		
		
		
		// Add Many Fire...................................
		for(int i=0;i<tank1.ss.size();i++){
			
		// Add fire........................................
			Shot ms=tank1.ss.get(i);
		if(ms!=null&&ms.noBang==true){
			g.setColor(Color.red);
			g.draw3DRect(ms.x, ms.y, 2, 2, false);
		}
		
		// Remove fire....................................
		if(ms.noBang==false)
		{
			tank1.ss.remove(ms);
		}
		}
		
		// Add Big Bang ! 
		for(int i=0;i<ba.size();i++)
		{
			Bang b= ba.get(i);
			if(b.time>30)
			{
				g.drawImage(bang2, b.x, b.y, 30, 30,this);
			}
			if(b.time<=30&&b.time>20)
			{
				g.drawImage(bang1, b.x, b.y, 30, 30,this);
			}
			if(b.time<=20&&b.time>10)
			{
				g.drawImage(bang2, b.x, b.y, 30, 30,this);
			}
			if(b.time<=10)
			{
				g.drawImage(bang3, b.x, b.y, 30, 30,this);
			}
			
			b.BangDown();
			
			// Delete the Bang
			if(b.time==0)
			{
				ba.remove(b);
			}
		}
		
		// Add enemy tank and bullet..................................
		for(int i=0;i<enemys.size();i++)
		{
			EnemyTank et=enemys.get(i);
			
			if(et.noBang)
			{
			this.DrawTank(et.getX(), et.getY(), g, et.getDirection(), et.getColor());
			
			// Add bullet
			for(int j=0;j<et.ss.size();j++)
			{
				Shot es =et.ss.get(j);
				if(es.noBang)
					
				{
					g.draw3DRect(es.x, es.y, 2, 2, false);
				}
				else 
				{
					et.ss.remove(es);
				}
			}
			}
		}
		
		
	}
	// Draw the massage
	public void Massage(Graphics g)
	{
		
		//画出提示
		this.DrawTank(30,405, g, 1, 0);
		this.DrawTank(100, 405, g, 1, 3);
		//提示的数量
		g.setColor(Color.red);
		g.drawString("X"+Recorder.getMyLife(), 67,424);
		g.setColor(Color.orange);
		g.drawString("X"+Recorder.getEnemyCount(), 137, 424);
		//总成绩		
		Font f=new Font("宋体",Font.BOLD,40);
		g.setFont(f);
		g.drawString("你消灭了：", 705, 50);
		this.DrawTank(705,70,g,1,1);
		g.setColor(Color.green);
		g.drawString("X"+Recorder.getAllBang(), 742, 100);
		
	}
	// My bullet hit enemy..............................
	public void MyBulletHit()
	{
		
					for(int i=0;i<tank1.ss.size();i++)
					{
						Shot ms=tank1.ss.get(i);
						
						if(ms.noBang)
						{
							for(int j=0;j<enemys.size();j++)
							{
								EnemyTank et=enemys.get(j);
								
								if(et.noBang)
								{
									if(this.hitTank(ms, et))
									{
										// Decrease enemy's count
										Recorder.Decrease();
										// increase enemy's count
										Recorder.Increase();
										//移除坦克
										enemys.remove(j);
										// 判断是否赢
										  if(Recorder.getEnemyCount()==0)
										    {
										    	JFrame lF =new JFrame("Sorry");
										    	JPanel lP =new JPanel();
										    	Color c1=new Color(196,226,216);
												lP.setPreferredSize(new Dimension(200, 100));
												lP.setBackground(c1);
												// 提示
												JLabel l1=new JLabel("You Win !");
												lP.add(l1);
												// 按钮
												JButton b1=new JButton("再来一局");
												lP.add(b1);
												
												//add
												lF.add(lP);
												lF.setVisible(true);
												lF.pack();

									      }
									}
								}
							}
						}
					}
	}
	
	// Enemy bullet hit me............................
	public void EnemyBulletHit()
	{
		
		for(int i=0;i<enemys.size();i++)
		{
			EnemyTank et=enemys.get(i);
			for(int j=0; j<et.ss.size();j++)
			{
				Shot es=et.ss.get(j);
				if(tank1.noBang)
				{
					if(this.hitTank(es, tank1))
					{
						// decrease my life
						Recorder.DecreaseMe();
						
						//zhen me chong sheng tan ke!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//						MyTank tank1=new MyTank(15,320);
//						 this.DrawTank(tank1.getX(), tank1.getY(), g,tank1.getDirection(),0);
						
						//是否输
						 if (Recorder.getMyLife()==0)
						    {   
						    	JFrame lF =new JFrame("Sorry");
						    	JPanel lP =new JPanel();
						    	Color c1=new Color(196,226,216);
								lP.setPreferredSize(new Dimension(200, 100));
								lP.setBackground(c1);
								// 提示
								JLabel l1=new JLabel("You not weak.\nBut he is too powerful.");
								lP.add(l1);
								// 按钮
								JButton b1=new JButton("再来一局");
								lP.add(b1);
								
								//add
								lF.add(lP);
								lF.setVisible(true);
								lF.pack();

//								b1.addActionListener();///////////////////////////////////////////////
						    	
						    }
					}
				}
				
				
			}
		}
	}
	
	
	// Despair enemy tank.............................
	public boolean hitTank (Shot s,Tank enemy)
	{
		boolean b2=false;
		
		// Enemy's direction~.........................
		switch(enemy.getDirection())
		{
		case 0:// UP and DOWN
		case 2:
			if(s.x>=enemy.x&&s.x<=enemy.x+22&&s.y>=enemy.y-1&&s.y<=enemy.y+30)
			{
				// Bullet dead
				s.noBang=false;
				// Enemy tank is dead
				enemy.noBang=false;

				// Big Bang !!!!!
				Bang b=new Bang(enemy.x,enemy.y);
				ba.add(b);
				// sound
				AePlayWave apw=new AePlayWave("auBang.wav");
				apw.start();
				b2=true;
								
			}
			break;
		case 1:// RIGHT and LEFT
		case 3:
			if(s.x>=enemy.x-5&&s.x<=enemy.x+26&&s.y>=enemy.y+4&&s.y<=enemy.y+26)
			{
				// Bullet dead
				s.noBang=false;
				// Enemy tank is dead
				enemy.noBang=false;
				// reduce enemy's count

				// Big Bang !!!!!
				Bang b=new Bang(enemy.x,enemy.y);
				ba.add(b);
				
				// sound
				AePlayWave apw=new AePlayWave("auBang.wav");
				apw.start();
				
				b2=true;
			}
			break;
		}
		
		return b2;
		
	}
	
	// Draw Tank............................................
	public void DrawTank (int x,int y,Graphics g,int direct,int type)
	{
		c1=new Color(120,120,120);
		c2=new Color(160,160,160);
		// Draw tank's style1...............................
		switch(type)
		{
		case 0:
			g.setColor(Color.red);
			break;
		case 1:
			g.setColor(Color.yellow);
			break;
		case 2:
			g.setColor(Color.blue);
			break;
		case 3:
			g.setColor(Color.green);
			break;	
		}
		// Draw tank's direction...........................
		switch(direct)
		{
		case 0://up
			g.fill3DRect(x, y, 6, 30,true);          //left
			g.fill3DRect(x+16, y, 6, 30,true);       //right
			g.setColor(c2);
			g.fill3DRect(x+6, y+5, 10, 20,true);     //center
			g.setColor(c1);
			g.fillOval(x+6, y+10, 10,10);            //oval
			g.setColor(Color.gray);
			g.fill3DRect(x+10, y, 2, 11, true);      //gun1
			g.fill3DRect(x+9, y-6, 4,6, true);       //gun2
			break;
		case 1://right.......................................
			g.fill3DRect(x-4, y+4, 30, 6,true);      //left
			g.fill3DRect(x-4, y+20,30, 6,true);      //right
			g.setColor(c2);
			g.fill3DRect(x+1, y+10, 20, 10,true);    //center
			g.setColor(c1);
			g.fillOval(x+6, y+10, 10,10);            //oval
			g.setColor(Color.gray);
			g.fill3DRect(x+16, y+14,11,2, true);     //gun1
			g.fill3DRect(x+27, y+13, 6,4, true);     //gun2
			break;
		case 3:// Left.......................................
			g.fill3DRect(x-4, y+4, 30, 6,true);       //left
			g.fill3DRect(x-4, y+20,30, 6,true);       //right
			g.setColor(c2);
			g.fill3DRect(x+1, y+10, 20, 10,true);     //center
			g.setColor(c1);
			g.fillOval(x+6, y+10, 10,10);             //oval
			g.setColor(Color.gray);
			g.fill3DRect(x-5, y+14,11,2, true);       //gun1
			g.fill3DRect(x-11, y+13, 6,4, true);      //gun2
			break;
		case 2:// Down........................................
			g.fill3DRect(x, y, 6, 30,true);           //left
			g.fill3DRect(x+16, y, 6, 30,true);        //right
			g.setColor(c2);
			g.fill3DRect(x+6, y+5, 10, 20,true);       //center
			g.setColor(c1);
			g.fillOval(x+6, y+10, 10,10);              //oval
			g.setColor(Color.gray);
			g.fill3DRect(x+10, y+21, 2, 11, true);     //gun1
			g.fill3DRect(x+9, y+32, 4,6, true);        //gun2
			break;
		}
	}
    // Key listener wdsa 0123............................................................
	public void keyPressed(KeyEvent e) {
		// My tank's direction..................................
		switch(e.getKeyCode()){
		
		case KeyEvent.VK_W:// Up................................
			this.tank1.setDirection(0);
			if(tank1.y>6&&!tank1.isTouchE())
			{
			this.tank1.moveUp();
			}
			break;
		case KeyEvent.VK_D:// Right..............................
			this.tank1.setDirection(1);
			if(tank1.x<647&&!tank1.isTouchE())
			{
			this.tank1.moveRight();
			}
			break;
		case KeyEvent.VK_S:// Down..............................
			this.tank1.setDirection(2);
			if(tank1.y<361&&!tank1.isTouchE())
			{
			this.tank1.moveDown();
			}
			break;
		case KeyEvent.VK_A:// Left...............................
			this.tank1.setDirection(3);
			if(tank1.x>12&&!tank1.isTouchE())
			{
			this.tank1.moveLeft();
			}
			break;
		}
//		if(e.getKeyCode()==KeyEvent.VK_W)
//		{
//			this.tank1.setDirection(0);
//			this.tank1.moveUp();
//		}else if(e.getKeyCode()==KeyEvent.VK_D)
//		{
//			this.tank1.setDirection(1);
//			this.tank1.moveRight();
//		}else if(e.getKeyCode()==KeyEvent.VK_S)
//		{
//			this.tank1.setDirection(2);
//			this.tank1.moveDown();
//		}else if(e.getKeyCode()==KeyEvent.VK_A)
//		{
//			this.tank1.setDirection(3);
//			this.tank1.moveLeft();
//		}
		if(e.getKeyCode()==KeyEvent.VK_J)// Fire...............................
		{
			if(this.tank1.ss.size()<5)// 5 Time!
			{
			    this.tank1.Fire();
			}
		}
		
		// Must repaint....
		repaint();
	}
	
	public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {
    	
    }


	@Override
	public void run() {
		// TODO Repaint the fire,
		while(true)
		{
			try{
				Thread.sleep(25);
				
			}catch(Exception e){
				
			}
			
			// My bullet hit enemy
			this.MyBulletHit();
			
			// Enemy's bullet hit me
			this.EnemyBulletHit();
			
			
			this.repaint();
		}
		
	}
}