package example;

import static org.junit.Assert.fail;
import io.netty.buffer.ByteBuf;

import org.junit.Test;

import com.mobius.software.mqttsn.parser.Parser;
import com.mobius.software.mqttsn.parser.packet.api.SNMessage;
import com.mobius.software.mqttsn.parser.packet.impl.Connect;
import com.mobius.software.mqttsn.parser.tests.util.Assertion;

public class ExampleTest
{
	@Test
	public void testEncodeMessage()
	{
		try
		{
			// Create message
			boolean cleanSession = true;
			int duration = 10;
			String clientID = "example_client";
			boolean willPresent = false;
			Connect message = new Connect(cleanSession, duration, clientID, willPresent);

			// Encode
			ByteBuf buf = Parser.encode(message);

			// Decode
			SNMessage decoded = Parser.decode(buf);

			Assertion.assertMessage(message, decoded);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
}
