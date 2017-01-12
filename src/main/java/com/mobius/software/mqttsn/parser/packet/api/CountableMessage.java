package com.mobius.software.mqttsn.parser.packet.api;

public abstract class CountableMessage implements SNMessage
{
	private int messageID;

	public CountableMessage()
	{
		super();
	}

	public CountableMessage(int messageID)
	{
		this.messageID = messageID;
	}

	public CountableMessage reInit(int messageID)
	{
		this.messageID = messageID;
		return this;
	}

	@Override
	public int getLength()
	{
		return 4;
	}

	public int getMessageID()
	{
		return messageID;
	}

	public void setMessageID(int messageID)
	{
		this.messageID = messageID;
	}
}
