package com.mobius.software.mqttsn.parser.avps;

import java.util.HashMap;
import java.util.Map;

import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;

public enum TopicType
{
	NAMED(0), PREDEFINED(1), SHORT(2);

	private int value;

	private static Map<Integer, TopicType> map = new HashMap<Integer, TopicType>();

	static
	{
		for (TopicType type : TopicType.values())
		{
			map.put(type.value, type);
		}
	}

	public int getValue()
	{
		return value;
	}

	private TopicType(final int value)
	{
		this.value = value;
	}

	public static TopicType valueOf(int type) throws MalformedMessageException
	{
		TopicType result = map.get(type);
		if (result == null)
			throw new MalformedMessageException(String.format("Invalid TopicIdType value: %d", type));
		return result;
	}
}
