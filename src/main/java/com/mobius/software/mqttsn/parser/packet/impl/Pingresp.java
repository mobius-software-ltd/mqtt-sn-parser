package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class Pingresp implements SNMessage
{
	@Override
	public int getLength()
	{
		return 2;
	}

	@Override
	public SNType getType()
	{
		return SNType.PINGRESP;
	}
}
