package com.mobius.software.mqttsn.parser.avps;

import java.util.HashMap;
import java.util.Map;

import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;

public enum SNType
{
	ADVERTISE(0x00), SEARCHGW(0x01), GWINFO(0x02), CONNECT(0x04), CONNACK(0x05), WILL_TOPIC_REQ(0x06), WILL_TOPIC(0x07), WILL_MSG_REQ(0x08), WILL_MSG(0x09), REGISTER(0x0A), REGACK(0x0B), PUBLISH(0x0C), PUBACK(0x0D), PUBCOMP(0x0E), PUBREC(0x0F), PUBREL(0x10), SUBSCRIBE(0x12), SUBACK(0x13), UNSUBSCRIBE(0x14), UNSUBACK(0x15), PINGREQ(0x16), PINGRESP(0x17), DISCONNECT(0x18), WILL_TOPIC_UPD(0x1A), WILL_TOPIC_RESP(0x1B), WILL_MSG_UPD(0x1C), WILL_MSG_RESP(0x1D), ENCAPSULATED(0xFE);

	private int value;

	private static Map<Integer, SNType> map = new HashMap<Integer, SNType>();

	static
	{
		for (SNType legEnum : SNType.values())
		{
			map.put(legEnum.value, legEnum);
		}
	}

	public int getValue()
	{
		return value;
	}

	private SNType(final int leg)
	{
		value = leg;
	}

	public static SNType valueOf(int type) throws MalformedMessageException
	{
		SNType result = map.get(type);
		if (result == null)
			throw new MalformedMessageException(String.format("Header code undefined: %d", type));
		return result;
	}
}
