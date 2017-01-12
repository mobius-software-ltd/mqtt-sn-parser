package com.mobius.software.mqttsn.parser.avps;

import java.util.HashMap;
import java.util.Map;

import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;

public enum ReturnCode
{
	ACCEPTED(0), CONGESTION(1), INVALID_TOPIC_ID(2), NOT_SUPPORTED(3);

	private int value;

	private static Map<Integer, ReturnCode> map = new HashMap<Integer, ReturnCode>();

	static
	{
		for (ReturnCode code : ReturnCode.values())
		{
			map.put(code.value, code);
		}
	}

	public int getValue()
	{
		return value;
	}

	private ReturnCode(final int value)
	{
		this.value = value;
	}

	public static ReturnCode valueOf(int type) throws MalformedMessageException
	{
		ReturnCode result = map.get(type);
		if (result == null)
			throw new MalformedMessageException(String.format("Return code undefined: %d", type));
		return result;
	}
}
