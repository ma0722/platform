package io.github.jhipster.sample.web.rest;

import io.github.jhipster.sample.web.rest.platform.Base.Graph;
import io.github.jhipster.sample.web.rest.platform.util.JSONUtil;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")

public class PlatformController {

    @GetMapping("/component")
    @ResponseBody
    public String getComponentInfo() {
        JSONObject componets = JSONUtil.jsonRead("src/main/resources/component.json");
        return componets.toString();
    }

    @GetMapping("/run")
    @ResponseBody
    public String run(@RequestParam("nodes") String nodes,
                      @RequestParam("links") String links,
                      @RequestParam("project_name") String project_name,
                      @RequestParam("project_description") String project_description,
                      @RequestParam("project_version") String project_version) throws Exception{


        JSONObject componets = new JSONObject(nodes);
        JSONObject edges = new JSONObject(links);

        Graph graph = new Graph(componets, edges);
        graph.run();
        return "success";
    }

}
