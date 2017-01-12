package com.mobius.software.mqttsn.parser.util;

import io.netty.buffer.ByteBuf;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ValuesValidator
{
	private static final Set<Integer> RESERVED_MESSAGE_IDS = new HashSet<Integer>(Arrays.asList(new Integer[]
	{ 0x0000 }));
	private static final Set<Integer> RESERVED_TOPIC_IDS = new HashSet<Integer>(Arrays.asList(new Integer[]
	{ 0x0000, 0xFFFF }));

	public static boolean validateMessageID(int messageID)
	{
		return messageID > 0 && !RESERVED_MESSAGE_IDS.contains(messageID);
	}

	public static boolean validateTopicID(int topicID)
	{
		return topicID > 0 && !RESERVED_TOPIC_IDS.contains(topicID);
	}

	public static boolean canRead(ByteBuf buf, int bytesLeft)
	{
		return buf.isReadable() && bytesLeft > 0;
	}

	public static boolean validateClientID(String clientID)
	{
		return clientID != null && !clientID.isEmpty();
	}
}
