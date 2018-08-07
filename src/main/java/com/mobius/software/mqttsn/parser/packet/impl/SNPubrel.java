package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;

public class SNPubrel extends CountableMessage
{
	public SNPubrel()
	{
		super();
	}

	public SNPubrel(int messageID)
	{
		super(messageID);
	}

	public SNPubrel reInit(int messageID)
	{
		setMessageID(messageID);
		return this;
	}

	@Override
	public SNType getType()
	{
		return SNType.PUBREL;
	}

	@Override
	public void processBy(SNDevice device)
	{
		device.processPubrel(messageID);
	}
}