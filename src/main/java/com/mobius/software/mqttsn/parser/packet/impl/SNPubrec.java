package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;

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

	@Override
	public void processBy(SNDevice device)
	{
		device.processPubrec(messageID);
	}
}