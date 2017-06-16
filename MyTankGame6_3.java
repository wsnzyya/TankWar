/*
 *����:̹�˴�ս6_3��
 *1.�ӵ�������������෢5��
 *2.�ҷ�̹�˻��е���̹��ʱ������̹����ʧ,��������ըЧ��(˲���ͼƬ�滻)
 *3.�ҷ�̹���е�ʱ��ʧ
 *4:���Ƶз�̹�˲����ص�
 *5.��ͣ��Ϸ�������˳�,�ٴ���Ϸʱ������Ϸ
 *6.���Էֹ�,��һ����ʼ��Panel
 *7.���Լ�¼��ҵĳɼ�
 *8.�˳���Ϸʱ�������ݣ��ٴο�ʼʱ������Ϸ
 *9.java��β��������ļ�
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
//������ʾ
class Gui extends  JFrame implements ActionListener
{
//	MyPanel mp=new MyPanel();
	MyStartPanel mpl=new MyStartPanel();
	//�����߳�
	Thread t1=new Thread(mpl);	
	//��һ���˵�
	//�����������
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
		//��ʼ����������
		jmb=new JMenuBar();
		jm1=new JMenu("��Ϸ����");
		jmi1=new JMenuItem("��ʼ����Ϸ");
		jmi2=new JMenuItem("������Ϸ");
		jmi4=new JMenuItem("�˳���Ϸ");
		//ע�����
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

		//������ʾ����
		this.setSize(520,420);
		this.setLocation(250, 100);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//������Ϸ֮ǰ���Ƴ���Ϸ��ʼ����
		
		//������Ϸ
		if(e.getActionCommand().equals("start"))
		{
			MyPanel mp=new MyPanel();
			Thread t=new Thread(mp);
			this.remove(mpl);
			t.start();
			//ע�����
			this.addKeyListener(mp);
			this.add(mp);
			//ע�������Ƴ�startPanel��Ҫ������ʾһ�½��棬���򿴲���
			this.setVisible(true);
		}else if(e.getActionCommand().equals("end"))
		{
			Recorder.record();	
			System.exit(0);
		}else if(e.getActionCommand().equals("continue"))
		{
            //���ָ������ݱ��浽Nodes��
			Recorder.nodeGetInfo();
			MyPanel.flag=1;//Ӧ�÷���newǰ�棬һ��new�Ļ������캯������ִ��
            MyPanel mp=new MyPanel();
            
			Thread t=new Thread(mp);
			this.remove(mpl);
			t.start();
			//ע�����
			this.addKeyListener(mp);
			this.add(mp);
			//ע�������Ƴ�startPanel��Ҫ������ʾһ�½��棬���򿴲���
			this.setVisible(true);
		}
	}
}
//��Ϸ��ʼ���
class MyStartPanel extends JPanel implements Runnable
{
	int times=0;
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.fill3DRect(0,0,400,300,false);
		g.setColor(Color.blue);
		g.setFont(new Font("��������",Font.BOLD,30));
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
//�ҵ����
class MyPanel extends JPanel implements KeyListener,Runnable
{
	static int flag=0;
	//����һ���ҵ�̹��
	Hero hero=null;
	//������˵�̹��
	Vector<EnemyTank> ets=new Vector<EnemyTank>();
	int enSize=6;
	//׼��������ͼƬ
	Image image1,image2,image3;
	//����һ��Bomb����
	Vector<Bomb> bombs=new Vector<Bomb>();
	//���캯��
	public MyPanel()
	{
		hero=new Hero(160,230);
		if(flag==0)
		{
			//��ʼ�����˵�̹��
			for(int i=0;i<enSize;i++)
			{
				//����һ�����˵�̹��
				EnemyTank et=new EnemyTank((i+1)*60,0);
				//���õ���̹����ɫ
				et.setColor(0);
				et.setDirect(2);
				//��������̹���߳�
				Thread t=new Thread(et);
				t.start();
				//����Vector��
				ets.add(et);
				//�����еĵз�̹����ӵ�EnemyTank��Vector��
				et.getAllEnemyTank(ets);	
			}
		}else if(flag==1)
		{
			System.out.println("�з�̹�˵�������"+Recorder.getEnNum());
			//�ûָ�����Ϣ��ʼ���з�̹��
			for(int i=0;i<Recorder.getEnNum();i++)
			{
				Node node=Recorder.Nodes.get(i);
				EnemyTank et=new EnemyTank(node.x,node.y);
				et.setDirect(node.direct);
				//���õ���̹����ɫ
				et.setColor(0);
				//��������̹���߳�
				Thread t=new Thread(et);
				t.start();
				//����Vector��
				ets.add(et);
				//�����еĵз�̹����ӵ�EnemyTank��Vector��
				et.getAllEnemyTank(ets);
			}
		}
		//�����е���̹�����ݴ��͵�Reader��
		Recorder.getEnInfo(ets);
		//��ʼ��ͼƬ�����ַ���
		try {
			image1=ImageIO.read(new File("5.gif"));
			image2=ImageIO.read(new File("5.gif"));
			image3=ImageIO.read(new File("5.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//�ڶ��ַ�������?
//		image1=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("5.gif"));
//		image2=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("5.gif"));
//		image3=Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("5.gif"));
	}
	//����Paint������д
	public void paint (Graphics g)
	{
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		//�����Լ���̹��
		if(Recorder.getMyLife()!=0)
		{
			if(hero.live)
			{
				this.drawTank(hero.getX(),hero.getY(),g,hero.direct,1);
			}
			else if(!hero.live)
			{
				//����ʱ���������ҷ�̹�˲���
				hero.live=true;
				hero.setX(160);
				hero.setY(230);
				hero.setDirect(0);
				this.drawTank(hero.getX(),hero.getY(),g,hero.direct,1);
			}
		}
	   //����hero���ӵ�
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
	  //����ը��
	    for(int i=0;i<bombs.size();i++)
	    {
	    	//System.out.println("��ӵ�Bombs="+bombs.size());
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
	    	//��b������ֵ��С
	    	b.lifeDown();
	    	//System.out.println(b.life);
	    	//�ж�ը���Ƿ�����
	    	if(b.life==0)
	    	{
	    		bombs.remove(b);
	    	}
	    }
	    //�������˵�̹�˺��ӵ�
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
	  //������Ϸ��ʼ����
	    
	  this.setMyStartPanel(g);   
	}
	//��Ϸ��ʼ��������
	public void setMyStartPanel(Graphics g)
	{
		//�����з�̹��
		this.drawTank(80, 320, g, 0, 0);
		g.setColor(Color.black);
		g.setFont(new Font("��������",Font.BOLD,30));
		g.drawString(Recorder.getEnNum()+"",110, 346);
		//�����ҷ�̹�˼�����
		this.drawTank(220,320,g,0,1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyLife()+"", 250, 346);
		//�����ҵ�ս��
		this.drawTank(415,84,g,0,1);
		g.setColor(Color.black);
		g.drawString(Recorder.getMyScore()+"", 445, 109);
	}
	//ȡ���з�̹�˺��ҷ��ӵ������û���̹�˺���
	public void hitEnemyTank()
	{
		
		for(int i=0;i<hero.ss.size();i++)
		{   
			//ȡ���ӵ�
			Shot MyShot=hero.ss.get(i);
			if(MyShot.live)
			{
				for(int j=0;j<this.ets.size();j++)
				{
					//ȡ��̹��
					EnemyTank et=this.ets.get(j);
					if(et.live)
					{
						this.hitTank(MyShot, et);
						//�жϵз�̹���Ƿ�����
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
	//ȡ���ҷ�̹�˺͵з��ӵ�������hitHero����
	public void hitHero()
	{
		
		for(int i=0;i<ets.size();i++)
		{
			//ȡ���з�̹��
			EnemyTank et=ets.get(i);
			if(et.live)
			{
				//ȡ���ӵ�
				for(int j=0;j<et.ss.size();j++)
				{
					Shot enemyShot=et.ss.get(j);
					if(enemyShot.live&&hero.live)//hero.live�������ҷ�̹��������������������ըЧ��
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
	//ר��дһ�����ез�̹�˵ĺ���,ע��Ӧ�õ��˶�̬��֪ʶ,�����˴�����ظ���
	public void hitTank(Shot s,Tank t)
	{
		switch(t.direct)
		{
		case 0:
		case 2:
		   if(s.x>t.x&&s.x<t.x+20&&s.y>t.y&&s.y<t.y+30)
		   {
			   //�ӵ�����,̹������
			   s.live=false;
			   t.live=false;
			 //��ʼ��ը����,����ӵ�������
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
				//��ʼ��ը����,����ӵ�������
				Bomb b=new Bomb(t.x,t.y);
				bombs.add(b);
			}
			
			break;
		}
	}
	//����̹�˵ĺ���(��չ)
	public void drawTank(int x,int y,Graphics g,int direct,int type)
	{
		//�ж����������͵�̹��
		switch(type)
		{
		case 0:
			g.setColor(Color.CYAN);
			break;
		case 1:
			g.setColor(Color.YELLOW);
			break;
		}
		//̹�˵ķ���
		switch(direct)
		{
		//̹������
		case 0:
			g.fill3DRect(x, y, 5, 30,false);
		    g.fill3DRect(x+15, y, 5,30,false);
		    g.fill3DRect(x+5, y+5, 10, 20,false);
			g.fillOval(x+4, y+10, 10, 10);
		    g.drawLine(x+10, y+15, x+10, y);
		    break;
		    //̹������
		case 1:
			g.fill3DRect(x, y, 30, 5,false);
			g.fill3DRect(x, y+15, 30, 5,false);
			g.fill3DRect(x+5, y+5, 20, 10,false);
			g.fillOval(x+10, y+5, 10, 10);
		    g.drawLine(x+15, y+10, x+30, y+10);
		    break;
		case 2:
			//����
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
	//�¼�������
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override//����������a��d��w��s
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==KeyEvent.VK_S)
		{
			//�����ҵ�̹�˵ķ���
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
		//�ж�����Ƿ�J
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
			//����,�жϵз�̹���Ƿ��е�
			this.hitEnemyTank();
			//�������ж��ҷ�̹���Ƿ��е�
				this.hitHero();
			//�ػ�
			this.repaint();	
		}	
	}
}