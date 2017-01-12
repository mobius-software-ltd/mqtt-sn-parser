package com.mobius.software.mqttsn.parser.packet.impl;

import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.avps.Topic;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

import io.netty.buffer.ByteBuf;

public class Publish implements SNMessage
{
	private int messageID;
	private Topic topic;
	private ByteBuf content;
	private boolean dup;
	private boolean retain;

	public Publish()
	{
		super();
	}

	public Publish(int messageID, Topic topic, ByteBuf content, boolean dup, boolean retain)
	{
		this.messageID = messageID;
		this.topic = topic;
		this.content = content;
		this.dup = dup;
		this.retain = retain;
	}

	public Publish reInit(int messageID, Topic topic, ByteBuf content, boolean dup, boolean retain)
	{
		this.messageID = messageID;
		this.topic = topic;
		this.content = content;
		this.dup = dup;
		this.retain = retain;
		return this;
	}

	@Override
	public int getLength()
	{
		int length = 7;
		length += content.capacity();
		if (content.capacity() > 248)
			length += 2;
		return length;
	}

	@Override
	public SNType getType()
	{
		return SNType.PUBLISH;
	}

	public int getMessageID()
	{
		return messageID;
	}

	public void setMessageID(int messageID)
	{
		this.messageID = messageID;
	}

	public Topic getTopic()
	{
		return topic;
	}

	public void setTopic(Topic topic)
	{
		this.topic = topic;
	}

	public ByteBuf getContent()
	{
		return content;
	}

	public void setContent(ByteBuf content)
	{
		this.content = content;
	}

	public boolean isDup()
	{
		return dup;
	}

	public void setDup(boolean dup)
	{
		this.dup = dup;
	}

	public boolean isRetain()
	{
		return retain;
	}

	public void setRetain(boolean retain)
	{
		this.retain = retain;
	}
}