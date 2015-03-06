[Storage_Local]
village.txt

[Storage_HDFS]
/user/casper/firemap/input/input.txt
/user/casper/firemap/village.txt

[Command-Line]
hadoop jar firemap.jar /user/casper/firemap/input /user/casper/firemap/output -villageLocal /home/casper/firemap/village.txt -villageDFS /user/casper/firemap/village.txt