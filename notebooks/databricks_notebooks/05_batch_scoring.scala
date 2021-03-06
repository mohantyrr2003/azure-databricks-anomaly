// Databricks notebook source
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.ml.classification._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

// COMMAND ----------

display(spark.catalog.listTables())

// COMMAND ----------

display(dbutils.fs.ls("/mnt/blob_storage/models"))

// COMMAND ----------

// Load data
// In production, you may need to filter since last run
val df = spark.read.table("kdd_unlabeled")

// Clean data
val cleanDf = df.na.drop() // For production, may need to save this to another table, or impute null values

// Load model
val modelLoc = "/mnt/blob_storage/models/RandomForestPipeline"
val model = PipelineModel.load(modelLoc)

// Make predictions
val predictions = model.transform(cleanDf)

// Save data
predictions.write.mode("append").saveAsTable("kdd_predictions")