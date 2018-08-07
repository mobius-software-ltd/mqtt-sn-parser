package com.mobius.software.mqttsn.parser.packet.api;

import com.mobius.software.mqttsn.parser.avps.ReturnCode;

public abstract class ResponseMessage extends SNMessage
{
	protected ReturnCode code;

	public ResponseMessage()
	{
		super();
	}

	public ResponseMessage(ReturnCode code)
	{
		this.code = code;
	}

	public ResponseMessage reInit(ReturnCode code)
	{
		this.code = code;
		return this;
	}

	@Override
	public int getLength()
	{
		return 3;
	}

	public ReturnCode getCode()
	{
		return code;
	}

	public void setCode(ReturnCode code)
	{
		this.code = code;
	}
}
