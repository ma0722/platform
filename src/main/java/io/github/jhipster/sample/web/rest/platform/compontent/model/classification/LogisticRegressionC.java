package io.github.jhipster.sample.web.rest.platform.compontent.model.classification;

import io.github.jhipster.sample.web.rest.platform.compontent.Component;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class LogisticRegressionC extends Component {

    private LogisticRegression model = new LogisticRegression();
    private LogisticRegressionModel model_;


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
        if(parameters.has("maxIter"))
            model.setMaxIter(parameters.getJSONObject("maxIter").getInt("value"));
        if(parameters.has("reg"))
            model.setRegParam(parameters.getJSONObject("reg").getDouble("value"));
        if(parameters.has("elasticNet"))
            model.setElasticNetParam(parameters.getJSONObject("elasticNet").getDouble("value"));
        if(parameters.has("tol"))
            model.setTol(parameters.getJSONObject("tol").getDouble("value"));
        if(parameters.has("features"))
            model.setFeaturesCol(parameters.getJSONObject("features").getString("value"));
        if(parameters.has("label"))
            model.setFeaturesCol(parameters.getJSONObject("label").getString("value"));
        if(parameters.has("savePath"))
            this.path = parameters.getJSONObject("savePath").getString("value");
    }

    public void save() throws IOException {
        model_.save(path);
    }

}
