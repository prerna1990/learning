package com.github.test.game.customizer;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
public class DateSeralizeCustomizer extends JsonSerializer<Timestamp> {
    private SimpleDateFormat formatter =
            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private SimpleDateFormat outputFormatter =
            new SimpleDateFormat("dd-MM-yyyy");
    @Override
    public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value != null){
            System.out.println("Value is : " + value);
            try {
                Date cc = formatter.parse(value.toString());
                String content = outputFormatter.format(cc.getTime());
                gen.writeString(content);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
