package com.mobius.software.mqttsn.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import org.junit.Before;
import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.avps.IdentifierTopic;
import com.mobius.software.mqttsn.parser.avps.SNQoS;
import com.mobius.software.mqttsn.parser.avps.Radius;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.avps.SNTopic;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.Encapsulated;
import com.mobius.software.mqttsn.parser.packet.impl.SNSubscribe;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class EncapsulatedTests
{
	private static final int MESSAGE_ID = 22;
	private static final SNTopic TOPIC = new IdentifierTopic(33, SNQoS.EXACTLY_ONCE);
	private static final boolean DUP = false;
	private static final Radius RADIUS = Radius.RADIUS_1;
	private static final String WIRELESS_NODE_ID = "dummy id";
	private static SNSubscribe innerMessage;
	private static Encapsulated message;

	@Before
	public void setUp()
	{
		innerMessage = new SNSubscribe(MESSAGE_ID, TOPIC, DUP);
		message = new Encapsulated(RADIUS, WIRELESS_NODE_ID, innerMessage);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.ENCAPSULATED, message.getType());
			assertEquals(0xFE, message.getType().getValue());
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
			assertEquals(3 + WIRELESS_NODE_ID.length(), message.getLength());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testEncodeDecode()
	{
		try
		{
			Encapsulated encapsulated = new Encapsulated(RADIUS, WIRELESS_NODE_ID, innerMessage);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(encapsulated);
			assertTrue(ByteBufUtil.equals(expected, actual));
			Assertion.assertMessage(message, Parser.decode(actual));
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
		{ 0x0c, (byte) 0xfe, 0x00, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x20, 0x69, 0x64, 0x07, 0x12, 0x41, 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidLengthArray =
		{ 0x0a, (byte) 0xfe, 0x00, 0x64, 0x75, 0x6d, 0x6d, 0x79, 0x20, 0x69, 0x64, 0x07, 0x12, 0x41, 0x00, 0x16, 0x00, 0x21 };
		ByteBuf buf = Unpooled.buffer(invalidLengthArray.length);
		buf.writeBytes(invalidLengthArray);
		Parser.decode(buf);
	}
}
