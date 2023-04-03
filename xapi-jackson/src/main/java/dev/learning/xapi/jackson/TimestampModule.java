package dev.learning.xapi.jackson;

import java.time.Instant;

import com.fasterxml.jackson.databind.cfg.PackageVersion;
import com.fasterxml.jackson.databind.module.SimpleModule;

import dev.learning.xapi.jackson.deserializers.TimestampDeserializer;

public class TimestampModule extends SimpleModule{

	private static final long serialVersionUID = 6639258524169172173L;

	public TimestampModule() {

		super(PackageVersion.VERSION);

	    addDeserializer(Instant.class, new TimestampDeserializer());

	}
	
}
