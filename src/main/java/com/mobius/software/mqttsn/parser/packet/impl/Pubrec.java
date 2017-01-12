package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;

public class Pubrec extends CountableMessage
{
	public Pubrec()
	{
		super();
	}

	public Pubrec(int messageID)
	{
		super(messageID);
	}

	public Pubrec reInit(int messageID)
	{
		setMessageID(messageID);
		return this;
	}

	@Override
	public SNType getType()
	{
		return SNType.PUBREC;
	}
}