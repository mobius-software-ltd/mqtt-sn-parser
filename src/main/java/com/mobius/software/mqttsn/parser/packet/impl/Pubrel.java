package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;

public class Pubrel extends CountableMessage
{
	public Pubrel()
	{
		super();
	}

	public Pubrel(int messageID)
	{
		super(messageID);
	}

	public Pubrel reInit(int messageID)
	{
		setMessageID(messageID);
		return this;
	}

	@Override
	public SNType getType()
	{
		return SNType.PUBREL;
	}
}