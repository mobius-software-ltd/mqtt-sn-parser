package com.mobius.software.mqttsn.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;
import com.mobius.software.mqttsn.parser.packet.impl.WillMsg;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class WillMsgTests
{
	private static WillMsg message;
	private static ByteBuf content;

	@BeforeClass
	public static void beforeClass()
	{
		message = new WillMsg(loadContent());
	}

	@Before
	public void setUp()
	{
		content = loadContent();
	}

	private static ByteBuf loadContent()
	{
		byte[] value = "message".getBytes();
		ByteBuf content = Unpooled.buffer(value.length);
		content.writeBytes(value);
		return content;
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.WILL_MSG, message.getType());
			assertEquals(9, message.getType().getValue());
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
			assertEquals(2 + content.readableBytes(), message.getLength());
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
			WillMsg willMsg = new WillMsg(loadContent());
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(willMsg);
			assertTrue(ByteBufUtil.equals(expected, actual));
			willMsg = (WillMsg) Parser.decode(actual);
			Assertion.assertWillMsg(message, willMsg);
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
		{ 0x10, 0x09, 0x6d, 0x65, 0x73, 0x73, 0x61, 0x67, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidInsufficientLength()
	{
		byte[] invalidProtocolEncodingArray =
		{ 0x08, 0x09, 0x6d, 0x65, 0x73, 0x73, 0x61, 0x67, 0x65 };
		ByteBuf buf = Unpooled.buffer(invalidProtocolEncodingArray.length);
		buf.writeBytes(invalidProtocolEncodingArray);
		Parser.decode(buf);
	}

	@Test(expected = MalformedMessageException.class)
	public void testInvalidContent()
	{
		WillMsg willMsg = new WillMsg(Unpooled.EMPTY_BUFFER);
		ByteBuf buf = Parser.encode(willMsg);
		Parser.decode(buf);
	}

	@Test
	public void testThreeOctetLength()
	{
		try
		{
			int size = 256;
			ByteBuf content = Unpooled.buffer(size);
			for (int i = 0; i < size; i++)
				content.writeByte(i);
			WillMsg expected = new WillMsg(content);
			assertEquals(4 + content.capacity(), expected.getLength());
			ByteBuf buf = Parser.encode(expected);
			WillMsg actual = (WillMsg) Parser.decode(buf);
			assertEquals(expected.getLength(), actual.getLength());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
}
