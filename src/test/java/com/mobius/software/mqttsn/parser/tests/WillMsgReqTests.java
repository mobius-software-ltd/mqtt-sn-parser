package com.mobius.software.mqttsn.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.WillMsgReq;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class WillMsgReqTests
{
	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.WILL_MSG_REQ, Parser.WILL_MSG_REQ.getType());
			assertEquals(8, Parser.WILL_MSG_REQ.getType().getValue());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testLength()
	{
		try
		{
			assertEquals(2, Parser.WILL_MSG_REQ.getLength());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testEncodeDeode()
	{
		try
		{
			WillMsgReq willMsgReq = new WillMsgReq();
			ByteBuf expected = Parser.encode(Parser.WILL_MSG_REQ);
			ByteBuf actual = Parser.encode(willMsgReq);
			assertTrue(ByteBufUtil.equals(expected, actual));
			willMsgReq = (WillMsgReq) Parser.decode(actual);
			Assertion.assertWillMsgReq(Parser.WILL_MSG_REQ, willMsgReq);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidExceedingLength()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x03, 0x06 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x01, 0x06 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}
}
