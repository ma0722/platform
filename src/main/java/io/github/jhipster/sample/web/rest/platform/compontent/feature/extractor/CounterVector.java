package io.github.jhipster.sample.web.rest.platform.compontent.feature.extractor;

import io.github.jhipster.sample.web.rest.platform.compontent.Component;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.feature.CountVectorizer;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class CounterVector extends Component{

    private Tokenizer tokenizer = new Tokenizer();
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
