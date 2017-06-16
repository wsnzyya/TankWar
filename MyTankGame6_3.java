/*
 *功能:坦克大战6_3版
 *1.子弹可以连发且最多发5颗
 *2.我方坦克击中敌人坦克时，敌人坦克消失,并产生爆炸效果(瞬间的图片替换)
 *3.我方坦克中弹时消失
 *4:控制敌方坦克不会重叠
 *5.暂停游戏并存盘退出,再次游戏时继续游戏
 *6.可以分关,做一个开始的Panel
 *7.可以记录玩家的成绩
 *8.退出游戏时保存数据，再次开始时继续游戏
 *9.java如何操作声音文件
 */
package Tank6_3;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;
import javax.swing.*;
import java.util.*;
public class MyTankGame6_3 {

	/**
	 * @param args
	 */
     public static void main(String[] args) {
		// TODO Auto-generated method stub

		Gui gui=new Gui();
	}

}
//界面显示
class Gui extends  JFrame implements ActionListener
{
//	MyPanel mp=new MyPanel();
	MyStartPanel mpl=new MyStartPanel();
	//启动线程
	Thread t1=new Thread(mpl);	
	//加一个菜单
	//定义所需组件
	JMenuBar jmb=null;
	JMenu jm1=null;
	JMenu jm2=null;
	JMenu jm3=null;
	
	JMenuItem jmi1=null;
	JMenuItem jmi2=null;
	JMenuItem jmi3=null;
	JMenuItem jmi4=null;
	
	public Gui()
	{
		//初始化并添加组件
		jmb=new JMenuBar();
		jm1=new JMenu("游戏设置");
		jmi1=new JMenuItem("开始新游戏");
		jmi2=new JMenuItem("继续游戏");
		jmi4=new JMenuItem("退出游戏");
		//注册监听
		jmi1.addActionListener(this);
		jmi1.setActionCommand("start");
		jmi2.addActionListener(this);
		jmi2.setActionCommand("continue");
		jmi4.addActionListener(this);
		jmi4.setActionCommand("end");
		
		jm1.add(jmi1);
		jm1.add(jmi2);
		jm1.add(jmi4);
		jmb.add(jm1);
		
		this.setJMenuBar(jmb);
		
		this.add(mpl);
		t1.start();

		//界面显示设置
		this.setSize(520,420);
		this.setLocation(250, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//启动游戏之前先移除游戏开始界面
		
		//启动游戏
		if(e.getActionCommand().equals("start"))
		{
			MyPanel mp=new MyPanel();
			Thread t=new Thread(mp);
			this.remove(mpl);
			t.start();
			//注册监听
			this.addKeyListener(mp);
			this.add(mp);
			//注意这里移除startPanel后要重新显示一下界面，否则看不到
			this.setVisible(true);
		}else if(e.getActionCommand().equals("end"))
		{
			Recorder.record();	
			System.exit(0);
		}else if(e.getActionCommand().equals("continue"))
		{
            //将恢复的数据保存到Nodes中
			Recorder.nodeGetInfo();
			MyPanel.flag=1;//应该放在new前面，一旦new的话，构造函数立马执行
            MyPanel mp=new MyPanel();
            
			Thread t=new Thread(mp);
			this.remove(mpl);
			t.start();
			//注册监听
			this.addKeyListener(mp);
			this.add(mp);
			//注意这里移除startPanel后要重新显示一下界面，否则看不到
			this.setVisible(true);
		}
	}
}
//游戏开始面板
class MyStartPanel extends JPanel implements Runnable
{
	int times=0;
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fill3DRect(0,0,400,300,false);
		g.setColor(Color.blue);
		g.setFont(new Font("华文中宋",Font.BOLD,30));
		if(times%2==0)
		{
			g.drawString("stage 1", 150, 150);
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.repaint();
			times++;
		}
	}
}
//我的面板
class MyPanel extends JPanel implements KeyListener,Runnable
{
	static int flag=0;
	//定义一个我的坦克
	Hero hero=null;
	//定义敌人的坦克
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	int enSize=6;
	//准备好三张图片
	Image image1,image2,image3;
	//定义一个Bomb集合
	Vector<Bomb> bombs=new Vector<Bomb>();
	//构造函数
	public MyPanel()
	{
		hero=new Hero(160,230);
		if(flag==0)
		{
			//初始化敌人的坦克
			for(int i=0;i<enSize;i++)
			{
				//创建一辆敌人的坦克
				EnemyTank et=new EnemyTank((i+1)*60,0);
				//设置敌人坦克颜色
				et.setColor(0);
				et.setDirect(2);
				//启动敌人坦克线程
				Thread t=new Thread(et);
				t.start();
				//加入Vector中
				ets.add(et);
				//将所有的敌方坦克添加到EnemyTank的Vector中
				et.getAllEnemyTank(ets);	
			}
		}else if(flag==1)
		{
			System.out.println("敌方坦克的数量是"+Recorder.getEnNum());
			//用恢复的信息初始化敌方坦克
			for(int i=0;i<Recorder.getEnNum();i++)
			{
				Node node=Recorder.Nodes.get(i);
				EnemyTank et=new EnemyTank(node.x,node.y);
				et.setDirect(node.direct);
				//设置敌人坦克颜色
				et.setColor(0);
				//启动敌人坦克线程
				Thread t=new Thread(et);
				t.start();
				//加入Vector中
				ets.add(et);
				//将所有的敌方坦克添加到EnemyTank的Vector中
				et.getAllEnemyTank(ets);
			}
		}
		//将所有敌人坦克数据传送到Reader中
		Recorder.getEnInfo(ets);
		//初始化图片的两种方法
		try {
			image1=ImageIO.read(new File("5.gif"));
			image2=ImageIO.read(new File("5.gif"));
			image3=ImageIO.read(new File("5.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//第二种方法报错?
//		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("5.gif"));
//		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("5.gif"));
//		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("5.gif"));
	}
	//父类Paint函数重写
	public void paint (Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		//画出自己的坦克
		if(Recorder.getMyLife()!=0)
		{
			if(hero.live)
			{
				this.drawTank(hero.getX(),hero.getY(),g,hero.direct,1);
			}
			else if(!hero.live)
			{
				//复活时重新设置我方坦克参数
				hero.live=true;
				hero.setX(160);
				hero.setY(230);
				hero.setDirect(0);
				this.drawTank(hero.getX(),hero.getY(),g,hero.direct,1);
			}
		}
	   //画出hero的子弹
	    for(int i=0;i<hero.ss.size();i++)
	    {
	    	Shot MyShot=hero.ss.get(i);
	    	if(MyShot!=null&&MyShot.live);
		    {
		    	g.draw3DRect(MyShot.x,MyShot.y,2,2,false);
		    }
		    if(!MyShot.live)
		    {
		    	hero.ss.remove(MyShot);
		    }
	    }
	  //绘制炸弹
	    for(int i=0;i<bombs.size();i++)
	    {
	    	//System.out.println("添加的Bombs="+bombs.size());
	    	Bomb b=bombs.get(i);
	    	if(b.life>6)
	    	{
	    		g.drawImage(image1,b.x,b.y,30,30,this);
	    	}else if(b.life>3)
	    	{
	    		g.drawImage(image2,b.x,b.y,20,20,this);
	    	}else
	    	{
	    		g.drawImage(image3,b.x,b.y,10,10,this);
	    	}
	    	//让b的生命值减小
	    	b.lifeDown();
	    	//System.out.println(b.life);
	    	//判断炸弹是否死亡
	    	if(b.life==0)
	    	{
	    		bombs.remove(b);
	    	}
	    }
	    //画出敌人的坦克和子弹
	    for(int i=0;i<ets.size();i++)
	    {
	    	EnemyTank et=ets.get(i);
	    	if(et.live)
	    	{
	    		this.drawTank(et.getX(), et.getY(),g,et.getDirect(), 0);
	    	    for(int j=0;j<et.ss.size();j++)
	    	    {
	    	    	if(et.ss.get(j).live)
	    	    	{
	    	    		g.draw3DRect(et.ss.get(j).x,et.ss.get(j).y,2,2,false);
	    	    	}
	    	    	else{
	    	    		et.ss.remove(et.ss.get(j));
	    	    		//System.out.println(et.ss.size());
	    	    	}
	    	    }
	    	}
	    }
	  //画出游戏初始界面
	    
	  this.setMyStartPanel(g);   
	}
	//游戏开始界面设置
	public void setMyStartPanel(Graphics g)
	{
		//画出敌方坦克
		this.drawTank(80, 320, g, 0, 0);
		g.setColor(Color.black);
		g.setFont(new Font("华文中宋",Font.BOLD,30));
		g.drawString(Recorder.getEnNum()+"",110, 346);
		//画出我方坦克及生命
		this.drawTank(220,320,g,0,1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"", 250, 346);
		//画出我的战绩
		this.drawTank(415,84,g,0,1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyScore()+"", 445, 109);
	}
	//取出敌方坦克和我方子弹并调用击中坦克函数
	public void hitEnemyTank()
	{
		
		for(int i=0;i<hero.ss.size();i++)
		{   
			//取出子弹
			Shot MyShot=hero.ss.get(i);
			if(MyShot.live)
			{
				for(int j=0;j<this.ets.size();j++)
				{
					//取出坦克
					EnemyTank et=this.ets.get(j);
					if(et.live)
					{
						this.hitTank(MyShot, et);
						//判断敌方坦克是否死亡
						if(!et.live)
						{
							ets.remove(et);
							Recorder.reduce();
							Recorder.increase();
						}
					}
				}
			}
		}
	}
	//取出我方坦克和敌方子弹并调用hitHero函数
	public void hitHero()
	{
		
		for(int i=0;i<ets.size();i++)
		{
			//取出敌方坦克
			EnemyTank et=ets.get(i);
			if(et.live)
			{
				//取出子弹
				for(int j=0;j<et.ss.size();j++)
				{
					Shot enemyShot=et.ss.get(j);
					if(enemyShot.live&&hero.live)//hero.live是在在我方坦克死亡后避免继续产生爆炸效果
					{
						this.hitTank(enemyShot, hero);
						if(hero.live==false)
						{
							Recorder.lifetap();
						}
					}
				}
			}
		}
	}
	//专门写一个击中敌方坦克的函数,注意应用到了多态的知识,减少了代码的重复率
	public void hitTank(Shot s,Tank t)
	{
		switch(t.direct)
		{
		case 0:
		case 2:
		   if(s.x>t.x&&s.x<t.x+20&&s.y>t.y&&s.y<t.y+30)
		   {
			   //子弹死亡,坦克死亡
			   s.live=false;
			   t.live=false;
			 //初始化炸弹类,并添加到集合中
			   Bomb b=new Bomb(t.x,t.y);
			   bombs.add(b);
		   }
		   		   break;
		case 1:
		case 3:
			if(s.x>t.x&&s.x<t.x+30&&s.y>t.y&&s.y<t.y+20)
			{
				s.live=false;
				t.live=false;
				//初始化炸弹类,并添加到集合中
				Bomb b=new Bomb(t.x,t.y);
				bombs.add(b);
			}
			
			break;
		}
	}
	//画出坦克的函数(扩展)
	public void drawTank(int x,int y,Graphics g,int direct,int type)
	{
		//判断是哪种类型的坦克
		switch(type)
		{
		case 0:
			g.setColor(Color.CYAN);
			break;
		case 1:
			g.setColor(Color.YELLOW);
			break;
		}
		//坦克的方向
		switch(direct)
		{
		//坦克向上
		case 0:
			g.fill3DRect(x, y, 5, 30,false);
		    g.fill3DRect(x+15, y, 5,30,false);
		    g.fill3DRect(x+5, y+5, 10, 20,false);
			g.fillOval(x+4, y+10, 10, 10);
		    g.drawLine(x+10, y+15, x+10, y);
		    break;
		    //坦克向右
		case 1:
			g.fill3DRect(x, y, 30, 5,false);
			g.fill3DRect(x, y+15, 30, 5,false);
			g.fill3DRect(x+5, y+5, 20, 10,false);
			g.fillOval(x+10, y+5, 10, 10);
		    g.drawLine(x+15, y+10, x+30, y+10);
		    break;
		case 2:
			//向下
			g.fill3DRect(x, y, 5, 30,false);
		    g.fill3DRect(x+15, y, 5,30,false);
		    g.fill3DRect(x+5, y+5, 10, 20,false);
			g.fillOval(x+4, y+10, 10, 10);
		    g.drawLine(x+10, y+15, x+10, y+30);
		    break;
		case 3:
			g.fill3DRect(x, y, 30, 5,false);
			g.fill3DRect(x, y+15, 30, 5,false);
			g.fill3DRect(x+5, y+5, 20, 10,false);
			g.fillOval(x+10, y+5, 10, 10);
		    g.drawLine(x+15, y+10, x, y+10);
		    break;
		}
	}
	//事件处理方法
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override//按键处理左a右d上w下s
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_S)
		{
			//设置我的坦克的方向
			this.hero.setDirect(2);
			this.hero.moveDown();
		}
		else if(e.getKeyCode()==KeyEvent.VK_W)
		{
			this.hero.setDirect(0);
			this.hero.moveUp();
		}
		else if(e.getKeyCode()==KeyEvent.VK_A)
		{
			this.hero.setDirect(3);
			this.hero.moveLeft();
		}	
		else if(e.getKeyCode()==KeyEvent.VK_D)
		{
			this.hero.setDirect(1);
			this.hero.moveRight();
		}
		//判断玩家是否按J
		if(e.getKeyCode()==KeyEvent.VK_J)
		{
			if(this.hero.ss.size()<6)
			{
				this.hero.shotEnemy();
			}
		}
		this.repaint();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//函数,判断敌方坦克是否中弹
			this.hitEnemyTank();
			//函数，判断我方坦克是否中弹
				this.hitHero();
			//重绘
			this.repaint();	
		}	
	}
}