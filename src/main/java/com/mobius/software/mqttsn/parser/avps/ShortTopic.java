package com.mobius.software.mqttsn.parser.avps;

public class ShortTopic implements SNTopic
{
	private String value;
	private SNQoS qos;

	public ShortTopic()
	{
		super();
	}

	public ShortTopic(String value, SNQoS qos)
	{
		this.value = value;
		this.qos = qos;
	}

	public ShortTopic reInit(String value, SNQoS qos)
	{
		this.value = value;
		this.qos = qos;
		return this;
	}

	@Override
	public TopicType getType()
	{
		return TopicType.SHORT;
	}

	@Override
	public byte[] encode()
	{
		return value.getBytes();
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
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		ShortTopic other = (ShortTopic) obj;
		if (qos != other.qos)
			return false;
		if (value == null)
		{
			if (other.value != null)
				return false;
		}
		else if (!value.equals(other.value))
			return false;
		return true;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public SNQoS getQos()
	{
		return qos;
	}

	public void setQos(SNQoS qos)
	{
		this.qos = qos;
	}
}
