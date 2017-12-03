package org.ink.web.view;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.ink.WebConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class FreeMarkerResolver implements TemplateResolver {

    private final Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
    private static final Logger logger = LoggerFactory.getLogger(FreeMarkerResolver.class);

    public FreeMarkerResolver() {
        try {
            cfg.setDirectoryForTemplateLoading(new File(WebConfig.PATH + "template"));
        } catch (Exception e) {
            logger.info("template path {} not found", WebConfig.PATH+"/template");
        }
    }

    public RandomAccessFile resolve(String name, Model model) throws Exception {
        Template template = cfg.getTemplate(name);
        File file = new File(template.getName());
        PrintWriter writer = new PrintWriter(file);
        template.process(model.getModel(), writer);
        return new RandomAccessFile(file, "r");
    }

}
