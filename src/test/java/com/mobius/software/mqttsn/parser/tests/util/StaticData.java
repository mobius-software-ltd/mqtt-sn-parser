package com.mobius.software.mqttsn.parser.tests.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class StaticData
{
	public static String messageToHex(SNMessage message)
	{
		return bufToHex(Parser.encode(message));
	}

	public static String bufToHex(ByteBuf buf)
	{
		String dump = ByteBufUtil.hexDump(buf);
		String formatted = "{";
		for (int i = 0, step = 2; i < dump.length(); i += step)
		{
			formatted += "0x";
			formatted += dump.substring(i, i + step);
			formatted += ",";
		}
		if (formatted.length() > 1)
			formatted = formatted.substring(0, formatted.length() - 1);
		formatted += "}";
		return formatted;
	}
}
