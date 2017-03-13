*****How To Build***********

1. The build requires Maven (>=3.3) and java (>1.7)
2. go to the xstampp.parent directory in the root path of the xstampp project (where this file is located)
3. open a command in the xstampp.parent dir and execute 
	3.1 'mvn clean verify' to build xstampp with xstpa and cast already included
	3.2 'mvn clean install' as 3.1 but also installs xstampp on [user]/.m2 for usage as local dependency of other builds
4. the build artifacts are located in the astpa.repository/target	