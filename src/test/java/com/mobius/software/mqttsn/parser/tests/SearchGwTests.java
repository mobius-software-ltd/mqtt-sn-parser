package com.mobius.software.mqttsn.parser.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import org.junit.BeforeClass;
import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.avps.SNType;
import com.mobius.software.mqttsn.parser.packet.impl.SearchGW;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class SearchGwTests
{
	private static final int RADIUS = 2;
	private static SearchGW message;

	@BeforeClass
	public static void beforeClass()
	{
		message = new SearchGW(RADIUS);
	}

	@Test
	public void testType()
	{
		try
		{
			assertEquals(SNType.SEARCHGW, message.getType());
			assertEquals(1, message.getType().getValue());
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
			SearchGW searchGw = new SearchGW(RADIUS);
			ByteBuf expected = Parser.encode(message);
			ByteBuf actual = Parser.encode(searchGw);
			assertTrue(ByteBufUtil.equals(expected, actual));
			searchGw = (SearchGW) Parser.decode(actual);
			Assertion.assertSearchGw(message, searchGw);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
}
