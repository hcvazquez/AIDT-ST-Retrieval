[![logo](https://github.com/hcvazquez/AIDT-ST-Retrieval/blob/master/img/customLogo.png?raw=true)](http://www.isistan.unicen.edu.ar/)

# ST-Retrieval
The retrieval component of AIDT

![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)

## Overview
ST-Retrieval proposes a meta-search approach that automates the process of retrieving software technologies for a specific design or development need. This approach, called ST-Retrieval, is based on the extraction, filtering and aggregation of software technologies returned by various existing search engines. 

![logo](https://github.com/hcvazquez/AIDT-ST-Retrieval/blob/master/img/Enfoque-ST-Retrieval.png?raw=true)


### Installation

ST-Retrieval requires the Java Development Kit [1.8](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)+ to run.

Add the "/lib" folder to the classpath.

Download the NPM repository from the [public registry](https://skimdb.npmjs.com/registry/_design/scratch/_view/byField)

In the **config.properties** file, set the next properties:

| Property | Value |
| ------ | ------ |
|npm_data_home| with the path to the NPM repository data|
|content_folder| with the path to the folder where the downloaded web pages will be stored|
|ranking_folder| with the path to the folder where the result ranking will be stored|

Then, run the next task
```sh
run src/task/AcquireData.main(args);
run src/task/ProcessData.main(args);
run src/task/AggregateData.main(args);
```

