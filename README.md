# ga-us-gdh-sgws-feed

## Introduction

This repo has been created for storing the code to populate the GDH US SGWS data model

## Use

Launch GdhLoadSgws with some of this params:

GdhLoadSgws -all| -harmonization| -account| -address| -product_additional| -product| -store_additional| -store| -sales_navigator_daily| -validation <business_date>?


## Installation

Before install, you have to create a file named ".credentials" inside the ivy2 directory. In this file, you have to create this information:

realm=Sonatype Nexus Repository Manager<br/>
host=104.154.138.29<br/>
user=beam-nexus<br/>
password=Nexus@321<br/>

This credentials are used for download our libraries.

Usual Commands:

- sbt clean
- sbt compile
- sbt test
- sbt publishLocal (for using in a local Machine)
- sbt publish (must be done by jenkins)


## Log version

    Features:
        1.0.0-00:
    	    Initial Code