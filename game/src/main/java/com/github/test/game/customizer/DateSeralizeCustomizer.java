package com.github.test.game.customizer;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DateSeralizeCustomizer extends JsonSerializer<Timestamp> {
	private SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (value != null) {
			System.out.println("Value is : " + value);
			// Date cc = formatter.parse(value.toString());
			String content = outputFormatter.format(value.getTime());
			gen.writeString(content);
		}
	}
}
