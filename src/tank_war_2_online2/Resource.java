package tank_war_2_online2;

import java.util.*;
import java.io.*;

import javax.sound.sampled.*;

// Notebook!!-------------------------------------------------------------------------------------
class Recorder
{
	private static int EnemyCount=5;
	
	private static int MyLife=3;
	
	private static int AllBang=0;
	
	private static FileWriter fw=null;
	private static BufferedWriter bw=null;
	private static FileReader fr=null;
	private static BufferedReader br=null;
	
	private static Vector<EnemyTank> ets=new Vector<EnemyTank>();
	
	//记录点
	static Vector<Node> nodes=new Vector<Node>();
	
	// read the tank's Game
	public Vector<Node> LoadGame()
	{
        try {
			
			fr=new FileReader ("D:\\Documents\\TankWarSave\\SinglePlayer.txt");
			br=new BufferedReader(fr);
			String n="";
			n=br.readLine();
			AllBang=Integer.parseInt(n);
			while((n=br.readLine())!=null)
			{
				String[] etc=n.split(" ");

					Node node=new Node(Integer.parseInt(etc[0]),Integer.parseInt(etc[1]),Integer.parseInt(etc[2]),Integer.parseInt(etc[3]));
					nodes.add(node);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			File f=new File("D:\\Documents\\TankWarSave\\SinglePlayer.txt");
//			AllBang=0;
			
		} finally
		{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
        return nodes;
	}

	//save the tank's x,y,d,.......
	public static void SaveGame()
	{
		// 存allbang
		try 
		{			
			fw=new FileWriter("D:\\Documents\\TankWarSave\\SinglePlayer.txt");
			bw=new BufferedWriter(fw);
					
			bw.write(AllBang+"\r\n");
					
			// 敌人的坐标方向类型
			for(int i=0;i<ets.size();i++)
			{
				
				//save no bang tank
				EnemyTank et=ets.get(i);
					
				if(et.noBang)
				{
					String message=et.getX()+" "+et.getY()+" "+et.getDirection()+" "+et.getColor();
						
					bw.write(message+"\r\n");
				}
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			// TODO: handle exception
		}
		finally
		{
			try {
						
				bw.close();
				fw.close();
					
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
	}
	
	// Reader the all bang..........................................
	public static void ReadAllBang()
	{
		try {
			
			fr=new FileReader ("D:\\Documents\\TankWarSave\\SinglePlayer.txt");
			br=new BufferedReader(fr);
			String n=br.readLine();
			AllBang=Integer.parseInt(n);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally
		{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	// Save the all bang
	public static void SaveAllBang()
	{
		// 存allbang
		try {
			
			fw=new FileWriter("D:\\Documents\\TankWarSave\\SinglePlayer.txt");
			bw=new BufferedWriter(fw);
			
			bw.write(AllBang+"\r\n");
			
		} 
		catch (Exception e)
		{
			// TODO: handle exception
		}
		finally
		{
			try {
				
				bw.close();
				fw.close();
				
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	// GET SET...............................................
	public static int getAllBang() {
		return AllBang;
	}

	public static void setAllBang(int allBang) {
		AllBang = allBang;
	}

	public static int getEnemyCount() {
		return EnemyCount;
	}

	public static void setEnemyCount(int enemyCount) {
		EnemyCount = enemyCount;
	}

	public static int getMyLife() {
		return MyLife;
	}

	public static void setMyLife(int myLife) {
		MyLife = myLife;
	}
	
	public static Vector<EnemyTank> getEts() {
		return ets;
	}

	public static void setEts(Vector<EnemyTank> ets) {
		Recorder.ets = ets;
	}
	
	//................................................
	
	public static void Decrease()
	{
		EnemyCount--;
	}
	public static void Increase()
	{
		AllBang++;
	}
	public static void DecreaseMe()
	{
		MyLife--;
	}

	
}
// Nodes 读取分段点
class Node
{
	int x,y,drection,color;
	
	public Node(int x,int y,int drection,int color)
	{
		this.x=x;
		this.y=y;
		this.drection=drection;
		this.color=color;
	}
}
// Sound------------------------------------------------------------------------------------------
class AePlayWave extends Thread
{
	private String filename;
	public AePlayWave(String name)
	{
		filename=name;
	}
	
	public void run()
	{
		File soundfile=new File(filename);
		AudioInputStream ais=null;
		
		try {
			ais=AudioSystem.getAudioInputStream(soundfile);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
		AudioFormat format=ais.getFormat();
		SourceDataLine auline=null;
		DataLine.Info info=new DataLine.Info(SourceDataLine.class, format);/////////////////////////
		
		try {
			
			auline=(SourceDataLine) AudioSystem.getLine(info);/////////////////////////////////
			auline.open(format);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		auline.start();
		
		int n=0;
		byte [] adDate=new byte[1024];
		try {
			
			while(n!=-1)
			{
				n=ais.read(adDate,0,adDate.length);/////////////////////////////////////
				if(n>=0)
				{
					auline.write(adDate,0,n);
					
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			auline.drain();
			auline.close();
			try {
				ais.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
// Bullet-----------------------------------------------------------------------------------------
class Shot implements Runnable{
	
	int x;
	int y;
	int direction;
	int speed=3;
	boolean noBang=true;
	
	public Shot(int x,int y,int direction)
	{
		this.x=x;
		this.y=y;
		this.direction=direction;
	}
	
	public void run() {
		
		while(true)			
		{
			try{
				Thread.sleep(25);
			}catch(Exception e){
				
			}
			switch(direction)// The running fire!!....................
			{
			case 0:
				y-=speed;
				break;
			case 1:
				x+=speed;
				break;
			case 2:
				y+=speed;
				break;
			case 3:
				x-=speed;
				break;
			}
			
		// Edge..............................
			if(x<0||x>700||y<0||y>400)
			{
				this.noBang=false;
				break;
			}
		}
			
	}
}

// Bang-----------------------------------------------------------------------------------------
class Bang
{
	int x;
	int y;
	int time=40;
	boolean noBang=true;
	
	public Bang(int x,int y)
	{
		this.x=x;
		this.y=y;
				
	}
	
	public void BangDown()
	{
		if(time>0)
		{
			time--;
		}
		else
		{
			this.noBang=false;
		}
	}
	
}



// Tank-----------------------------------------------------------------------------------------
class Tank
{
	// tank's position
	int x;
	int y;
	//Tank's direction
	int direction;
	// Tank's speed
	int speed=4;
	// Tank's color
	int color;
	// Tank is live
	boolean noBang=true;
	
	public Tank(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}

	
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}

	
	public int getDirection(){
		return direction;
	}
	public void setDirection(int direction){
		this.direction=direction;
	}

	
	public int getSpeed(){
		return speed;
	}
	public void setSpeed(int speed){
		this.speed=speed;
	}
	
	
	public int getColor(){
		return color;
	}
	public void setColor(int color){
		this.color=color;
	}
	
}

//My tank----------------------------------------------------------------------------------------------
class MyTank extends Tank
{
	Vector<Shot>ss=new Vector<Shot>();
	Shot s=null;
	
	//定义向量，can access the panel's Enemy tank
		Vector <EnemyTank> ets=new Vector<EnemyTank>();
	
	//输赢判断。。。。。。。。。。。。。。。
		boolean Win;
	
	public MyTank(int x,int y)
	{
		super(x, y);
	}

	public void Fire()// Fire................................s
	{
		if(Recorder.getMyLife()>0)
		{
		switch (this.direction){
		
		case 0:
			s=new Shot(x+10,y-6,0);
			ss.add(s);
			break;
		case 1:
			s=new Shot(x+30,y+14,1);
			ss.add(s);
			break;
		case 2:
			s=new Shot(x+10,y+35,2);
			ss.add(s);
			break;
		case 3:	
			s=new Shot(x-11,y+14,3);
			ss.add(s);
			break;
		}
		AePlayWave apw=new AePlayWave("auBiu.wav");
		apw.start();
		Thread t=new Thread(s);
		t.start();
		
	}
	}
	
	// Tank go up...............................
	public void moveUp()
	{
		this.y-=this.speed;
	}
	
	public void moveRight()
	{
		this.x+=this.speed;
	}
	
	public void moveDown()
	{
		this.y+=this.speed;
	}
	
	public void moveLeft()
	{
		this.x-=this.speed;
	}
	
	//碰撞检测
	// Access panel's Enemy tank
		public void setets(Vector<EnemyTank> touch1)
		{
			this.ets=touch1;
		}
		
		// 判断if touch the enemy tank
		public boolean isTouchE()
		{
			boolean isTouchE=false;
			// 间隔
			int itv=2;
			// 判断
			switch(this.direction)
			{
			case 0:// up........................................
				for(int i=0;i<ets.size();i++)
				{
					EnemyTank et=ets.get(i);

						if(et.direction==0||et.direction==2)/////
						{
							if(this.x>=et.x&&this.x<=et.x+22&&this.y>=et.y&&this.y<=et.y+30+itv)
							{
								return true;
							}
							if(this.x+22>=et.x&&this.x+22<=et.x+22&&this.y>=et.y&&this.y<=et.y+30+itv)
							{
								return true;
							}
						}
						if(et.direction==1||et.direction==3)/////
						{
							if(this.x>=et.x-4&&this.x<=et.x+26&&this.y>=et.y+4&&this.y<=et.y+26+itv)
							{
								return true;
							}
							if(this.x+22>=et.x-4&&this.x+22<=et.x+26&&this.y>=et.y+4&&this.y<=et.y+26+itv)
							{
								return true;
							}
						}
					
				}
			break;
			case 1:// right.......................................
				for(int i=0;i<ets.size();i++)
				{
					EnemyTank et=ets.get(i);
					
						if(et.direction==0||et.direction==2)
						{
							if(this.x+26>=et.x-itv&&this.x+26<=et.x+22&&this.y+4>=et.y&&this.y+4<=et.y+30)
							{
								return true;
							}
							if(this.x+26>=et.x-itv&&this.x+26<=et.x+22&&this.y+26>=et.y&&this.y+26<=et.y+30)
							{
								return true;
							}
						}
						if(et.direction==1||et.direction==3)
						{
							if(this.x+26>=et.x-4-itv&&this.x+26<=et.x+26&&this.y+4>=et.y+4&&this.y+4<=et.y+26)
							{
								return true;
							}
							if(this.x+26>=et.x-4-itv&&this.x+26<=et.x+26&&this.y+26>=et.y+4&&this.y+26<=et.y+26)
							{
								return true;
							}
						}
					
				}
			break;
			case 2:// down.......................................
				for(int i=0;i<ets.size();i++)
				{
					EnemyTank et=ets.get(i);
					
						if(et.direction==0||et.direction==2)
						{
							if(this.x>=et.x&&this.x<=et.x+22&&this.y+30>=et.y-itv&&this.y+30<=et.y+30)
							{
								return true;
							}
							if(this.x+22>=et.x&&this.x+22<=et.x+22&&this.y+30>=et.y-itv&&this.y+30<=et.y+30)
							{
								return true;
							}
						}
						if(et.direction==1||et.direction==3)
						{
							if(this.x>=et.x-4&&this.x<=et.x+26&&this.y+30>=et.y+4-itv&&this.y+30<=et.y+26)
							{
								return true;
							}
							if(this.x+22>=et.x-4&&this.x+22<=et.x+26&&this.y+30>=et.y+4-itv&&this.y+30<=et.y+26)
							{
								return true;
							}
						}
					
				}
			break;
			case 3:// left.......................................
				for(int i=0;i<ets.size();i++)
				{
					EnemyTank et=ets.get(i);
					
						if(et.direction==0||et.direction==2)
						{
							if(this.x-4>=et.x&&this.x-4<=et.x+22+itv&&this.y+4>=et. y&&this.y+4<=et.y+30)
							{
								return true;
							}
							if(this.x-4>=et.x&&this.x-4<=et.x+22+itv&&this.y+26>=et.y&&this.y+26<=et.y+30)
							{
								return true;
							}
						}
						if(et.direction==1||et.direction==3)
						{
							if(this.x-4>=et.x-4&&this.x-4<=et.x+26+itv&&this.y+4>=et.y+4&&this.y+4<=et.y+26)
							{
								return true;
							}
							if(this.x-4>=et.x-4&&this.x-4<=et.x+26+itv&&this.y+26>=et.y+4&&this.y+26<=et.y+26)
							{
								return true;
							}
						}
					
				}
			break;
			}
			
			return isTouchE;
		}

	
}

// Enemy Tank----------------------------------------------------------------------------------------

class EnemyTank extends Tank implements Runnable
{
	// 子弹周期
	int time=0;
	
	//定义向量，can access the panel's Enemy tank
	Vector <EnemyTank> ets=new Vector<EnemyTank>();
	
	
	// Add bullet
	Vector<Shot> ss=new Vector();

	public EnemyTank(int x, int y)
	{
		super(x, y);
	}
	
	// Access panel's Enemy tank
	public void setets(Vector<EnemyTank> touch1)
	{
		this.ets=touch1;
	}
	
	// 判断if touch the enemy tank
	public boolean isTouchE()
	{
		boolean isTouchE=false;
		// 间隔
		int itv=2;
		// 判断
		switch(this.direction)
		{
		case 0:// up........................................
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				if(et!=this)
				{
					if(et.direction==0||et.direction==2)/////
					{
						if(this.x>=et.x&&this.x<=et.x+22&&this.y>=et.y&&this.y<=et.y+30+itv)
						{
							return true;
						}
						if(this.x+22>=et.x&&this.x+22<=et.x+22&&this.y>=et.y&&this.y<=et.y+30+itv)
						{
							return true;
						}
					}
					if(et.direction==1||et.direction==3)/////
					{
						if(this.x>=et.x-4&&this.x<=et.x+26&&this.y>=et.y+4&&this.y<=et.y+26+itv)
						{
							return true;
						}
						if(this.x+22>=et.x-4&&this.x+22<=et.x+26&&this.y>=et.y+4&&this.y<=et.y+26+itv)
						{
							return true;
						}
					}
				}
			}
		break;
		case 1:// right.......................................
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				if(et!=this)
				{
					if(et.direction==0||et.direction==2)
					{
						if(this.x+26>=et.x-itv&&this.x+26<=et.x+22&&this.y+4>=et.y&&this.y+4<=et.y+30)
						{
							return true;
						}
						if(this.x+26>=et.x-itv&&this.x+26<=et.x+22&&this.y+26>=et.y&&this.y+26<=et.y+30)
						{
							return true;
						}
					}
					if(et.direction==1||et.direction==3)
					{
						if(this.x+26>=et.x-4-itv&&this.x+26<=et.x+26&&this.y+4>=et.y+4&&this.y+4<=et.y+26)
						{
							return true;
						}
						if(this.x+26>=et.x-4-itv&&this.x+26<=et.x+26&&this.y+26>=et.y+4&&this.y+26<=et.y+26)
						{
							return true;
						}
					}
				}
			}
		break;
		case 2:// down.......................................
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				if(et!=this)
				{
					if(et.direction==0||et.direction==2)
					{
						if(this.x>=et.x&&this.x<=et.x+22&&this.y+30>=et.y-itv&&this.y+30<=et.y+30)
						{
							return true;
						}
						if(this.x+22>=et.x&&this.x+22<=et.x+22&&this.y+30>=et.y-itv&&this.y+30<=et.y+30)
						{
							return true;
						}
					}
					if(et.direction==1||et.direction==3)
					{
						if(this.x>=et.x-4&&this.x<=et.x+26&&this.y+30>=et.y+4-itv&&this.y+30<=et.y+26)
						{
							return true;
						}
						if(this.x+22>=et.x-4&&this.x+22<=et.x+26&&this.y+30>=et.y+4-itv&&this.y+30<=et.y+26)
						{
							return true;
						}
					}
				}
			}
		break;
		case 3:// left.......................................
			for(int i=0;i<ets.size();i++)
			{
				EnemyTank et=ets.get(i);
				if(et!=this)
				{
					if(et.direction==0||et.direction==2)
					{
						if(this.x-4>=et.x&&this.x-4<=et.x+22+itv&&this.y+4>=et. y&&this.y+4<=et.y+30)
						{
							return true;
						}
						if(this.x-4>=et.x&&this.x-4<=et.x+22+itv&&this.y+26>=et.y&&this.y+26<=et.y+30)
						{
							return true;
						}
					}
					if(et.direction==1||et.direction==3)
					{
						if(this.x-4>=et.x-4&&this.x-4<=et.x+26+itv&&this.y+4>=et.y+4&&this.y+4<=et.y+26)
						{
							return true;
						}
						if(this.x-4>=et.x-4&&this.x-4<=et.x+26+itv&&this.y+26>=et.y+4&&this.y+26<=et.y+26)
						{
							return true;
						}
					}
				}
			}
		break;
		}
		
		return isTouchE;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(50);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			switch(this.direction)
			{
			case 0:
				for(int i=0;i<15;i++){
					if(y>6&&!this.isTouchE())
					y-=speed;
					try {
						Thread.sleep(80);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			case 1:
				for(int i=0;i<15;i++){
					if(x<647&&!this.isTouchE())
					x+=speed;
					try {
						Thread.sleep(80);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			case 2:
				for(int i=0;i<15;i++){
					if(y<361&&!this.isTouchE())
					y+=speed;
					try {
						Thread.sleep(80);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				break;
			case 3:
				for(int i=0;i<15;i++){
					if(x>12&&!this.isTouchE())
					x-=speed;
					try {
						Thread.sleep(80);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			    break;
			}
			// Many bullet...........................
			this.time++;
			if(time%1==0)// control the bullet's frequency
			{
				if(noBang)
				{
					if(ss.size()<6)
					{
						Shot s=null;
						
						switch(direction)
						{
						case 0:
							s=new Shot(x+10,y-6,0);
							//sound
							AePlayWave apw1=new AePlayWave("auBiu.wav");
							apw1.start();
							ss.add(s);
							break;
						case 1:
							s=new Shot(x+30,y+14,1);
							//sound
							AePlayWave apw2=new AePlayWave("auBiu.wav");
							apw2.start();
							ss.add(s);
							break;
						case 2:
							s=new Shot(x+10,y+35,2);
							//sound
							AePlayWave apw3=new AePlayWave("auBiu.wav");
							apw3.start();
							ss.add(s);
							break;
						case 3:
							s=new Shot(x-11,y+14,3);
							//sound
							AePlayWave apw4=new AePlayWave("auBiu.wav");
							apw4.start();
							ss.add(s);
							break;
						}
						Thread t=new Thread(s);
						t.start();
					}
				}
			}
			//...................................................
			// Random direction!
			this.direction=(int)(Math.random()*4);
			
			// Dead
			if(this.noBang==false)
			{
				break;
			}
			
		}
	}
	
	
}
