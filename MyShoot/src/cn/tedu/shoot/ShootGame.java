package cn.tedu.shoot;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;

public class ShootGame extends JPanel{//主程序
	public static final int WIDTH = 400;
	public static final int HEIGHT = 654;
	public static BufferedImage background;
	public static BufferedImage start;
	public static BufferedImage pause;
	public static BufferedImage gameover;
	public static BufferedImage airplane;
	public static BufferedImage bee;
	public static BufferedImage bullet;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	
	static{	
		try{
			background = ImageIO.read(ShootGame.class.getResource("background.png"));//同包图片读取方法
			start = ImageIO.read(ShootGame.class.getResource("start.png"));
			pause = ImageIO.read(ShootGame.class.getResource("pause.png"));
			gameover = ImageIO.read(ShootGame.class.getResource("gameover.png"));
			airplane = ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bee = ImageIO.read(ShootGame.class.getResource("bee.png"));
			bullet = ImageIO.read(ShootGame.class.getResource("bullet.png"));
			hero0 = ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("hero1.png"));
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private Hero hero = new Hero();
	private FlyingObject[] flyings = {};
	private Bullet[] bullets = {};
	private static final int AIRPLANE_AND_BEE_NUMBER = 20;
	private static final int BEE_NUMBER = 5;
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAME_OVER = 3;
	private int state = START;
	
	public FlyingObject nextOne(){
		Random rand = new Random();
		int type = rand.nextInt(AIRPLANE_AND_BEE_NUMBER);
		if(type < BEE_NUMBER) {
			return new Bee();
		}else{
			return new Airplane();
		}	
	}
	
	private static final int TIMER_INTERVAL = 10;
	int flyingEnterdIndex = 0;
	private static final int FLYING_ENTER_INTERVAL = 40;
	public void enterAction(){
		flyingEnterdIndex++;
		if(flyingEnterdIndex % FLYING_ENTER_INTERVAL == 0){
			FlyingObject obj = nextOne();
			flyings = Arrays.copyOf(flyings,flyings.length+1);
			flyings[flyings.length - 1] = obj;
		}
	}
	
	public void stepAction(){
		hero.step();
		for(int i = 0;i < flyings.length;i++){
			flyings[i].step();
		}
		for(int i = 0;i < bullets.length;i++){
			bullets[i].step();
		}
	}
	
	int shootIndex = 0;
	private static final int SHOOT_INTERVAL = 20;
	public void shootAction(){
		shootIndex++;
		if(shootIndex % SHOOT_INTERVAL == 0){
			Bullet[] bs = hero.shoot();
			bullets = Arrays.copyOf(bullets,bullets.length + bs.length);
			System.arraycopy(bs,0,bullets,bullets.length - bs.length,bs.length);
		}
	}
	
	public void outOfBoundsAction(){
		int index = 0;
		FlyingObject[] flyingLives = new FlyingObject[flyings.length];
		for(int i = 0;i < flyings.length;i++){
			FlyingObject f = flyings[i];
			if(!f.outOfBounds()){
				flyingLives[index] = f;
				index++;
			}
		}
		flyings = Arrays.copyOf(flyingLives,index);
		index = 0;
		Bullet[] bulletLives = new Bullet[bullets.length];
		for(int i = 0;i < bullets.length;i++){
			Bullet b = bullets[i];
			if(!b.outOfBounds()){
				bulletLives[index] = b;
				index++;
			}
		}
		bullets = Arrays.copyOf(bulletLives,index);
	}
	
	public void bangAction(){
		for(int i = 0;i < bullets.length;i++){
			bang(bullets[i]);
		}
	}
	int score = 0;
	public void bang(Bullet b){
		int index = -1;
		for(int i = 0;i < flyings.length;i++){
			if(flyings[i].shootBy(b)){
				index = i;
				break;
			}
		}
		if(index != -1){
			FlyingObject one =  flyings[index];
			if(one instanceof Enemy){
				Enemy e = (Enemy)one;
				score += e.getScore();
			}
			if(one instanceof Award){
				Award a = (Award)one;
				int type = a.getType();
				switch(type){
				case Award.DOUBLE_FIRE:
					hero.addDoubleFire();
					break;
				case Award.LIFE:
					hero.addLife();
					break;
				}
			}
			FlyingObject t = flyings[index];
			flyings[index] = flyings[flyings.length - 1];
			flyings[flyings.length - 1] = t;
			flyings = Arrays.copyOf(flyings,flyings.length - 1);
		}
	}
	
	public void hitAction(){
		for(int i = 0;i < flyings.length;i++){
			if(hero.hit(flyings[i])){
				hero.subtractLife();
				hero.clearDoubleFire();
				FlyingObject t = flyings[i];
				flyings[i] = flyings[flyings.length - 1];
				flyings[flyings.length - 1] = t;
				flyings = Arrays.copyOf(flyings, flyings.length - 1);
			}
		}
	}
	
	public void checkGameOverAction(){
		if(hero.getLife() <= 0){
			state = GAME_OVER;
			
		}
	}
	
	public void action(){
		MouseAdapter l  = new MouseAdapter(){
			public void mouseMoved(MouseEvent e){
				if(state == RUNNING){
					hero.moveTo(e.getX(), e.getY());
				}
			}
			
			public void mouseDragged(MouseEvent e){
				mouseMoved(e);
			}
			
			public void mouseClicked(MouseEvent e){
				switch(state){
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					score = 0;
					hero = new Hero();
					flyings = new FlyingObject[0];
					bullets = new Bullet[0];
					state = START;
					break;
				}
			}
			
			public void mouseExited(MouseEvent e){
				if(state == RUNNING){
					state = PAUSE;
				}
			}
			
			public void mouseEntered(MouseEvent e){
				if(state == PAUSE){
					state = RUNNING;
				}
			}
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		
		Timer timer = new Timer();
		int interval = TIMER_INTERVAL;
		TimerTask timerTask = new TimerTask(){
			public void run(){
				if(state == RUNNING){
					enterAction();
					shootAction();
					stepAction();
					outOfBoundsAction();
					bangAction();
					hitAction();
					checkGameOverAction();			
				}
				repaint();
			}
		};
		timer.schedule(timerTask,interval,interval);	
	}
	
	
	public void paint(Graphics g){
		g.drawImage(background,0,0,null);
		paintFlyingObjects(g);
		paintBullets(g);
		paintHero(g);
		paintScoreAndLife(g);
		paintState(g);
	}
	
	public void paintHero(Graphics g){
		g.drawImage(hero.image,hero.x,hero.y,null);
	}
	
	public void paintFlyingObjects(Graphics g){
		for(int i = 0;i < flyings.length;i++){
			FlyingObject f = flyings[i];
			g.drawImage(f.image,f.x,f.y,null);
		}
	}
	
	public void paintBullets(Graphics g){
		for(int i =0;i < bullets.length;i++){
			Bullet b = bullets[i];
			g.drawImage(b.image,b.x,b.y,null);
		}
	}
	
	public void paintState(Graphics g){
		switch(state){
		case START:
			g.drawImage(start,0,0,null);
			break;
		case PAUSE:
			g.drawImage(pause,0,0,null);
			break;
		case GAME_OVER:
			g.drawImage(gameover,0,0,null);
			break;
			
		}
		
	}
	private static final int COLOR_RED = 0xFF0000;
	private static final int WORD_SIZE = 24;
	public void paintScoreAndLife(Graphics g){	
		g.setColor(new Color(COLOR_RED));
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,WORD_SIZE));
		g.drawString("SCORE:" + score,10,25);
		g.drawString("LIFE:" + hero.getLife(),10,45);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Fly");
		ShootGame game = new ShootGame();
		frame.add(game);
		frame.setSize(WIDTH,HEIGHT);
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
		game.action();	
	}

}
