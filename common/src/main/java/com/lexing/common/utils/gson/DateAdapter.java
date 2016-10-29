package com.lexing.common.utils.gson;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Asura on 2016/6/12.
 * 日期Adapter，可以设置时区，默认GsonBuilder没有提供设置时区的方法
 */
public class DateAdapter extends TypeAdapter<Date> {
    private final DateFormat dateFormat;

    public DateAdapter(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(dateFormat.format(value));
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String str = in.nextString();
        try {
            Date date = dateFormat.parse(str);
            return date;
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}
