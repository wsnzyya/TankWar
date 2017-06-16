package Tank6_3;

import java.io.*;
import java.util.*;
class Node
{
	int x, y, direct;
	public Node(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
}
//��¼
class Recorder
{
	//��ȡ�з�̹�˴�ʱ����Ϣ
	static Vector<EnemyTank> ets=new Vector<EnemyTank>();
	static Vector<Node> Nodes=new Vector<Node>();
	//����̹������
	private static int enNum=6;
	//�ҵ�����ֵ
	private static int myLife=3;
	//�ҵ�ս��
	private static int myScore=0;
	//�ָ�����
	public static void nodeGetInfo()
	{
		FileReader fr=null;
		BufferedReader br=null;
		try {
			fr=new FileReader("G:\\��¼.txt");
			br=new BufferedReader(fr);
			
			try {
				String n=br.readLine();
				enNum=Integer.parseInt(n);
				while((n=br.readLine())!=null)
				{
					String[] xyz=n.split(" ");
					Node node=new Node(Integer.parseInt(xyz[0]),Integer.parseInt(xyz[1]),Integer.parseInt(xyz[2]));
				    Nodes.add(node);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	public static void record()
	{
		FileWriter fw=null;
		BufferedWriter bw=null;
		try {
			fw=new FileWriter("G:\\��¼.txt");
			bw=new BufferedWriter(fw);
			bw.write(enNum+"\r\n");
			for(int i=0;i<enNum;i++)
			{
				EnemyTank et=ets.get(i);
				if(et.live)
				{
					String s=et.x+" "+et.y+" "+et.direct+"\r\n";
					bw.write(s);
				}	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	//��ȡ���ез�̹�˵�����
	public static void getEnInfo(Vector<EnemyTank> v2)
	{
		ets=v2;
	}
	public static int getEnNum() {
		return enNum;
	}
	public static void setEnNum(int enNum) {
		Recorder.enNum = enNum;
	}
	public static int getMyLife() {
		return myLife;
	}
	public static void setMyLife(int myLife) {
		Recorder.myLife = myLife;
	}
	public static int getMyScore() {
		return myScore;
	}
	public static void setMyScore(int myScore) {
		Recorder.myScore = myScore;
	}
	//���ٵ���̹��
	public static void reduce()
	{
		enNum--;
	}
	//ս������
	public static void increase()
	{
		myScore++;
	}
	//��������
	public static void lifetap()
	{
		myLife--;
	}
}
//ը����
class Bomb
{
	int x;
	int y;
	int life=9;
	boolean live=true;
	public Bomb(int x,int y)
	{
		this.x=x;
		this.y=y;
	}
	public void lifeDown()
	{
		if(life>0)
		{
			life--;
		}
		else
		{
			this.live=false;
		}
	}
}
//�ӵ���
class Shot implements Runnable
{
	int x,y;
	int direct;//�ӵ��ķ���
	int speed=2;//�ӵ����ٶ�
	boolean live=true;
	public Shot(int x,int y,int direct)
	{
		this.x=x;
		this.y=y;
		this.direct=direct;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			switch(direct)
			{
			case 0:
				y-=speed;//��
				break;
			case 1:
				x+=speed;
				break;//��
			case 2:
				y+=speed;
				break;//��
			case 3:
				x-=speed;
				break;//��
			}
			if(x<0||x>400||y<0||y>300)
			{
				this.live=false;
				break;
			}
			//System.out.println("�ӵ��������� x="+x+" ,y="+y);
		}
	}
}
//���˵�̹��
class EnemyTank extends Tank implements Runnable
{
	//����з�̹���ӵ��ļ���
	Vector<Shot> ss=new Vector<Shot>();
	Shot s=null;
	//�������ез�̹�˵ļ���
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	//���캯��
	public EnemyTank(int x, int y)
	{
		super(x, y);
	}
	//��MyPanel�ϻ�ȡ���еĵз�̹��
		public void getAllEnemyTank(Vector<EnemyTank> vv)
		{
			/*
			 * ����Ҫ:�ٴ��������java���������͵����
			 * ���������ﲢ���ǻ����̹�˵ĳ�ʼ������,���ǻ��������̹�˵�����,�����ÿ���
			 * ���ʵ�����̹�˵�ʵʱ����
			 */
			this.ets=vv;
		}
	//���õз�̹�˲����ص�
	public boolean isOverlap()
	{
		boolean b=false;
		//����з�̹���ص��㷨
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			switch(this.direct)
			{
			case 0://��������
				if(et!=this)
				{
					if(et.direct==0||et.direct==2)
					{
						//�ж���ߵĵ��Ƿ���뵽�з�̹�˷�Χ
						if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return true;
						}
						//�ж��ұߵĵ��Ƿ���뵽�з�̹�˷�Χ
						if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return true;
						}
					}
					if(et.direct==1||et.direct==3)
					{
						if(this.x>=et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return true;
						}
						if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return true;
						}
					}
				}
				break;
			case 1://��������
				if(et!=this)
				{
					if(et.direct==0||et.direct==2)
					{
						if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return true;
						}
						if(this.x+30>=et.x&&this.x+30<=et.x+20&&this.y+20>=et.y&&this.y+20<=et.y+30)
						{
							return true;
						}
					}
					if(et.direct==1||et.direct==3)
					{
						if(this.x+30>=et.x&&this.x+30<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
						{
							return true;
						}
						if(this.x+30>=et.x&&this.x+30<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20)
						{
							return true;
						}
					}
				}
				break;
			case 2://��������
				if(et!=this)
				{
					if(et.direct==0||et.direct==2)
					{
						//�ж���ߵĵ��Ƿ���뵽�з�̹�˷�Χ
						if(this.x>=et.x&&this.x<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30)
						{
							return true;
						}
						//�ж��ұߵĵ��Ƿ���뵽�з�̹�˷�Χ
						if(this.x+20>=et.x&&this.x+20<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30)
						{
							return true;
						}
					}
					if(et.direct==1||et.direct==3)
					{
						if(this.x>=et.x&&this.x<=et.x+30&&this.y+30>=et.y&&this.y+30<=et.y+20)
						{
							return true;
						}
						if(this.x+20>=et.x&&this.x+20<=et.x+30&&this.y+30>=et.y&&this.y+30<=et.y+20)
						{
							return true;
						}
					}
				}
				break;
			case 3://��������
				if(et.direct==0||et.direct==2)
				{
					if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
					{
						return true;
					}
					if(this.x>=et.x&&this.x<=et.x+20&&this.y+20>=et.y&&this.y+20<=et.y+30)
					{
						return true;
					}
				}
				if(et.direct==1||et.direct==3)
				{
					if(this.x>=et.x&&this.x<=et.x+30&&this.y>=et.y&&this.y<=et.y+20)
					{
						return true;
					}
					if(this.x>=et.x&&this.x<=et.x+30&&this.y+20>=et.y&&this.y+20<=et.y+20)
					{
						return true;
					}
				}
				break;
			}
		}
		
		
		return b;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			switch(this.direct)
			{
			case 0:
				for(int i=1;i<30;i++)
				{
					if(y>0&&!this.isOverlap())
					{
						y-=speed;
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 1:
				for(int i=1;i<30;i++)
				{
					if(x<370&&!this.isOverlap())
					{
						x+=speed;
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case 2:
				for(int i=1;i<30;i++)
				{
					if(y<270&&!this.isOverlap())
					{
						y+=speed;
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			    break;
			case 3:
				for(int i=1;i<30;i++)
				{
					if(x>0&&!this.isOverlap())
					{
						x-=speed;
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			}
			
			//��ӵз�̹�˵��ӵ���̹����1.5�뷢��һ���ӵ�
			switch(this.direct)
			{
			case 0:
				s=new Shot(x+10,y,0);//��סs��ʼ���ĵط�
				ss.add(s);
				break;
			case 1:
				s=new Shot(x+30,y+10,1);
				ss.add(s);
				break;
			case 2:
				s=new Shot(x+10,y+30,2);
				ss.add(s);
				break;
			case 3:
				s=new Shot(x,y+10,3);
				ss.add(s);
				break;
			}
			ss.add(s);
			//�����з��ӵ��߳�
			Thread t=new Thread(s);
			t.start();
		      //����һ������ķ���
			this.direct=(int)(Math.random()*4);
			
			if(this.live==false)
			{
				break;
			}
			
		}
	}
	
}
//�ҵ�̹��
class Hero extends Tank
{
	//�ӵ���
	Vector<Shot> ss=new Vector<Shot>();
	public Hero(int x,int y)
	{
		super(x,y);	
	}
	//�����ӵ�
	public void shotEnemy()
	{
		Shot s=null;
		switch(this.direct)
		{
		case 0:
			s=new Shot(x+10,y,0);//��סs��ʼ���ĵط�
			ss.add(s);
			break;
		case 1:
			s=new Shot(x+30,y+10,1);
			ss.add(s);
			break;
		case 2:
			s=new Shot(x+10,y+30,2);
			ss.add(s);
			break;
		case 3:
			s=new Shot(x,y+10,3);
			ss.add(s);
			break;
		}
		//�����߳�
		//�˴���t����һ������
		Thread t=new Thread(s);
		t.start();
		//System.out.println(Thread.currentThread().getName());
	}
	//̹�������ƶ�
	public void moveUp()
	{
		if(y>0)
		{
			y-=speed;
		}	
	}
	public void moveDown()
	{
		if(y<270)
		{
			y+=speed;
		}	
	}
	public void moveLeft()
	{
		if(x>0)
		{
			x-=speed;
		}
	}
	public void moveRight()
	{
		if(x<370)
		{
			x+=speed;
		}	
	}
}
//̹����,������ĸ���
class Tank
{
	//��ʾ̹�˵ĺ�����
	int x=0;
	//̹��������
	int y=0;
	//̹�˷���
	//0��ʾ��,1��ʾ��,2��ʾ��,3��ʾ��
	int direct=0;
	//̹���ٶ�
	int speed=4;
	//̹����ɫ
	int color;
	boolean live=true;
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

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
}