package com.mobius.software.mqttsn.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.avps.Flag;
import com.mobius.software.mqttsn.parser.avps.FullTopic;
import com.mobius.software.mqttsn.parser.avps.SNQoS;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.WillTopic;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

public class WillTopicTests
{
	private static WillTopic message;
	private static final boolean RETAIN = true;
	private static final FullTopic TOPIC = new FullTopic("name", SNQoS.AT_LEAST_ONCE);

	@BeforeClass
	public static void beforeClass()
	{
		message = new WillTopic(RETAIN, TOPIC);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.WILL_TOPIC, message.getType());
			assertEquals(7, message.getType().getValue());
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
			assertEquals(3 + TOPIC.length(), message.getLength());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testThreeOctetLength()
	{
		try
		{
			int totalSegments = 28;
			StringBuilder sb = new StringBuilder();
			sb.append("root");
			for (int i = 0; i < totalSegments; i++)
				sb.append("/segment").append(i);
			String topicName = sb.toString();
			FullTopic topic = new FullTopic(topicName, SNQoS.EXACTLY_ONCE);
			WillTopic expected = new WillTopic(false, topic);
			assertEquals(5 + expected.getTopic().length(), expected.getLength());
			ByteBuf buf = Parser.encode(expected);
			WillTopic actual = (WillTopic) Parser.decode(buf);
			assertEquals(expected.getLength(), actual.getLength());
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
			WillTopic willTopic = new WillTopic(RETAIN, TOPIC);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(willTopic);
			assertTrue(ByteBufUtil.equals(expected, actual));
			willTopic = (WillTopic) Parser.decode(actual);
			Assertion.assertWillTopic(message, willTopic);
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
		{ 0x08, 0x07, 0x30, 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x06, 0x07, 0x30, 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsDuplicate()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x07, 0x07, (byte) (0x30 + Flag.DUPLICATE.getValue()), 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsWill()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x07, 0x07, (byte) (0x30 + Flag.WILL.getValue()), 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsCleanSession()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x07, 0x07, (byte) (0x30 + Flag.CLEAN_SESSION.getValue()), 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsShortTopic()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x07, 0x07, (byte) (0x30 + Flag.SHORT_TOPIC.getValue()), 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsPredefinedTopic()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x07, 0x07, (byte) (0x30 + Flag.ID_TOPIC.getValue()), 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidEmptyTopic()
	{
		WillTopic willTopic = new WillTopic(true, new FullTopic("", SNQoS.AT_LEAST_ONCE));
		ByteBuf buf = Parser.encode(willTopic);
		Parser.decode(buf);
	}
}
