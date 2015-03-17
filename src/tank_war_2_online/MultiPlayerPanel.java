package tank_war_2_online;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

// Panel
class Paint extends JPanel implements KeyListener,Runnable
{
	//窗口判断
	static boolean exit=false;
	// 声明 我的坦克
	NetTank tank1=null;
	
	// Bang's process
	Vector<Bang> ba=new Vector<Bang>();
		
	Color c1,c2;
		
	// Biagbang's  three picture
	Image bang1=null;
	Image bang2=null;
	Image bang3=null;
	
	public Vector <Barrier>bbs=new Vector <Barrier>();
    int bbSize;
    
//网络编程所需变量.................................................    
	//构造玩伴信息
    int x2=800;
    int y2;
    int direction2;
    int color2;
    int id2;
	boolean good2;

	//声明变量
	Socket s=null;
    //声明变量
	DataInputStream dis=null;
	DataOutputStream dos=null;
	static //服务器IP
	String ip="127.0.0.1";
	static int p;
	//循环
	boolean bConnect=false;
	
	//信息
	NetTank tank2=null;
	String in=null;
	String out=null;	
	int id;
	
	//线程
	Thread rt=new Thread(new RecieveThread());
	
//	Thread st=new Thread(new SendThread());
	
    
    
    public Paint(String flag)
    {
    	this.setSize(1000,500);
    	
    	exit=true;
    	

    	
    	if(flag.equals("netgame"))
    	{

    		//链接
    		Connect();
    		//建立我自己并判断我的初始位置与帮派
    		if(id%2==0){
    		tank1=new NetTank(15,320);
    		tank1.setColor(0);
    		tank1.setDirection(0);
    		tank1.good=true;
    		}
    		else
    		{
    			tank1=new NetTank(650,15);
    			tank1.setDirection(2);
    			tank1.setColor(2);
    			tank1.good=false;
    		}
//    		st.start();
			rt.start();
    		
    		tank1.setbbs(bbs);
    		
   			tank2=new NetTank(x2,y2);

    		
    		//障碍物位置
   			Barrier bb1=new Barrier(100,50,100,80,1);//左半边
   			bbs.add(bb1);
   			Barrier bb2=new Barrier(100,160,100,80,1);
   			bbs.add(bb2);
   			Barrier bb3=new Barrier(100,270,100,80,1);
   			bbs.add(bb3);
   			Barrier bb4=new Barrier(500,50,100,80,1);//右半边
   			bbs.add(bb4);
   			Barrier bb5=new Barrier(500,160,100,80,1);
   			bbs.add(bb5);
   			Barrier bb6=new Barrier(500,270,100,80,1);
   			bbs.add(bb6);
   			Barrier bb7=new Barrier(250,50,200,125,1);//中间一竖行
   			bbs.add(bb7);
   			Barrier bb8=new Barrier(250,225,200,125,1);
   			bbs.add(bb8);
//			for(int i=0;i<3;i++)
//			{
//				for(int j=0;j<3;j++)
//				{
//					Barrier bb=new Barrier((2*i+1)*100,j*(300/3)+50,100,400/3-65,1);
//					bbs.add(bb);
//				}
//			}
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
    }
    
    public void paint(Graphics g)
	{   

		super.paint(g);
				
		g.fillRect(0, 0, 700, 400);
		
		// Add barrier
		for(int i=0;i<bbs.size();i++)
		{
			Barrier bb=bbs.get(i);
			g.setColor(Color.gray);
			g.fill3DRect(bb.x, bb.y, bb.width, bb.height, true);
		}

		
		// Add tank(ME)........................................		
		if(tank1.getLife()>0)
		{
			
		this.PaintTank(tank1.getX(), tank1.getY(), g,tank1.getDirection(),tank1.getColor());
		this.PaintHealthy(tank1.getX(), tank1.getY(), g, tank1);
		
		}
//		if(tank1.getLife()==0)
//		{
//			tank1.setX(800);
//			tank1.setY(0);
//			
//		}
		//Add tank(HE).............................................
		if(tank2.getX()!=800&&tank2.getLife()>0)
		{
		this.PaintTank(tank2.getX(), tank2.getY(), g, tank2.getDirection(), tank2.getColor());
		this.PaintHealthy(tank2.getX(), tank2.getY(), g, tank2);
		
		}
//		if(tank2.getLife()==0)
//		{	
//			this.PaintTank(800, 0, g, tank2.getDirection(), tank2.getColor());
//			tank2.setX(800);
//			tank2.setY(0);
//
//			
//		}
		
		// Add Many MY Fire...................................
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
		// Add Other Player's Many Fire...................................
				for(int i=0;i<tank2.ss.size();i++)
				{
					
				// Add fire........................................
					Shot ms=tank2.ss.get(i);
				if(ms!=null&&ms.noBang==true){
					g.setColor(Color.red);
					g.draw3DRect(ms.x, ms.y, 2, 2, false);
				}
				
				// Remove fire....................................
				if(ms.noBang==false)
				{
					tank2.ss.remove(ms);
				}
				}
				
		// Add Big Bang ! ................................
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

	}
    
	// Draw Tank------------------------------------------------------------------------------------
	public void PaintTank (int x,int y,Graphics g,int direct,int type)
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
	//画血条-----------------------------------------------------------------------------------------
	public void PaintHealthy(int x,int y,Graphics g,NetTank t)
	{
		g.setColor(Color.red);
		g.drawRect(x, y-13, 21, 7);
		if(t.getLife()>7&&t.getLife()<=10)
			g.setColor(Color.green);
		if(t.getLife()<=7&&t.getLife()>4)
			g.setColor(Color.yellow);
		if(t.getLife()<=4&&t.getLife()>2)
			g.setColor(Color.orange);
		if(t.getLife()<=2&&t.getLife()>0)
			g.setColor(Color.red);
		g.fillRect(x+1, y-12, t.getLife()*2, 5);
	}
	//判断是否击中障碍物---------------------------------------------------------------------------------
	public void hitBarrier(NetTank t)
	{
		NetTank t1=t;
		
		for(int i=0;i<bbs.size();i++)
		{
			Barrier bb=bbs.get(i);
			for(int j=0; j<t1.ss.size();j++)
			{
				Shot s=t1.ss.get(j);

		        	if(s.x>=bb.x && s.x<=(bb.x+bb.width) && s.y>=bb.y && s.y<=bb.y+bb.height)
		        	{
		        		s.noBang=false;
		        		t1.ss.remove(j);
		        	}
			}
		}
	}
	//判断对手大中午我否----------------------------------------------------------------------------------
	public boolean hitPlayer(Shot s,NetTank t)
	{
		boolean b2=false;
		
		if(t.noBang==true)
		{
		    switch(t.getDirection())
		    {
		    case 0:
		    case 2:
			    if(s.x>=t.x&&s.x<=t.x+22&&s.y>=t.y-1&&s.y<=t.y+30)
			    {
				    //bullet dead
				    s.noBang=false;
				
				    // sound
				    AePlayWave apw=new AePlayWave("auBang.wav");
				    apw.start();
				
				    b2=true;
			    }
			    break;
		    case 1:
		    case 3:
			    if(s.x>=t.x-5&&s.x<=t.x+26&&s.y>=t.y+4&&s.y<=t.y+26)
			    {
				    //bullet dead
				    s.noBang=false;
				
				    // sound
				    AePlayWave apw=new AePlayWave("auBang.wav");
				    apw.start();
				
				    b2=true;
			    }
			    break;
		    }
		}
		return b2;
	}
	//判断是否集中我--------------------------------------------------------------------------------------
	public void hitMe()
	{
		for(int i=0;i<tank2.ss.size();i++)
		{
			Shot s=tank2.ss.get(i);
			if(this.hitPlayer(s, tank1))
			{
				//I'm not healthy
				tank1.Life--;
				// Big Bang !!!!!
				Bang b=new Bang(tank1.x,tank1.y);
				ba.add(b);
				
				if(tank1.getLife()==0)
				{
					//移出坦克
//					tank1.setX(800);
					tank1.noBang=false;
					//出现对话框
			    	JFrame frame=new JFrame("Sorry");
				    JPanel panel=new JPanel();
				    JLabel label=new JLabel("You are strong. But he is mroe stronger than you");
				    JButton jb1=new JButton("Again?");
//				    jb1.addActionListener(new ButtonListener());
//				    TankWar.AgainGame jb1=new AgainGame();
				    panel.add(label);
			    	panel.add(jb1);
			    	frame.add(panel);
				
			    	frame.setVisible(true);
			    	frame.pack();
				}
			}
		}
	}
	//判断是否击中他-------------------------------------------------------------------------------------
	public void hitHe()
	{
		for(int i=0;i<tank1.ss.size();i++)
		{
			Shot s=tank1.ss.get(i);
			if(this.hitPlayer(s, tank2))
			{
				//I'm not healthy
				tank2.Life--;
				// Big Bang !!!!!
				Bang b=new Bang(tank2.x,tank2.y);
				ba.add(b);
				if(tank2.getLife()==0)
				{
					//移出坦克
//					tank2.setX(800);
					tank2.noBang=false;
					//出现对话框
			    	JFrame frame=new JFrame("Congratulation");
				    JPanel panel=new JPanel();
				    JLabel label=new JLabel("Congratulation! \nYou win!");
				    JButton jb1=new JButton("Again?");
				    panel.add(label);
			    	panel.add(jb1);
			    	frame.add(panel);
				
			    	frame.setVisible(true);
			    	frame.pack();
				}
			}
		}
	}
	
	//内部监听类
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
//			new TankWar().AgainGame("two");

		}
	}

	// Key listener wdsa 0123............................................................
		public void keyPressed(KeyEvent e) {
			// My tank's direction..................................
			switch(e.getKeyCode()){
			
			case KeyEvent.VK_W:// Up................................
				this.tank1.setDirection(0);
				if(tank1.y>6&&!tank1.isTouchB())
				{
				this.tank1.moveUp();
				}
				break;
			case KeyEvent.VK_D:// Right..............................
				this.tank1.setDirection(1);
				if(tank1.x<666&&!tank1.isTouchB())
				{
				this.tank1.moveRight();
				}
				break;
			case KeyEvent.VK_S:// Down..............................
				this.tank1.setDirection(2);
				if(tank1.y<361&&!tank1.isTouchB())
				{
				this.tank1.moveDown();
				}
				break;
			case KeyEvent.VK_A:// Left...............................
				this.tank1.setDirection(3);
				if(tank1.x>12&&!tank1.isTouchB())
				{
				this.tank1.moveLeft();
				}
				break;
			}

			if(e.getKeyCode()==KeyEvent.VK_J)// Fire...............................
			{
				if(this.tank1.ss.size()<5)// 5 Time!
				{
				    this.tank1.Fire();
				}
				//向上传输子弹信息
				try {
					dos.writeUTF("fire "+id);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			//以字符串的形式上传信息
			try {
				dos.writeUTF(tank1.x+" "+tank1.y+" "+tank1.direction+" "+tank1.color+" "+id);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
			
			
			// Must repaint....
			repaint();
		}
		
		public void keyTyped(KeyEvent e) {}
	    public void keyReleased(KeyEvent e) {}
	    
//	    //断开连接-------------------------------------------------------------------------------
//	    public void DisConnect()
//	    {
//	    	tc.DisConnect();
//	    }
//	    
//	    //得到客户端组
//	    public static void clints(Vector<Client> clients1)
//	    {
//	    	clients=clients1;
//	    }
//	    
//	    public static void getMsg(int x,int y ,int direction,int color ,boolean good ,int id)
//	    {
//	    	x2=x;
//	    	y2=y;
//	    	direction2=direction;
//	    	color2=color;
//	    	good2=good;
//	    	id2=id;
//	    }
//客户端连接---------------------------------------------------------------------------------------------------------------	    
    public void Connect()
    {
    	try {
			//建立连接
			s = new Socket(ip, p);
			//建立通道
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			//开启传送与接收
			this.id=dis.readInt();
			
            //循环
			bConnect = true;
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    }
    
    public void DisConnect()
    {
        try {
			
			dis.close();
			dos.close();
			
			s.close();
			
		} catch (Exception e) {
		
			System.out.println("别想他了~");
			
		}
    	
    }
    
  //读取服务器的线程--------------------------------------------------------------------------------
    private class RecieveThread implements Runnable
    {
	    public void run() {
		
		    try { 
			 
		    	while(bConnect)
			    {

				    //读取服务器信息
				    String msg=dis.readUTF();
				    //将信息赋值于战场
				    
				    if(msg.length()>7){
				    String []etc=msg.split(" "); 
				    id2=Integer.parseInt(etc[4]);
				    if(id2!=id)
				    {
				        x2=Integer.parseInt(etc[0]);
				        y2=Integer.parseInt(etc[1]);
				        direction2=Integer.parseInt(etc[2]);
				        color2=Integer.parseInt(etc[3]);
				   
				        good2=false;
				        if(id2%2==0)
				        {
				          	good2=true;
				        }
				    }
				    
				    }
				    
				    else{
				    	 String []etc=msg.split(" "); 
						 id2=Integer.parseInt(etc[1]);
						 if(id2!=id)
						 {
							 if(tank2.ss.size()<5)
							 {
								 tank2.Fire();
							 }
							 
						 }
				    }

		    	   }
			
		    }
		    catch (Exception e) 
		    {
		    	System.out.println("读取有小错，不好避免，就算了吧~");

		    }

		
	}
	}
    
    //得到IP与端口
    public static void getIP(String ip1,int p1)
    {
    	ip=ip1;
//    	p=Integer.parseInt(P1);
    	p=p1;
    }
	//发送线程-------------------------------------------------------------------------------------
//    private class SendThread implements Runnable
//    {
//		public void run() {
//			
//			try {
//				
//				while(bConnect)
//				{
////					dos.writeUTF(tank1.x+" "+tank1.y+" "+tank1.direction+" "+tank1.color+" "+id);
//					dos.writeInt(id);
//					dos.writeInt(tank1.x);
//					dos.writeInt(tank1.y);
//					dos.writeInt(tank1.direction);
//					dos.writeInt(tank1.color);
////					System.out.println("2 "+tank1.x+" "+tank1.y+" "+tank1.direction+" "+tank1.color+" "+tank1.good+" "+id);
//				}
//				
//			} catch (Exception e) {
//
//			}
//			
//		}
//    	
//    }
    
    
	    
//线程------------------------------------------------------------------------------------------------------------	    
		public void run() {
			
			

			while(true)
			{
				try{
					Thread.sleep(25);
					
				}catch(Exception e){
					
				}
				
				this.hitBarrier(tank1);
				
				this.hitBarrier(tank2);
				
				this.hitMe();
				this.hitHe();
//				if(id!=id2)
				{
					tank2.setColor(color2);
	    			tank2.setDirection(direction2);
	    			tank2.setX(x2);
	    			tank2.setY(y2);
//					System.out.println(x2+" "+y2+" "+direction2+" "+color2+" "+id2);

				}
				
				this.repaint();
			}
			}
		
		
		

}