package com.mobius.software.mqttsn.parser.avps;

import java.util.HashMap;
import java.util.Map;

public enum Radius
{
	BROADCAST(0), RADIUS_1(1), RADIUS_2(2), RADIUS_3(3);

	private static final Map<Integer, Radius> intToTypeMap = new HashMap<Integer, Radius>();
	private static final Map<String, Radius> strToTypeMap = new HashMap<String, Radius>();

	static
	{
		for (Radius radius : Radius.values())
		{
			intToTypeMap.put((int) radius.value, radius);
			strToTypeMap.put(radius.name(), radius);
		}
	}

	private int value;

	Radius(final int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static Radius valueOf(int type)
	{
		return intToTypeMap.get(type);
	}
}
