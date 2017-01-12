package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;

public class Unsuback extends CountableMessage
{
	public Unsuback()
	{
		super();
	}

	public Unsuback(int messageID)
	{
		super(messageID);
	}

	public Unsuback reInit(int messageID)
	{
		setMessageID(messageID);
		return this;
	}

	@Override
	public SNType getType()
	{
		return SNType.UNSUBACK;
	}
}
