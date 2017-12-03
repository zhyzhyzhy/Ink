package org.ink.web.view;

import java.io.RandomAccessFile;
import java.util.Map;

public interface TemplateResolver {

    RandomAccessFile resolve(String name, Model model) throws Exception;
}
