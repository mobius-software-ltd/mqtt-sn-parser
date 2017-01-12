package com.mobius.software.mqttsn.parser.avps;

import java.util.HashMap;
import java.util.Map;

public enum Control
{
	BROADCAST(0), RADIUS_1(1), RADIUS_2(2), RADIUS_3(3);

	private static final Map<Integer, Control> intToTypeMap = new HashMap<Integer, Control>();
	private static final Map<String, Control> strToTypeMap = new HashMap<String, Control>();

	static
	{
		for (Control control : Control.values())
		{
			intToTypeMap.put((int) control.value, control);
			strToTypeMap.put(control.name(), control);
		}
	}

	private int value;

	Control(final int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static Control valueOf(int type)
	{
		return intToTypeMap.get(type);
	}
}
