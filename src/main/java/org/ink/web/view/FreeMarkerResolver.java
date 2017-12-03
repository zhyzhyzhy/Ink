package org.ink.web.view;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Map;

public class FreeMarkerResolver implements TemplateResolver {

    public RandomAccessFile resolve(String name, Map<String, Object> model) throws Exception {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        configuration.setDirectoryForTemplateLoading(new File("/Users/zhuyichen/git/noname/ink-examples/example1/target/classes/template"));
        Template template = configuration.getTemplate(name);

        File file = new File(template.getName());
        PrintWriter writer = new PrintWriter(file);
        template.process(model, writer);
        return new RandomAccessFile(file, "r");
    }
}
