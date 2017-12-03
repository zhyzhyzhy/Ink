package org.ink.web.view;


import java.util.HashMap;
import java.util.Map;

/**
 * for mvc
 */
public class Model {

    private Map<String, Object> model = new HashMap<>();

    public Model() {

    }

    public Model(Model model) {

    }


    public void attr(String key, Object value) {
        model.put(key, value);
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
