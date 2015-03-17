package tank_war_2_online2;

import java.util.*;
import java.io.*;
import javax.sound.sampled.*;
import java.net.*;


//��ս������̹��----------------------------------------------------------------------------------
class NetTank extends Tank
{
	//�ӵ�����
	Vector<Shot>ss=new Vector<Shot>();
	Shot s=null;
	
	//�����ϰ���
	Vector <Barrier>bbs=new Vector <Barrier>();
	
	//��Ӯ�жϡ�����������������������������
		boolean Win;
		
	//�жϷְ���
		boolean good=true;
	
	//����
	public int Life=10;
	
	int speed=6;
	
	//���캯��
	public NetTank(int x,int y)
	{
		super(x,y);
	}
	
	//�����ӵ�
	public void Fire()
	{
		if(this.Life>0)
		{
			switch (this.direction)
			{
		
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
	// Tank �ƶ�����...............................
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
		
		//��ȥ����
		public void setLife(int life)
		{
			this.Life=life;
		}
		public int getLife()
		{
			return Life;
		}
		
		//��ײ���
		// Access panel's Barrier
	    public void setbbs(Vector<Barrier> touch1)
		{
			this.bbs=touch1;
		}
	    public boolean isTouchB()
		{
			boolean isTouchE=false;
			// ���
			int itv=2;
			// �ж�
			switch(this.direction)
			{
			case 0:// up........................................
				for(int i=0;i<bbs.size();i++)
				{
					Barrier bb=bbs.get(i);

						
							if(this.x>=bb.x&&this.x<=bb.x+bb.width&&this.y>=bb.y&&this.y<=bb.y+bb.height+itv)
							{
								return true;
							}
							if(this.x+22>=bb.x&&this.x+22<=bb.x+bb.width&&this.y>=bb.y&&this.y<=bb.y+bb.height+itv)
							{
								return true;
							}
						
				}
			break;
			case 1:// right.......................................
				for(int i=0;i<bbs.size();i++)
				{
					Barrier bb=bbs.get(i);
					
							if(this.x+26>=bb.x-itv&&this.x+26<=bb.x+bb.width&&this.y+4>=bb.y&&this.y+4<=bb.y+bb.height)
							{
								return true;
							}
							if(this.x+26>=bb.x-itv&&this.x+26<=bb.x+bb.width&&this.y+26>=bb.y&&this.y+26<=bb.y+bb.height)
							{
								return true;
							}
						
						
					
				}
			break;
			case 2:// down.......................................
				for(int i=0;i<bbs.size();i++)
				{
					Barrier bb=bbs.get(i);
					
							if(this.x>=bb.x&&this.x<=bb.x+bb.width&&this.y+30>=bb.y-itv&&this.y+30<=bb.y+bb.height)
							{
								return true;
							}
							if(this.x+22>=bb.x&&this.x+22<=bb.x+bb.width&&this.y+30>=bb.y-itv&&this.y+30<=bb.y+bb.height)
							{
								return true;
							}
						

					
				}
			break;
			case 3:// left.......................................
				for(int i=0;i<bbs.size();i++)
				{
					Barrier bb=bbs.get(i);
					

							if(this.x-4>=bb.x&&this.x-4<=bb.x+bb.width+itv&&this.y+4>=bb. y&&this.y+4<=bb.y+bb.height)
							{
								return true;
							}
							if(this.x-4>=bb.x&&this.x-4<=bb.x+bb.width+itv&&this.y+26>=bb.y&&this.y+26<=bb.y+bb.height)
							{
								return true;
							}
						

					
				}
			break;
			}
			
			return isTouchE;
		}

}

//�ϰ���---------------------------------------------------------------------------------------
class Barrier
{
	int x,y,width,height,type;
	//���캯������������������������������������������������������������������
	public Barrier(int x,int y,int width,int height,int type)
	{
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.type=type;
	}
	
//��ȡ��������������������������������������������������������������������������
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	//����������������������������������������������������������
}



//��Ϣ��
class Message 
{
	int x,y,color,drection;
//	String type;
	
	public Message(int x,int y,int drection,int color)
	{
//		this.type=type;
//		if(type.equals("x"))
//		{
//			this.x=m;
//		}
//		if(type.equals("y"))
//		{
//			this.y=m;
//		}
//		if(type.equals("color"))
//		{
//			this.color=m;
//		}
//		if(type.equals("drection"))
//		{
//			this.drection=m;
//		}
		this.x=x;
		this.y=y;
		this.drection=drection;
		this.color=color;
	}
	
	public void send (int x,int y,int drection,int color)
	{
		
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

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getDrection() {
		return drection;
	}

	public void setDrection(int drection) {
		this.drection = drection;
	}
	
	
}








