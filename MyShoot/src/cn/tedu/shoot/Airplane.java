package cn.tedu.shoot;
import java.util.Random;
public class Airplane extends FlyingObject implements Enemy{
	private int speed;
	private static final int AIRPLANE_SPEED = 3;
	public Airplane(){
		image = ShootGame.airplane;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		x = rand.nextInt(ShootGame.WIDTH - this.width);
		y = -this.height;
		speed = AIRPLANE_SPEED;
	}
	
	public int getScore(){
		return SCORE;
	}
	
	public void step(){
		this.y += speed;
	}
	
	public boolean outOfBounds(){
		return this.y >= ShootGame.HEIGHT;
	}

}
