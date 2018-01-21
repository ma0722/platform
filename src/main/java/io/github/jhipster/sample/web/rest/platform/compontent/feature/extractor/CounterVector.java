package io.github.jhipster.sample.web.rest.platform.compontent.feature.extractor;

import io.github.jhipster.sample.web.rest.platform.compontent.Component;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;

public class CounterVector extends Component{


    private CountVectorizerModel model;
    private String[] vocabulary;

    public void run(){
        Dataset dataset = inputs.get("data").getDataset();
        Dataset data = model.transform(dataset);
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(data);
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("vocabulary")) {
            vocabulary = parameters.getJSONObject("vocabulary").getString("value").split(" ");
            model = new CountVectorizerModel(vocabulary);
        }
        if(parameters.has("inputCol"))
            model.setInputCol(parameters.getJSONObject("inputCol").getString("value"));
        if(parameters.has("outputCol"))
            model.setOutputCol(parameters.getJSONObject("outputCol").getString("value"));
    }

}
