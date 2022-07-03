# Content
- directory data/ stores all data as well as scripts for refine real data and generate synthesized datasets.
- directorty logs/ stores all logs each execution of a method on a certain dataset.
- directoy warehouse contains codes of simulator and methods. methods are in java package Solvers.

We are not authorized to opensource the real data, so please change to directory ./data/ and run the script ./gen\_data.sh to get sythesized data.

# How to Run
After generate data, set the data directory at ./warehouse/src/main/java/UserConfig/Configurations.java
```java
public static final String ROOT_PATH = "/path/to/data";
```

If you want to run a single test, input below instructions in bash
```bash
$ mvn clean package
$ java  -Xmx20480m -Xms20480m -jar warehouse/target/SmartWarehouse-1.0-SNAPSHOT.jar <method> <dataset> > /path/to/logs/<method>_<dataset>.log
```

where \<metod\> and \<dataset\> specifies the method and dataset.

\<method\> can be "SRP", "SAP" and "ACP", while \<dataset\> can be "synA", "synB".

If you want to run methods batchfully, you may use run.sh.
```bash
$./run.sh a b
```
where b is number of group and a is the number of one single group.

For example, if you divided all tasks in 3 groups, you may run 

```bash
$./run.sh 1 3
$./run.sh 2 3
$./run.sh 3 3
```

This may help better exploit the CPU computation resources.

If you do not want to divide, simply use 
```bash
$./run.sh 1 1
```
results will be in directory ./log