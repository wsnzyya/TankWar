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
//记录
class Recorder
{
	//获取敌方坦克此时的信息
	static Vector<EnemyTank> ets=new Vector<EnemyTank>();
	static Vector<Node> Nodes=new Vector<Node>();
	//敌人坦克总数
	private static int enNum=6;
	//我的生命值
	private static int myLife=3;
	//我的战绩
	private static int myScore=0;
	//恢复数据
	public static void nodeGetInfo()
	{
		FileReader fr=null;
		BufferedReader br=null;
		try {
			fr=new FileReader("G:\\记录.txt");
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
			fw=new FileWriter("G:\\记录.txt");
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
	//获取所有敌方坦克的引用
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
	//减少敌人坦克
	public static void reduce()
	{
		enNum--;
	}
	//战绩提升
	public static void increase()
	{
		myScore++;
	}
	//生命减少
	public static void lifetap()
	{
		myLife--;
	}
}
//炸弹类
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
//子弹类
class Shot implements Runnable
{
	int x,y;
	int direct;//子弹的方向
	int speed=2;//子弹的速度
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
				y-=speed;//上
				break;
			case 1:
				x+=speed;
				break;//右
			case 2:
				y+=speed;
				break;//下
			case 3:
				x-=speed;
				break;//左
			}
			if(x<0||x>400||y<0||y>300)
			{
				this.live=false;
				break;
			}
			//System.out.println("子弹的坐标是 x="+x+" ,y="+y);
		}
	}
}
//敌人的坦克
class EnemyTank extends Tank implements Runnable
{
	//定义敌方坦克子弹的集合
	Vector<Shot> ss=new Vector<Shot>();
	Shot s=null;
	//定义所有敌方坦克的集合
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	//构造函数
	public EnemyTank(int x, int y)
	{
		super(x, y);
	}
	//从MyPanel上获取所有的敌方坦克
		public void getAllEnemyTank(Vector<EnemyTank> vv)
		{
			/*
			 * 很重要:再次深入理解java中引用类型的理解
			 * 深刻理解这里并不是获得了坦克的初始化数据,而是获得了所有坦克的引用,用引用可以
			 * 访问到所有坦克的实时数据
			 */
			this.ets=vv;
		}
	//设置敌方坦克不能重叠
	public boolean isOverlap()
	{
		boolean b=false;
		//避免敌方坦克重叠算法
		for(int i=0;i<ets.size();i++)
		{
			EnemyTank et=ets.get(i);
			switch(this.direct)
			{
			case 0://开口向上
				if(et!=this)
				{
					if(et.direct==0||et.direct==2)
					{
						//判断左边的点是否进入到敌方坦克范围
						if(this.x>=et.x&&this.x<=et.x+20&&this.y>=et.y&&this.y<=et.y+30)
						{
							return true;
						}
						//判断右边的点是否进入到敌方坦克范围
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
			case 1://开口向右
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
			case 2://开口向下
				if(et!=this)
				{
					if(et.direct==0||et.direct==2)
					{
						//判断左边的点是否进入到敌方坦克范围
						if(this.x>=et.x&&this.x<=et.x+20&&this.y+30>=et.y&&this.y+30<=et.y+30)
						{
							return true;
						}
						//判断右边的点是否进入到敌方坦克范围
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
			case 3://开口向左
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
			
			//添加敌方坦克的子弹，坦克走1.5秒发射一颗子弹
			switch(this.direct)
			{
			case 0:
				s=new Shot(x+10,y,0);//记住s初始化的地方
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
			//启动敌方子弹线程
			Thread t=new Thread(s);
			t.start();
		      //产生一个随机的方向
			this.direct=(int)(Math.random()*4);
			
			if(this.live==false)
			{
				break;
			}
			
		}
	}
	
}
//我的坦克
class Hero extends Tank
{
	//子弹集
	Vector<Shot> ss=new Vector<Shot>();
	public Hero(int x,int y)
	{
		super(x,y);	
	}
	//发射子弹
	public void shotEnemy()
	{
		Shot s=null;
		switch(this.direct)
		{
		case 0:
			s=new Shot(x+10,y,0);//记住s初始化的地方
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
		//启动线程
		//此处的t就是一个引用
		Thread t=new Thread(s);
		t.start();
		//System.out.println(Thread.currentThread().getName());
	}
	//坦克向上移动
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
//坦克类,抽象出的父类
class Tank
{
	//表示坦克的横坐标
	int x=0;
	//坦克纵坐标
	int y=0;
	//坦克方向
	//0表示上,1表示右,2表示下,3表示左
	int direct=0;
	//坦克速度
	int speed=4;
	//坦克颜色
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