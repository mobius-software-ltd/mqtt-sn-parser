package com.mobius.software.mqttsn.parser.avps;

public class FullTopic implements SNTopic
{
	private String value;
	private SNQoS qos;

	public FullTopic()
	{
		super();
	}

	public FullTopic(String value, SNQoS qos)
	{
		this.value = value;
		this.qos = qos;
	}

	public FullTopic reInit(String value, SNQoS qos)
	{
		this.value = value;
		this.qos = qos;
		return this;
	}

	@Override
	public TopicType getType()
	{
		return TopicType.NAMED;
	}

	@Override
	public byte[] encode()
	{
		return value.getBytes();
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
		FullTopic other = (FullTopic) obj;
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

	@Override
	public int length()
	{
		return value.length();
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
