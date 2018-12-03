package cn.mklaus.plugin.velocity;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author klaus
 * @date 2018/11/29 11:36 AM
 */
public class VelocityUtils {

    public static String generate(String template, Map<String, Object> map) {
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty(VelocityEngine.INPUT_ENCODING, "utf-8");
        engine.setProperty(VelocityEngine.OUTPUT_ENCODING, "utf-8");

        VelocityContext context = new VelocityContext();
        if (map != null) {
            map.forEach(context::put);
        }

        StringWriter stringWriter = new StringWriter();
        try {
            engine.evaluate(context, stringWriter, "VM Generate", template);
        } catch (Exception e) {
            StringBuilder builder = new StringBuilder("生成模板代码出错：\n");
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            builder.append(writer.toString());
            return builder.toString();
        }

        return stringWriter.toString();
    }

}
