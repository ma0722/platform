package io.github.jhipster.sample.web.rest.platform.util;


import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;

public class SparkUtil {

    public static SparkSession spark;

    static {
        try{
            Properties properties = new Properties();
            properties.load(SparkUtil.class.getResourceAsStream("/cluster.properties"));
            String master = properties.getProperty("spark_master_ip");
            String port = properties.getProperty("spark_port");
            SparkConf conf = new SparkConf().setAppName("data-platform").setMaster("spark://" + master + ":" + port);
            spark = SparkSession.builder().config(conf).getOrCreate();
            spark.sparkContext().addJar("jars/mysql-connector-java-5.1.46.jar");
            spark.sparkContext().addJar("jars/spark-avro_2.11-3.1.0.jar");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Dataset<Row> readFromHDFS(String path, String dataFormat) throws Exception {
        if(!HDFSFileUtil.checkFile(path))
            throw new FileNotFoundException(path + "not found on hadoop!");
        return spark.read().format(dataFormat).load(HDFSFileUtil.HDFSPath(path));
    }

    public static Dataset<Row> readFromLocal(String path, String dataFormat) throws Exception{
        if (!new File(path).exists())
            throw new FileNotFoundException(path + " not found on local!");
        return spark.read().format(dataFormat).load(path);
    }

    public static Dataset<Row> readFromSQL(String sql) {
        return spark.sql(sql);
    }


}
