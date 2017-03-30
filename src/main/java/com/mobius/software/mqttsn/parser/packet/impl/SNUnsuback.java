package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;

public class SNUnsuback extends CountableMessage
{
	public SNUnsuback()
	{
		super();
	}

	public SNUnsuback(int messageID)
	{
		super(messageID);
	}

	public SNUnsuback reInit(int messageID)
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
