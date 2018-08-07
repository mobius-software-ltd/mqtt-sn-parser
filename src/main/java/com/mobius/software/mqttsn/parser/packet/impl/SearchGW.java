package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.Radius;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class SearchGW extends SNMessage
{
	private int radius;

	public SearchGW()
	{
		super();
	}

	public SearchGW(int radius)
	{
		this.radius = radius;
	}

	public SearchGW reInit(int radius)
	{
		this.radius = radius;
		return this;
	}

	@Override
	public int getLength()
	{
		return 3;
	}

	@Override
	public SNType getType()
	{
		return SNType.SEARCHGW;
	}

	public int getRadius()
	{
		return radius;
	}

	public void setRadius(int radius)
	{
		this.radius = radius;
	}

	@Override
	public void processBy(SNDevice device)
	{
		device.processSearchGw(Radius.valueOf(radius));
	}
}
