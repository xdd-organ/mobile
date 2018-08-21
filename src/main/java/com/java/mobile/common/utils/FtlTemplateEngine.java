//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.java.mobile.common.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component("ftlTemplateEngine")
public class FtlTemplateEngine {
    private static final String encoding = "UTF-8";
    private Configuration config;

    public FtlTemplateEngine() {
    }

    @PostConstruct
    public void init() throws IOException {
        String messageTemplateDir = this.getClass().getClassLoader().getResource("template").getPath().toString();
        File messageTemplateFileDir = new File(messageTemplateDir);
        this.config = new Configuration(Configuration.VERSION_2_3_0);
        if (messageTemplateFileDir.isDirectory()) {
            this.config.setDirectoryForTemplateLoading(messageTemplateFileDir);
        } else {
            this.config.setClassForTemplateLoading(this.getClass().getClassLoader().getClass(), messageTemplateDir);
        }

        this.config.setDefaultEncoding("UTF-8");
    }

    public Template getTemplate(String templateName) throws IOException {
        return this.config.getTemplate(templateName, "UTF-8");
    }

    public String genMessage(String templateName, Object dataModel) throws TemplateException, IOException {
        return this.process(templateName, dataModel);
    }

    public String process(String templateName, Object dataModel) throws TemplateException, IOException {
        Template template = this.getTemplate(templateName);
        StringWriter out = new StringWriter();
        template.process(dataModel, out);
        out.close();
        return out.toString();
    }
}
