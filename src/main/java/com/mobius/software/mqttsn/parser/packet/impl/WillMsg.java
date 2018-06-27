package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

import io.netty.buffer.ByteBuf;

public class WillMsg extends SNMessage
{
	private ByteBuf content;

	public WillMsg()
	{
		super();
	}

	public WillMsg(ByteBuf content)
	{
		this.content = content;
	}

	public WillMsg reInit(ByteBuf content)
	{
		this.content = content;
		return this;
	}

	@Override
	public int getLength()
	{
		int length = 2;
		length += content.capacity();
		if (content.capacity() > 253)
			length += 2;
		return length;
	}

	@Override
	public SNType getType()
	{
		return SNType.WILL_MSG;
	}

	public ByteBuf getContent()
	{
		return content;
	}

	public void setContent(ByteBuf content)
	{
		this.content = content;
	}
}