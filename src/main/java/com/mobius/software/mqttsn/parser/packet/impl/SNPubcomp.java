package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.CountableMessage;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;

public class SNPubcomp extends CountableMessage
{
	public SNPubcomp()
	{
		super();
	}

	public SNPubcomp(int messageID)
	{
		super(messageID);
	}

	public SNPubcomp reInit(int messageID)
	{
		setMessageID(messageID);
		return this;
	}

	@Override
	public SNType getType()
	{
		return SNType.PUBCOMP;
	}

	@Override
	public void processBy(SNDevice device)
	{
		device.processPubcomp(messageID);
	}
}
