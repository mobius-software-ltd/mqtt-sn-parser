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
import com.mobius.software.mqttsn.parser.packet.impl.Disconnect;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class DisconnectTests
{
	private static final int DURATION = 10;
	private static Disconnect message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new Disconnect(DURATION);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.DISCONNECT, message.getType());
			assertEquals(0x18, message.getType().getValue());
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
			assertEquals(4, message.getLength());
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
			Disconnect disconnect = new Disconnect(DURATION);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(disconnect);
			assertTrue(ByteBufUtil.equals(expected, actual));
			disconnect = (Disconnect) Parser.decode(actual);
			Assertion.assertDisconnect(message, disconnect);
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
		byte[] invalidLengthArray =
		{ 0x05, 0x18, 0x00, 0x0a };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidLengthArray =
		{ 0x03, 0x18, 0x00, 0x0a };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}
}
