package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.Radius;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class Encapsulated extends SNMessage
{
	private Radius radius;
	private String wirelessNodeID;
	private SNMessage message;

	public Encapsulated()
	{
		super();
	}

	public Encapsulated(Radius radius, String wirelessNodeID, SNMessage message)
	{
		this.radius = radius;
		this.wirelessNodeID = wirelessNodeID;
		this.message = message;
	}

	public Encapsulated reInit(Radius radius, String wirelessNodeID, SNMessage message)
	{
		this.radius = radius;
		this.wirelessNodeID = wirelessNodeID;
		this.message = message;
		return this;
	}

	@Override
	public int getLength()
	{
		int length = 3;
		if (wirelessNodeID != null)
			length += wirelessNodeID.length();
		return length;
	}

	@Override
	public SNType getType()
	{
		return SNType.ENCAPSULATED;
	}

	public Radius getRadius()
	{
		return radius;
	}

	public void setRadius(Radius radius)
	{
		this.radius = radius;
	}

	public String getWirelessNodeID()
	{
		return wirelessNodeID;
	}

	public void setWirelessNodeID(String wirelessNodeID)
	{
		this.wirelessNodeID = wirelessNodeID;
	}

	public SNMessage getMessage()
	{
		return message;
	}

	public void setMessage(SNMessage message)
	{
		this.message = message;
	}

	@Override
	public void processBy(SNDevice device)
	{
		message.processBy(device);
	}
}
