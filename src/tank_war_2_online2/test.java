package tank_war_2_online2;

import java.util.Vector;

import org.omg.CORBA.PUBLIC_MEMBER;

public class test {
	
	public static void main(String[]args) 
	{
		Vector <Integer> a=new Vector<Integer>(); 
		
		int b=1;
		
		a.add(b);
		
//		int d1=a.get(0);
    	int d2=a.size();
    	
		System.out.println(a.get(0)+" "+d2);	
		int a3=Integer.parseInt("-3");
		System.out.println(a3);
		String string="3+4-2";
		String[]strings=string.split("\\+");
		
		String temp="";
		String a4="hh";
		String b4="gg";
		temp=b4;
		b4=a4;
		a4=temp;

		System.out.println(temp+" "+ a4+" "+b4);
		
		
		int a7[][]=new int[2][8];
		
		System.out.println(a7.length+"");
		System.out.println(a7[1].length+"");
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
