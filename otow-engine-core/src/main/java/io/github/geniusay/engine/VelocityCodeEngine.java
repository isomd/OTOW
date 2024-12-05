package io.github.geniusay.engine;

import io.github.geniusay.template.VelocityOTOWTemplate;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Properties;

/**
 * velocity工厂
 */
public class VelocityCodeEngine extends Engine<VelocityOTOWTemplate> {

    private static Properties props;

    private VelocityEngine engine;

    static{
        props = new Properties();
        String resourcePath = Objects.requireNonNull(VelocityCodeEngine.class.getClassLoader().getResource("template")).getPath();
        props.setProperty("file.resource.loader.path", resourcePath); // 设置模板路径
        props.setProperty("input.encoding", "UTF-8"); // 设置输入文件的编码
        props.setProperty("output.encoding", "UTF-8"); // 设置输出文件的编码
    }

    public VelocityEngine velocityEngine(){
        if(engine==null){
            engine = new VelocityEngine(props);
        }
        return engine;
    }

    @Override
    public void generate(VelocityOTOWTemplate template) {
        VelocityEngine engine = velocityEngine();
        Template velocityTemplate = engine.getTemplate(template.getTemplateFilePath());
        VelocityContext context = template.getContext();
        StringWriter writer = new StringWriter();
        velocityTemplate.merge(context, writer);

        Path filePath = Path.of(template.getOutputDir()); // 指定文件路径
        try {
            // 写入文件，如果文件存在则覆盖
            Files.write(filePath, writer.toString().getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
        }
    }
}