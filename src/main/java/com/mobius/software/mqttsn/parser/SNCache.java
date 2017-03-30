package com.mobius.software.mqttsn.parser;

import java.awt.TrayIcon.MessageType;

import com.mobius.software.mqttsn.parser.packet.api.SNMessage;

public interface SNCache
{
	SNMessage borrowMessage(MessageType type);

	void returnMessage(SNMessage message);
}
