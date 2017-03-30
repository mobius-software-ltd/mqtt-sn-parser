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
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.SNPingreq;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class PingreqTests
{
	private static final String CLIENT_ID = "dummy_123";
	private static SNPingreq message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new SNPingreq(CLIENT_ID);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.PINGREQ, message.getType());
			assertEquals(0x16, message.getType().getValue());
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
			assertEquals(2 + CLIENT_ID.length(), message.getLength());
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
			SNPingreq pingreq = new SNPingreq(CLIENT_ID);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(pingreq);
			assertTrue(ByteBufUtil.equals(expected, actual));
			pingreq = (SNPingreq) Parser.decode(actual);
			Assertion.assertPingreq(message, pingreq);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidExceedingLength()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x0c, 0x16, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x0a, 0x16, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}
}
