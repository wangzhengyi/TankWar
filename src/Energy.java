package com.xv.tankwar;

import java.awt.Color;
import java.awt.Graphics;

import com.sun.prism.shader.DrawCircle_Color_AlphaTest_Loader;

public class Energy {
	int x,y; //能量块的坐标位置
	int width = 20;//设置能量块的宽度     能量块的中心位置在 (x+5,y+5)
	boolean tag = true;
	public Energy(int x, int y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
	}
	public void draw(Graphics g) {
		if(this.tag==true) {
			g.setColor(Color.green);
			g.fillOval(x, y, width, width);
		}
	}
	
	public void hit(Tank tank) {
		if((Math.pow(((this.x+5)-(tank.x+15)),2)+Math.pow(((this.y+5)-(tank.y+15)),2))<700) {
			if(tank.blood<40) {
				tank.blood=40;
				this.tag = false;
			}
		} 


	}
	
}
