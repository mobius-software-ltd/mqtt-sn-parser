package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class SNDisconnect implements SNMessage
{
	private int duration;

	public SNDisconnect()
	{

	}

	public SNDisconnect(int duration)
	{
		this.duration = duration;
	}

	public SNDisconnect reInit(Integer duration)
	{
		this.duration = duration;
		return this;
	}

	@Override
	public int getLength()
	{
		int length = 2;
		if (duration > 0)
			length += 2;
		return length;
	}

	@Override
	public SNType getType()
	{
		return SNType.DISCONNECT;
	}

	public int getDuration()
	{
		return duration;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
	}
}
