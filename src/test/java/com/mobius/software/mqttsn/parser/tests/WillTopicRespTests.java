package com.mobius.software.mqttsn.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.avps.ReturnCode;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.WillTopicResp;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class WillTopicRespTests
{
	private static final ReturnCode code = ReturnCode.ACCEPTED;
	private static WillTopicResp message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new WillTopicResp(code);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.WILL_TOPIC_RESP, message.getType());
			assertEquals(0x1B, message.getType().getValue());
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
			assertEquals(3, message.getLength());
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
			WillTopicResp willTopicResp = new WillTopicResp(code);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(willTopicResp);
			assertTrue(ByteBufUtil.equals(expected, actual));
			willTopicResp = (WillTopicResp) Parser.decode(actual);
			Assertion.assertWillTopicResp(message, willTopicResp);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidCode()
	{
		byte[] invalidReturnCodeArray =
		{ 0x03, 0x1b, 0x04 };
		ByteBuf buf = Unpooled.buffer(invalidReturnCodeArray.length);
		buf.writeBytes(invalidReturnCodeArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidExceedingLength()
	{
		byte[] invalidLengthArray =
		{ 0x04, 0x1b, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidLengthArray =
		{ 0x02, 0x1b, 0x00 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}
}
