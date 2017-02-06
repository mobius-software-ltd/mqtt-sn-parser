# MQTT-SN parser

MQTT-SN parser is a library designed for encoding and decoding of MQTT-SN 1.2 packets. It is written in Java. MQTT-SN parser is developed by [Mobius Software](http://mobius-software.com).

## Getting Started

First you should clone MQTT-SN parser. Then you should add the following lines within the `<project>` element of pom.xml file of your project:

```
<dependency>
	<groupId>com.mobius-software.mqttsn</groupId>
	<artifactId>mqtt-sn-parser</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```
Now you are able to start using MQTT-SN parser.

## Example

### Create message, encode, decode

```
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

```
## [License](LICENSE.md)
