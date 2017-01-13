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
import com.mobius.software.mqttsn.parser.avps.NamedTopic;
import com.mobius.software.mqttsn.parser.avps.QoS;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.WillTopicUpd;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class WillTopicUpdTests
{
	private static WillTopicUpd message;
	private static final boolean RETAIN = true;
	private static final NamedTopic TOPIC = new NamedTopic("name", QoS.AT_LEAST_ONCE);

	@BeforeClass
	public static void beforeClass()
	{
		message = new WillTopicUpd(RETAIN, TOPIC);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.WILL_TOPIC_UPD, message.getType());
			assertEquals(0x1A, message.getType().getValue());
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
			NamedTopic topic = new NamedTopic(topicName, QoS.EXACTLY_ONCE);
			WillTopicUpd expected = new WillTopicUpd(false, topic);
			assertEquals(5 + expected.getTopic().length(), expected.getLength());
			ByteBuf buf = Parser.encode(expected);
			WillTopicUpd actual = (WillTopicUpd) Parser.decode(buf);
			assertEquals(expected.getLength(), actual.getLength());
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
			WillTopicUpd willTopicUpd = new WillTopicUpd(RETAIN, TOPIC);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(willTopicUpd);
			assertTrue(ByteBufUtil.equals(expected, actual));
			willTopicUpd = (WillTopicUpd) Parser.decode(actual);
			Assertion.assertWillTopicUpd(message, willTopicUpd);
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
		byte[] invalidLengthEncodingArray =
		{ 0x08, 0x1a, 0x30, 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidLengthEncodingArray.length);
		buf.writeBytes(invalidLengthEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidLengthEncodingArray =
		{ 0x06, 0x1a, 0x30, 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidLengthEncodingArray.length);
		buf.writeBytes(invalidLengthEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsDuplicate()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x07, 0x1a, (byte) (0x30 + Flag.DUPLICATE.getValue()), 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsWill()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x07, 0x1a, (byte) (0x30 + Flag.WILL.getValue()), 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsCleanSession()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x07, 0x1a, (byte) (0x30 + Flag.CLEAN_SESSION.getValue()), 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsShortTopic()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x07, 0x1a, (byte) (0x30 + Flag.SHORT_TOPIC.getValue()), 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsPredefinedTopic()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x07, 0x1a, (byte) (0x30 + Flag.ID_TOPIC.getValue()), 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidFlagsTopicReserved()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x07, 0x1a, (byte) (0x30 + Flag.RESERVED_TOPIC.getValue()), 0x6e, 0x61, 0x6d, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}
}
