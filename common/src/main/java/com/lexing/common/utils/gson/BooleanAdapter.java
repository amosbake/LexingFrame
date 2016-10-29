package com.lexing.common.utils.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by Asura on 2016/1/8.
 * For Gson 反序列化的时候，针对bool类型，整数和字符串，0/1，"true","false"合法转化
 */
public class BooleanAdapter extends TypeAdapter<Boolean> {
    @Override
    public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value);
        }
    }

    @Override
    public Boolean read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        switch (peek) {
            case BOOLEAN:
                return in.nextBoolean();
            case NULL:
                in.nextNull();
                return null;
            case NUMBER:
                return in.nextInt() > 0;
            case STRING:
                String s = in.nextString();
                return "true".equalsIgnoreCase(s) || "1".equalsIgnoreCase(s);
            default:
                throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
        }
    }
}
