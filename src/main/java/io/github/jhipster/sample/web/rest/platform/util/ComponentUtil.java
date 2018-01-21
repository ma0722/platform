package io.github.jhipster.sample.web.rest.platform.util;

import io.github.jhipster.sample.web.rest.platform.compontent.Component;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;


public class ComponentUtil {

    static private HashMap<String, String> componentPath;

    static {
        componentPath = new HashMap<String, String>();
        try{

            JSONObject componentInfo = JSONUtil.jsonRead("src/main/resources/component.json");
            Iterator iterator = componentInfo.keys();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                String value = componentInfo.getJSONObject(key).getString("class");
                componentPath.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public Component generateComponent(String componentName) throws Exception{
        if(!componentPath.containsKey(componentName))
            return null;
        String name = componentPath.get(componentName);
        Class clazz = Class.forName(name);
        Constructor constructor = clazz.getConstructor();
        return (Component) constructor.newInstance();
    }


}
