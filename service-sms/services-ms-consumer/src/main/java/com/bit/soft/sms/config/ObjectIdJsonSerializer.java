package com.bit.soft.sms.config;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @Description:
 * @Author: liyujun
 * @Date: 2020-06-01
 **/
public class ObjectIdJsonSerializer implements ObjectSerializer {


    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,int features) throws IOException {
        SerializeWriter out = serializer.getWriter();
        if (object == null) {
            serializer.getWriter().writeNull();
            return;
        }
        out.write("\"" + ((ObjectId) object).toString() + "\"");
    }
}
