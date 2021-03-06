package io.github.jhipster.sample.web.rest.platform.compontent.feature.extractor;


import io.github.jhipster.sample.web.rest.platform.compontent.Component;
import io.github.jhipster.sample.web.rest.platform.util.SparkUtil;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.*;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;


public class TokenizerC extends Component {

    private StopRecognition filter = new StopRecognition();
    private String inputCol;

    public void run(){

        Dataset dataset = inputs.get("data").getDataset();
        Encoder<String> encoder = Encoders.STRING();
        Dataset data = dataset.map(new MapFunction <Row, String>() {
            public String call(Row row) {
                int index = row.fieldIndex(inputCol);
                String s = (String)row.get(index);
                Result result = ToAnalysis.parse(s).recognition(filter);
                StringBuffer sb = new StringBuffer();
                for (Term term: result.getTerms()) {
                    sb.append(term.getName());
                    sb.append(" ");
                }
                return sb.toString();
            }
        }, encoder);
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(data);
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("dictPath"))
            DicLibrary.put("dict", parameters.getJSONObject("dictPath").getString("value"));
        if(parameters.has("stopwordsPath"))
            StopLibrary.put("stop", parameters.getJSONObject("dicPath").getString("value"));
        if(parameters.has("inputCol"))
            this.inputCol = parameters.getJSONObject("inputCol").getString("value");
    }

    @Test
    public void test() throws Exception{



        SparkConf conf = new SparkConf().setAppName("data-platform").setMaster("local");
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();
        DataFrameReader reader =  spark.read().format("jdbc");

//        DataFrameReader reader = SparkUtil.spark.read().format("jdbc") ;

        String url = String.format("jdbc:mysql://%s:%d/%s", "10.109.247.63", 3306, "db_weibo");
        reader.option("url",url);
        reader.option("dbtable", "(SELECT weibo_content from weibo_original limit 30) as tmp");

        reader.option("driver","com.mysql.jdbc.Driver");
        reader.option("user", "root");
        reader.option("password", "hadoop");
        Dataset<Row> dataset = reader.load();
        dataset.show();

        this.inputCol = "weibo_content";
        Encoder<String> encoder = Encoders.STRING();

        Dataset data = dataset.map(new MapFunction <Row, String>() {
            public String call(Row row) {
                int index = row.fieldIndex(inputCol);
                String s = (String)row.get(index);
                Result result = ToAnalysis.parse(s);
                StringBuffer sb = new StringBuffer();
                for (Term term: result.getTerms()) {
                    sb.append(term.getName());
                    sb.append(" ");
                }
                return sb.toString();
            }
        }, encoder);

        data.show();

    }
}
