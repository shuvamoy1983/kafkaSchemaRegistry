package org.example.schemaRegistry.writeOps

import org.example.schemaRegistry.config.SparkEngine
import org.example.schemaRegistry.utilities.Constants
import org.example.schemaRegistry.utilities.utils.getSchemaRegistryConfig
import za.co.absa.abris.avro.functions.{to_avro,from_avro}
import org.apache.spark.sql.functions.col

object ReadWithSchema extends SparkEngine{

  val input = spark.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", Constants.kafka_bootstrap_server)
    .option("subscribe", Constants.kafka_topic)
    .option("startingOffsets", "latest")
    .option("failOnDataLoss", "false")
    .load()

  println(input)
  val schemaRegConfig = getSchemaRegistryConfig()

  import spark.implicits._

  val outputDf = spark
    .readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", Constants.kafka_bootstrap_server)
    .option("subscribe", Constants.kafka_topic)
    .option("startingOffsets", "latest")
    .load()
    .selectExpr("CAST(key as STRING)", "value")
    .select(from_avro(col("value"),schemaRegConfig) as "data")
    .select("data.*")

  outputDf.writeStream.format("console").start().awaitTermination()

}
