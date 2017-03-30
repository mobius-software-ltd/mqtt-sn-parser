package com.mobius.software.mqttsn.parser.avps;

/**
 * Mobius Software LTD
 * Copyright 2015-2016, Mobius Software LTD
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

import java.util.HashMap;
import java.util.Map;

import com.mobius.software.mqttsn.parser.exceptions.MalformedMessageException;

public enum SNQoS
{
	AT_MOST_ONCE((byte) 0), AT_LEAST_ONCE((byte) 1), EXACTLY_ONCE((byte) 2), LEVEL_ONE((byte) 3);

	private byte value;

	private static final Map<Integer, SNQoS> intToTypeMap = new HashMap<Integer, SNQoS>();
	private static final Map<String, SNQoS> strToTypeMap = new HashMap<String, SNQoS>();

	static
	{
		for (SNQoS type : SNQoS.values())
		{
			intToTypeMap.put((int) type.value, type);
			strToTypeMap.put(type.name(), type);
		}
	}

	public int getValue()
	{
		return value;
	}

	private SNQoS(final byte leg)
	{
		value = leg;
	}

	public static SNQoS valueOf(int type) throws MalformedMessageException
	{
		return intToTypeMap.get(type);
	}

	public static SNQoS calculate(final SNQoS subscriberQos, final SNQoS publisherQos)
	{
		if (subscriberQos.getValue() == publisherQos.getValue())
			return subscriberQos;

		if (subscriberQos.getValue() > publisherQos.getValue())
			return publisherQos;
		else
			return subscriberQos;
	}
}
