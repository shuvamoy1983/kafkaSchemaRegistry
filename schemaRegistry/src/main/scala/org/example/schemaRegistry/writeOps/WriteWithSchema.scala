package org.example.schemaRegistry.writeOps

import org.example.schemaRegistry.config.SparkEngine
import org.example.schemaRegistry.utilities.Constants
import org.example.schemaRegistry.utilities.utils
import za.co.absa.abris.avro._

object WriteWithSchema extends SparkEngine{

    val sampleData = spark.read.format("csv").option("header","true").option("inferSchema","true").load(Constants.sampleDataFile)
     sampleData.show(2)
    //getting the configurations for schema registry
    val schemaRegistryConfig = utils.getSchemaRegistryConfig()

    //writing the sample data to the kafka using avro schema registry
    utils.writeAvro(sampleData,schemaRegistryConfig)

}
