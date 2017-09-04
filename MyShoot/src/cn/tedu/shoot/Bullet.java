package cn.tedu.shoot;

public class Bullet extends FlyingObject{
	private int speed;
	private static final int BULLET_SPEED = 3;
	public Bullet(int x,int y){
		image = ShootGame.bullet;
		width = image.getWidth();
		height = image.getHeight();
		speed = BULLET_SPEED;
		this.x = x;
		this.y = y;
	}
	
	public void step(){
		y -= speed;
	}
	
	public boolean outOfBounds(){
		return this.y <= -this.height;
	}
}
