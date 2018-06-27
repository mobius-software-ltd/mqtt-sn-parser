package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public class SNConnect extends SNMessage
{
	public static final int MQTT_SN_PROTOCOL_ID = 1;

	private boolean willPresent;
	private boolean cleanSession;
	private int protocolID = MQTT_SN_PROTOCOL_ID;
	private int duration;
	private String clientID;

	public SNConnect()
	{
		super();
	}

	public SNConnect(boolean cleanSession, int duration, String clientID, boolean willPresent)
	{
		this.cleanSession = cleanSession;
		this.duration = duration;
		this.clientID = clientID;
		this.willPresent = willPresent;
	}

	public SNConnect reInit(boolean cleanSession, int duration, String clientID, boolean willPresent)
	{
		this.cleanSession = cleanSession;
		this.duration = duration;
		this.clientID = clientID;
		this.willPresent = willPresent;
		return this;
	}

	@Override
	public int getLength()
	{
		if (clientID == null || clientID.isEmpty())
			throw new MalformedMessageException("connect must contain a valid clientID");
		return 6 + clientID.length();
	}

	@Override
	public SNType getType()
	{
		return SNType.CONNECT;
	}

	public boolean isWillPresent()
	{
		return willPresent;
	}

	public void setWillPresent(boolean willPresent)
	{
		this.willPresent = willPresent;
	}

	public boolean isCleanSession()
	{
		return cleanSession;
	}

	public void setCleanSession(boolean cleanSession)
	{
		this.cleanSession = cleanSession;
	}

	public int getProtocolID()
	{
		return protocolID;
	}

	public void setProtocolID(int protocolID)
	{
		this.protocolID = protocolID;
	}

	public int getDuration()
	{
		return duration;
	}

	public void setDuration(int duration)
	{
		this.duration = duration;
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
