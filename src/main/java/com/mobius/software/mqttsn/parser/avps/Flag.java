package com.mobius.software.mqttsn.parser.avps;

import java.util.HashMap;
import java.util.Map;

import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;

public enum Flag
{
	DUPLICATE(128), QOS_LEVEL_ONE(96), QOS_2(64), QOS_1(32), RETAIN(16), WILL(8), CLEAN_SESSION(4), RESERVED_TOPIC(3), SHORT_TOPIC(2), ID_TOPIC(1);

	private static final Map<Integer, Flag> intToTypeMap = new HashMap<Integer, Flag>();
	private static final Map<String, Flag> strToTypeMap = new HashMap<String, Flag>();

	static
	{
		for (Flag flags : Flag.values())
		{
			intToTypeMap.put((int) flags.value, flags);
			strToTypeMap.put(flags.name(), flags);
		}
	}

	private int value;

	Flag(final int value)
	{
		this.value = value;
	}

	public int getValue()
	{
		return value;
	}

	public static Flag valueOf(int type) throws MalformedMessageException
	{
		return intToTypeMap.get(type);
	}
}
