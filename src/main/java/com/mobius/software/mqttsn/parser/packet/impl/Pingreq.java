package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class Pingreq implements SNMessage
{
	private String clientID;

	public Pingreq()
	{
		super();
	}

	public Pingreq(String clientID)
	{
		this.clientID = clientID;
	}

	public Pingreq reInit(String clientID)
	{
		this.clientID = clientID;
		return this;
	}

	@Override
	public int getLength()
	{
		int length = 2;
		if (clientID != null)
			length += clientID.length();
		return length;
	}

	@Override
	public SNType getType()
	{
		return SNType.PINGREQ;
	}

	public String getClientID()
	{
		return clientID;
	}

	public void setClientID(String clientID)
	{
		this.clientID = clientID;
	}
}