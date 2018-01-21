package io.github.jhipster.sample.web.rest.platform.compontent.data;

import io.github.jhipster.sample.web.rest.platform.compontent.Component;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;
import io.github.jhipster.sample.web.rest.platform.util.SparkUtil;


public class MysqlData extends Component {

    private String ip;
    private String username;
    private String password;
    private String database;
    private int port;
    private String sql;

    public void run() throws  Exception {
        DataFrameReader reader = SparkUtil.spark.read().format("jdbc");
        String url = String.format("jdbc:mysql://%s:%d/%s", ip, port, database);
        reader.option("url",url);
        reader.option("dbtable", sql);
        reader.option("driver","com.mysql.jdbc.Driver");
        reader.option("user", username);
        reader.option("password", password);

        Dataset<Row> dataset = reader.load();
        outputs.get("data").setDataset(dataset);
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("ip"))
            this.ip = parameters.getJSONObject("ip").getString("value");
        if(parameters.has("password"))
            this.password = parameters.getJSONObject("password").getString("value");
        if(parameters.has("username"))
            this.username = parameters.getJSONObject("username").getString("value");
        if(parameters.has("database"))
            this.database = parameters.getJSONObject("database").getString("value");
        if(parameters.has("port"))
            this.port = parameters.getJSONObject("port").getInt("value");
        if(parameters.has("sql"))
            this.sql = parameters.getJSONObject("sql").getString("value");
    }

}
