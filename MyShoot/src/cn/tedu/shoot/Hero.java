package cn.tedu.shoot;
import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{
	private int doubleFire;
	private int life;
	private BufferedImage[] images;
	private int index;
	private static final int IMAGES_NUMBER = 2;
	private static final int IMAGES_ALTERNATE_INTERVAL = 10;
	private static final int HERO_X = 150;
	private static final int HERO_Y = 400;
	private static final int ORIGINAL_DOUBLEFIRE = 0;
	private static final int DOUBLEFIRE = 40;
	private static final int ORIGINAL_LIFE = 3;
	public Hero(){
		image = ShootGame.hero0;
		width = image.getWidth();
		height = image.getHeight();
		x = HERO_X;
		y = HERO_Y;
		doubleFire = ORIGINAL_DOUBLEFIRE;
		life = ORIGINAL_LIFE;
		images = new BufferedImage[IMAGES_NUMBER];
		images[0] = ShootGame.hero0;
		images[1] = ShootGame.hero1;
	}
	
	public void step(){
		image = images[index++ / IMAGES_ALTERNATE_INTERVAL % images.length];
	}
	private static final int HERO_BULLET_DISTANCE = 20;
	private static final int DOUBLEFIRE_DECREASE_PER_SHOOT = 2;

	public Bullet[] shoot(){
		int xStep = this.width / 4;
		int yStep = HERO_BULLET_DISTANCE;
		Bullet[] bs;
		if(doubleFire > 0){
			bs = new Bullet[2];
			bs[0] = new Bullet(this.x + xStep * 1,this.y - yStep);
			bs[1] = new Bullet(this.x + xStep * 3,this.y - yStep);
			doubleFire -= DOUBLEFIRE_DECREASE_PER_SHOOT;
		}else{
			bs = new Bullet[1];
			bs[0] = new Bullet(this.x + xStep * 2,this.y - yStep);
		}
		return bs;
	}
	
	public void moveTo(int x,int y){
		this.x = x - this.width / 2;
		this.y = y - this.height / 2;
	}
	
	public boolean outOfBounds(){
		return false;
	}
	
	public void addLife(){
		life++;
	}
	public void subtractLife(){
		life--;
	}
	public void addDoubleFire(){
		doubleFire += DOUBLEFIRE;
	}
	public void clearDoubleFire(){
		doubleFire = 0;
	}
	
	public int getLife(){
		return life;
	}
	
	public boolean hit(FlyingObject other){
		int x1 = other.x - this.width / 2;
		int x2 = other.x + other.width + this.width / 2;
		int y1 = other.y - this.height / 2;
		int y2 = other.y + other.height + this.height / 2;
		int x = this.x + this.width / 2;
		int y = this.y + this.height / 2;
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}
}
