package io.github.jhipster.sample.web.rest.platform.compontent.model.classification;

import io.github.jhipster.sample.web.rest.platform.compontent.Component;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class DecisionTreeC extends Component {

    private DecisionTreeClassifier model = new DecisionTreeClassifier();
    private DecisionTreeClassificationModel model_;


    private String path;

    public void run() throws Exception {
        Dataset dataset = inputs.get("data").getDataset();
        model_ = model.fit(dataset);
        if(outputs.containsKey("model"))
            outputs.get("model").setModel(model_);
        if(path != null && path.equals(""))
            save();
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("maxDepth"))
            model.setMaxDepth(parameters.getJSONObject("maxDepth").getInt("value"));
        if(parameters.has("maxBins"))
            model.setMaxBins(parameters.getJSONObject("maxBins").getInt("value"));
        if(parameters.has("MinInstancesPerNode"))
            model.setMinInstancesPerNode(parameters.getJSONObject("MinInstancesPerNode").getInt("value"));
        if(parameters.has("minInfoGain"))
            model.setMinInfoGain(parameters.getJSONObject("minInfoGain").getDouble("value"));
        if(parameters.has("cacheNodeIds"))
            model.setCacheNodeIds(parameters.getJSONObject("cacheNodeIds").getBoolean("value"));
        if(parameters.has("checkpointInterval"))
            model.setCheckpointInterval(parameters.getJSONObject("checkpointInterval").getInt("value"));
        if(parameters.has("impurity"))
            model.setImpurity(parameters.getJSONObject("impurity").getString("value"));
         if(parameters.has("savePath"))
            this.path = parameters.getJSONObject("savePath").getString("value");
    }

    public void save() throws IOException {
        model_.save(path);
    }

}
