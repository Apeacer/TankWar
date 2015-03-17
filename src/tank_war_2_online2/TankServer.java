package tank_war_2_online2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

//ServerSocket------------------------------------------------------------------------------------------------------
class TankServer {
	boolean started = false;
	ServerSocket ser = null;
	
	Vector<Client> clients = new Vector<Client>();
	
	Vector<String> strs=new Vector<String>();
	
	int id=2;
	
	//主运行方法。。。。。。。。。。
	public static void main(String[] args) {
		new TankServer().start();
	}
	
	//连接方法。。。。。。。。。。
	public void start() {
		try {
			ser = new ServerSocket(6008);
			started = true;
		} catch (BindException e) {
			System.out.println("端口被抢先了，早点来吧~");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//不停地链接客户端
		try {
			
			while(started) {
				
				Socket s = ser.accept();
				Client c = new Client(s);
				
				c.dos.writeInt(id);

				new Thread(c).start();
				clients.add(c);
				
				id++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ser.close();
			} catch (IOException e) {
	
				e.printStackTrace();
			}
		}
	}
	
	//将客户端视为对象，对其进行链接
	class Client implements Runnable {
		private Socket s;
		
		private DataInputStream dis = null;
		private DataOutputStream dos = null;
		private boolean bConnected = false;
		
		public Client(Socket s) {
			
			
			this.s = s;
			try {
				
				dis=new DataInputStream(s.getInputStream());
				dos=new DataOutputStream(s.getOutputStream());
				
				bConnected = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void send(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				clients.remove(this);
				System.out.println("人家讨厌你了，下线了~");
			}
		}
		
		public void run() {
			try {
				while(bConnected) {
						
						String str = dis.readUTF();

					
					for(int i=0; i<clients.size(); i++) {
						Client c = clients.get(i);

						    c.send(str);
					}
				}
			} catch (EOFException e) {
				System.out.println("Client closed!");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(dis != null) dis.close();
					if(dos != null) dos.close();
					if(s != null)  {
						s.close();
					}
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				
			}
		}
		
	}
}