package com.mobius.software.mqttsn.parser.avps;

import java.nio.ByteBuffer;

public class PredefinedTopic implements Topic
{
	private int value;
	private QoS qos;

	public PredefinedTopic()
	{
		super();
	}

	public PredefinedTopic(int value, QoS qos)
	{
		this.value = value;
		this.qos = qos;
	}

	public PredefinedTopic reInit(int value, QoS qos)
	{
		this.value = value;
		this.qos = qos;
		return this;
	}

	@Override
	public TopicType getType()
	{
		return TopicType.PREDEFINED;
	}

	@Override
	public byte[] encode()
	{
		return ByteBuffer.allocate(2).putShort((short) value).array();
	}

	@Override
	public int length()
	{
		return 2;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((qos == null) ? 0 : qos.hashCode());
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PredefinedTopic other = (PredefinedTopic) obj;
		if (qos != other.qos)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	public int getValue()
	{
		return value;
	}

	public void setValue(int value)
	{
		this.value = value;
	}

	public QoS getQos()
	{
		return qos;
	}

	public void setQos(QoS qos)
	{
		this.qos = qos;
	}
}
