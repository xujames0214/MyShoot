package cn.tedu.shoot;

import java.util.Random;

public class Bee extends FlyingObject implements Award{
	private int xSpeed; //x坐标移动速度
	private int ySpeed; //y坐标移动速度
	private static final int BEE_X_SPEED = 1;
	private static final int BEE_Y_SPEED = 2;
	private static final int AWARD_TYPE_NUMBER = 2;//奖励类型数量
	private int awardType; //奖励类型0或1
	public int getType(){
		return awardType;
	}
	
	public Bee(){
		image = ShootGame.bee;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		int xDirection = rand.nextInt(2);
		if(xDirection == 0){
			xSpeed = BEE_X_SPEED;
		}else{
			xSpeed = -BEE_X_SPEED;
		}
		ySpeed = BEE_Y_SPEED;
		x = rand.nextInt(ShootGame.WIDTH - this.width);
		y = -this.height;
		awardType = rand.nextInt(AWARD_TYPE_NUMBER);
	}
	
	public void step(){
		y += ySpeed;
		x += xSpeed;
		if(x >= ShootGame.WIDTH - this.width){
			xSpeed = -BEE_X_SPEED;
		}
		if(x <= 0){
			xSpeed = BEE_X_SPEED;
		}
	}
	
	public boolean outOfBounds(){
		return this.y >= ShootGame.HEIGHT;
	}
}
