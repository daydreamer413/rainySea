package com.example.util;
/**
 * 编码生成类
 */
import java.util.Random;

public class CheckCode {

	public String getCheckCode(){
		Random random = new Random();
		String sRand="";
		for (int i=0;i<8;i++){
	    String rand=String.valueOf(random.nextInt(10));
	    sRand+=rand;
		}
	    return sRand;
	}
	
}
