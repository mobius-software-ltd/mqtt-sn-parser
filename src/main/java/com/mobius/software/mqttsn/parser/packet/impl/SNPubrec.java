package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;

public class SNPubrec extends CountableMessage
{
	public SNPubrec()
	{
		super();
	}

	public SNPubrec(int messageID)
	{
		super(messageID);
	}

	public SNPubrec reInit(int messageID)
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