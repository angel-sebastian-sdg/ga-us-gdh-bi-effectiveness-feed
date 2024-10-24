-- set spark.location.wh=gs://dev-bigdata-storage-raw-gl--<project>/
-- set spark.location.wh=gs://bstdl-raw-gl-<project>/
-- set spark.location.wh=gs://bspdl-raw-gl-<project>/



DROP TABLE if exists thebar_us_raw.project_raw;
CREATE TABLE thebar_us_raw.project_raw(
date string,
store_name string,
city string,
state string,
tdlinx string,
item_number string,
product_name string,
item_size string,
pricing string
)
USING com.databricks.spark.csv
OPTIONS (path '${spark.location.wh}distribution_insights_us/', header 'true', treatEmptyValuesAsNulls 'true' , delimiter ',');






