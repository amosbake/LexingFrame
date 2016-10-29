package com.lexing.common.utils.gson;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * int-json类型转换器，将"","null"转换成0
 */
public class IntTypeAdapter extends TypeAdapter<Integer> {


    @Override
    public void write(JsonWriter out, Integer value) throws IOException {
        out.value(value);
    }

    @Override
    public Integer read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if(peek == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        if(peek == JsonToken.STRING){
            String str = in.nextString();
            if("".equals(str) || "null".equals(str)){
                return 0;
            } else {
                return Integer.parseInt(str);
            }
        }
        try {
            return in.nextInt();
        } catch (NumberFormatException e) {
            throw new JsonSyntaxException(e);
        }
    }
}
