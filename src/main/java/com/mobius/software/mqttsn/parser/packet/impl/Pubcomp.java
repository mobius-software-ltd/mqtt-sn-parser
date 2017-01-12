package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;

public class Pubcomp extends CountableMessage
{
	public Pubcomp()
	{
		super();
	}

	public Pubcomp(int messageID)
	{
		super(messageID);
	}

	public Pubcomp reInit(int messageID)
	{
		setMessageID(messageID);
		return this;
	}

	@Override
	public SNType getType()
	{
		return SNType.PUBCOMP;
	}
}
