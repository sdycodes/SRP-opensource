mvn clean package -f warehouse/pom.xml
datasets=("synA" "synB")
methods=("SRP" "ACP" "SAP")
mem_set=20480
group=$1
total=$2
echo "group: $group out of $total"
number=0
for dataset in ${datasets[*]};
do
   for method in ${methods[*]};
   do
      number=$(($number+1))
       if [ $(($number%$total+1)) -eq $(($group)) ]; then
          java  -Xmx${mem_set}m -Xms${mem_set}m -jar warehouse/target/SmartWarehouse-1.0-SNAPSHOT.jar ${method} ${dataset} > logs/${method}_${dataset}.log
        fi
   done
done
