package tank_war_2_online;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.*;

//Game center.-----------------------------------------------------------------------------------------
public class TankWar extends JFrame implements ActionListener{
	
	// tank war map
	Draw window =null;
	
	// net tank war map
	Paint windownet=null;
	// start map
	StartPanel stp=null;
	// 横条
	JMenuBar mb=null;
	// game选项
	JMenu m1=null;
	JMenu m2=null;
	JMenu m3=null;
	JMenu m4=null;
	// game start
	JMenuItem mi1=null;
	JMenuItem mi2=null;
	JMenuItem mi3=null;
	JMenuItem mi4=null;
	JMenuItem mi5=null;
	JMenuItem mi6=null;
	
	
	//主机地址与端口
	String ip;
	int pour;
	TextArea ta=null;
	TextArea tb=null;
	JFrame input=null;
	
	public static void main (String[] args)
	{
		TankWar map=new TankWar();	
		
	}
// 构造	
	public TankWar()
	{
		
		stp=new StartPanel();
		Thread t1=new Thread(stp);
		t1.start();
		this.add(stp);
		
		// 设置横条..........................
		mb=new JMenuBar();             //.
		//设置游戏选项                                                                           //.
		m1=new JMenu("游戏(G)");         //.
		m1.setMnemonic('g');           //.
        m2=new JMenu("嘿嘿~");          //.
        m2.setMnemonic('x');           //.
        m3=new JMenu("开始游戏(B)");      //.
        m3.setMnemonic('b');           //. 
		// game start                  //.
		mi1=new JMenuItem("单人游戏(O)"); //.
		mi1.setMnemonic('O');          //.
		mi1.addActionListener(this);   //.
		mi1.setActionCommand("start"); //.
		// system exit                 //.
		mi2=new JMenuItem("退出(E)");   //.
		mi2.setMnemonic('e');          //.
		mi2.addActionListener(this);   //.
		mi2.setActionCommand("exit");  //.
		// save and exit               //.
		mi3=new JMenuItem("保存并退出(S)");//.
		mi3.setMnemonic('s');          //.
		mi3.addActionListener(this);   //.
		mi3.setActionCommand("save");  //.
		// load game                   //.
		mi4=new JMenuItem("读取(L)");   //.
		mi4.setMnemonic('l');          //.
		mi4.addActionListener(this);   //.
		mi4.setActionCommand("load");  //.
		//net work style               //.
		mi5=new JMenuItem("多人对战(N)"); //.
		mi5.setMnemonic('n');          //.
		mi5.addActionListener(this);   //.
		mi5.setActionCommand("net");   //.
		mi6=new JMenuItem("点我有惊喜哦~");//.
		mi6.addActionListener(this);   //.
		mi6.setActionCommand("haha");  //.
		// Add item                    //.
		m1.add(m3);                    //.
		m3.add(mi1);                   //.
		m3.add(mi5);                   //.
		m1.addSeparator();             //.
		m1.add(mi3);                   //.
		m1.add(mi4);                   //.
		m1.addSeparator();             //.
		m1.add(mi2);                   //.
		m2.add(mi6);                   //.
		mb.add(m1);                    //.
		mb.add(m2);                    //.
		this.setJMenuBar(mb);          //.
		//................................
		
		this.setSize(1000,500);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		//start game..............................
		if(e.getActionCommand().equals("start"))
		{
			this.remove(stp);

			
			window=new Draw("NewGame");
			
			// Start the window 线程。
			Thread t =new Thread(window);
			t.start();
			this.add(window);
			this.addKeyListener(window);
			
			this.repaint();
		}
		// net work..................................
		if(e.getActionCommand().equals("net"))
		{
//            this.remove(stp);
//
//			
//			windownet=new Paint("netgame");
//
//			
//			// Start the window 线程。
//			Thread t =new Thread(windownet);
//			t.start();
//			this.add(windownet);
//			this.addKeyListener(windownet);
//			
//			this.repaint();
			
			input=new JFrame("请输入ip与端口号~");
			
			JLabel l1=new JLabel("IP:");
			JLabel l2=new JLabel("端口:");
			JButton b1=new JButton("确定");
			b1.addActionListener(this);
			b1.setActionCommand("netwindow");
			
			ta=new TextArea();
//			ta.setSize(100, 20);
			ta.setPreferredSize(new Dimension(115,20));
			
			tb=new TextArea();
			tb.setPreferredSize(new Dimension(50,20));
			
			JPanel jp=new JPanel();
			jp.add(l1);
			jp.add(ta);
			jp.add(l2);
			jp.add(tb);
			jp.add(b1);
			
			input.add(jp);
			
			input.setVisible(true);
			input.pack();
			
			//得到ip与端口
//			ip=ta.getText();
//			pour=Integer.parseInt(tb.getText());
//			
//			Paint.getIP(ip, pour);
			
			
		}
		//save game..................................
		if(e.getActionCommand().equals("save"))
		{
			Recorder.setEts(window.enemys);
			Recorder.SaveGame();
			System.exit(0);
		}
		//load game..................................
		if(e.getActionCommand().equals("load"))
		{
            this.remove(stp);
			
			window=new Draw("LoadGame");
			// Start the window 线程。
			Thread t =new Thread(window);
			t.start();
			this.add(window);
			this.addKeyListener(window);
			
			this.repaint();
		}
		
		//exit game..................................
		if(e.getActionCommand().equals("exit"))
		{
			Recorder.SaveAllBang();
			
			if(Paint.exit)
			{
				windownet.DisConnect();
				
			}
			
			System.exit(0);
		}
		
		if(e.getActionCommand().equals("netwindow"))
		{
			//传输ip
			ip=ta.getText();
			pour=Integer.parseInt(tb.getText());			
			Paint.getIP(ip, pour);
			
			input.setVisible(false);
			
          this.remove(stp);

			
			windownet=new Paint("netgame");

			
			// Start the window 线程。
			Thread t =new Thread(windownet);
			t.start();
			this.add(windownet);
			this.addKeyListener(windownet);
			
			this.repaint();
		}
		
		if(e.getActionCommand().equals("haha"))
		{
			JFrame frame=new JFrame("哈哈哈~");
		    JPanel panel=new JPanel();
		    panel.setPreferredSize(new Dimension(200,150));
		    JLabel label=new JLabel("哈哈~被我骗了吧~贪心的家伙~");
		    panel.add(label);
	    	frame.add(panel);
		
	    	frame.setVisible(true);
	    	frame.pack();
		}
		
//		if(e.getActionCommand().equals("again"))
//		{
//
//				this.remove(windownet);
//				
//	            windownet=new Paint("netgame");
//
//				
//				// Start the window 线程。
//				Thread t =new Thread(windownet);
//				t.start();
//				this.add(windownet);
//				this.addKeyListener(windownet);
//				
//				this.repaint();
//				
//			
//			
//		}
		
		
	}
//	public class AgainGame extends JPanel
//	{
//		public AgainGame(){
//			
//		}
//	}
	//again button
	public void AgainGame()
	{
//		String how=how1;
//		if(how.equals("two"))
//		{
//			this.remove(windownet);
//			
//            windownet=new Paint("netgame");
//
//			
//			// Start the window 线程。
//			Thread t =new Thread(windownet);
//			t.start();
//			this.add(windownet);
//			this.addKeyListener(windownet);
//			
//			this.repaint();
//			
//		}
//		if(how.equals("one"))
//		{
//			this.remove(window);
//			
//            window=new Draw("NewGame");
//			
//			// Start the window 线程。
//			Thread t =new Thread(window);
//			t.start();
//			this.add(window);
//			this.addKeyListener(window);
//			
//			this.repaint();
//		}
		
		
		
	}

}

// Start panel-----------------------------------------------------------------------------------------
class StartPanel extends JPanel implements Runnable
{
	int time=0;
	
		   public void paint(Graphics g)
	   {
		   Color c1=new Color(117,36,35);
//		   Color c2=new Color(78,84,82);
		   Color c2=new Color(128,134,132);
		   
		   super.paint(g);
		   g.setColor(c1);
		   g.fillRect(0, 0, 700, 400);
		   // 浮动字幕
		   if(time%2==0)
			{
		       g.setColor(c2);
		       Font f=new Font("汉仪丫丫体简",Font.BOLD,35);
		       g.setFont(f);
		       g.drawString("Let's Start", 200, 100);
	        }
	   }
	
	public void run() {
	
		while(true)
		{
			try {
				
				Thread.sleep(300);
				
			} catch (Exception e) {

				e.printStackTrace();
			}
			finally
			{
				this.repaint();
			}
			time++;
		}
		
	}
}
// win Panel------------------------------------------------------------------------------------------
//class WinPanel extends JPanel
//{
//	public void paint(Graphics g)
//	{
//		Color c1=new Color(117,36,35);
//	    Color c2=new Color(78,84,82);
//	    		
//		super.paint(g);
//		g.setColor(c1);
//		g.fillRect(0, 0, 700, 400);
//		   
//	    g.setColor(c2);
//	    Font f=new Font("汉仪丫丫体简",Font.BOLD,35);
//	    g.setFont(f);
//	    g.drawString("You Win !", 200, 100);
//	}
//}
//Lose Panel-------------------------------------------------------------------------------------------
//class LosePanel extends JPanel
//{
//	public void paint(Graphics g)
//	{
//		Color c1=new Color(117,36,35);
//	    Color c2=new Color(78,84,82);
//	    		
//		super.paint(g);
//		g.setColor(c1);
//		g.fillRect(0, 0, 700, 400);
//		   
//	    g.setColor(c2);
//	    Font f=new Font("汉仪丫丫体简",Font.BOLD,35);
//	    g.setFont(f);
//	    g.drawString("You not weak.\nBut he is too powerful. ", 200, 100);
//	}
//}






















