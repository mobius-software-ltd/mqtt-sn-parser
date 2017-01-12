package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class Advertise implements SNMessage
{
	private int gwID;
	private int duration;

	public Advertise()
	{
		super();
	}

	public Advertise(int gwID, int duration)
	{
		this.gwID = gwID;
		this.duration = duration;
	}

	public Advertise reInit(int gwID, int duration)
	{
		this.gwID = gwID;
		this.duration = duration;
		return this;
	}

	@Override
	public int getLength()
	{
		return 5;
	}

	@Override
	public SNType getType()
	{
		return SNType.ADVERTISE;
	}

	public int getGwID()
	{
		return gwID;
	}

	public void setGwID(int gwID)
	{
		this.gwID = gwID;
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
