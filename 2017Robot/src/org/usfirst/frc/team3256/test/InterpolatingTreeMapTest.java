package org.usfirst.frc.team3256.test;

import org.usfirst.frc.team3256.lib.InterpolatingDouble;
import org.usfirst.frc.team3256.lib.InterpolatingTreeMap;

public class InterpolatingTreeMapTest {

	public static void main(String[] args){
		InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> map = new InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble>(50);
		for(int i=0; i<=50; i+=5){
			map.put(new InterpolatingDouble(i + 20.0), new InterpolatingDouble(6000.0 + i*20));
		}
		map.print();
	}
}
