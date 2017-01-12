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
import com.mobius.software.mqttsn.parser.avps.Flag;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.Connect;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class ConnectTests
{
	private static final boolean cleanSession = true;
	private static final int duration = 11;
	private static final String clientID = "dummy_123";
	private static final boolean willPresent = true;
	private static Connect message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new Connect(cleanSession, duration, clientID, willPresent);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.CONNECT, message.getType());
			assertEquals(4, message.getType().getValue());
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
			assertEquals(6 + clientID.length(), message.getLength());
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
			Connect connect = new Connect(cleanSession, duration, clientID, willPresent);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(connect);
			assertTrue(ByteBufUtil.equals(expected, actual));
			connect = (Connect) Parser.decode(actual);
			Assertion.assertConnect(message, connect);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test(expected = MalformedMessageException.class)
	public void testEmptyClientID()
	{
		Connect connect = new Connect(true, 123, "", true);
		ByteBuf buf = Parser.encode(connect);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidProtocolID()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x0f, 0x04, 0x0c, 0x02, 0x00, 0x0b, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidExceedingLength()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x10, 0x04, 0x0c, 0x01, 0x00, 0x0b, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x0e, 0x04, 0x0c, 0x01, 0x00, 0x0b, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsDup()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x0f, 0x04, (byte) (0x0c + Flag.DUPLICATE.getValue()), 0x01, 0x00, 0x0b, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsQoS1()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x0f, 0x04, (byte) (0x0c + Flag.QOS_1.getValue()), 0x01, 0x00, 0x0b, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsQoS2()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x0f, 0x04, (byte) (0x0c + Flag.QOS_2.getValue()), 0x01, 0x00, 0x0b, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsQoSLevelOne()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x0f, 0x04, (byte) (0x0c + Flag.QOS_LEVEL_ONE.getValue()), 0x01, 0x00, 0x0b, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsRetain()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x0f, 0x04, (byte) (0x0c + Flag.RETAIN.getValue()), 0x01, 0x00, 0x0b, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsShortTopic()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x0f, 0x04, (byte) (0x0c + Flag.SHORT_TOPIC.getValue()), 0x01, 0x00, 0x0b, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsPredefinedTopic()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x0f, 0x04, (byte) (0x0c + Flag.ID_TOPIC.getValue()), 0x01, 0x00, 0x0b, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x5f, 0x31, 0x32, 0x33 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}
}
