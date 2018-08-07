package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNDevice;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class GWInfo extends SNMessage
{
	private int gwID;
	private String gwAddress;

	public GWInfo()
	{
		super();
	}

	public GWInfo(int gwID, String gwAddress)
	{
		this.gwID = gwID;
		this.gwAddress = gwAddress;
	}

	public GWInfo reInit(int gwID, String gwAddress)
	{
		this.gwID = gwID;
		this.gwAddress = gwAddress;
		return this;
	}

	@Override
	public int getLength()
	{
		int length = 3;
		if (gwAddress != null)
			length += gwAddress.length();
		return length;
	}

	@Override
	public SNType getType()
	{
		return SNType.GWINFO;
	}

	public int getGwID()
	{
		return gwID;
	}

	public void setGwID(int gwID)
	{
		this.gwID = gwID;
	}

	public String getGwAddress()
	{
		return gwAddress;
	}

	public void setGwAddress(String gwAddress)
	{
		this.gwAddress = gwAddress;
	}

	@Override
	public void processBy(SNDevice device)
	{
		device.processGwInfo(gwID, gwAddress);
	}
}
