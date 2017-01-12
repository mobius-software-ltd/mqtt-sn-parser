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
import com.mobius.software.mqttsn.parser.packet.impl.WillTopicReq;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class WillTopicReqTests
{
	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.WILL_TOPIC_REQ, Parser.WILL_TOPIC_REQ.getType());
			assertEquals(6, Parser.WILL_TOPIC_REQ.getType().getValue());
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
			assertEquals(2, Parser.WILL_TOPIC_REQ.getLength());
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
			WillTopicReq willTopicReq = new WillTopicReq();
			ByteBuf expected = Parser.encode(Parser.WILL_TOPIC_REQ);
			ByteBuf actual = Parser.encode(willTopicReq);
			assertTrue(ByteBufUtil.equals(expected, actual));
			willTopicReq = (WillTopicReq) Parser.decode(actual);
			Assertion.assertWillTopicReq(Parser.WILL_TOPIC_REQ, willTopicReq);
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
