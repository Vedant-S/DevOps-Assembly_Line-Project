# Linux World DevOps Training - Assignment
----------------------------------

### Prerequisites:

- YOU HAVE TO SET THE BUID TRIGGERS AND WEBHOOK AND DO CONFIGURATIONS IN JENKINS WHILE BUILDING JOBS.
- U SHOULD HAVE KNOWLEDGE ABOUT JENKINS CHAINING, HOW TO USE GITHUB PLUGIN TO INTEGRATE GITHUB, WEBHOOKS, SOME SHELL SCRIPTING KNOWLEDGE(LINUX COMMANDS), DOCKER KNOWLEDGE, GIT HOOKS.

# The Project:-
----------------------------------

### Python Code:

```
import os
all_files=os.listdir("/storage")

all_htmls=[]
all_phps=[]

for file in all_files:
    if os.path.splitext(file)[1] ==".html" or os.path.splitext(file)[1]==".js":
        all_htmls.append(file)
    if os.path.splitext(file)[1] ==".php":
        all_phps.append(file)

if(len(all_htmls)==0 and len(all_phps)==0):
    os.system("tput setaf 3")
    print("NO FILE TO COPY")
    os.system("exit 0")
if len(all_htmls)>0:
    os.system("ssh root@192.168.29.62 rm -rf /htmltest_storatie/")
    os.system("ssh root@192.168.29.62 mkdir /htmltest_storage/")
    for htm in all_htmls:
        os.system(f"scp {htm}  192.168.29.62:/htmltest_storage")
if len(all_phps)>0:
    os.system("ssh root@192.168.29.62 rm -rf /phptest_storage/")
    os.system("ssh root@192.168.29.62 mkdir /phptest_storage/")
    for php in all_phps:
        os.system(f"scp {php}  192.168.29.62:/phptest_storage")


print("TOTAL FILES COPIED:",len(all_files))

print("TOTAL PHP FILES  :",len(all_phps))
print("TOTAL HTML+JS FILES  :",len(all_htmls))
print("OTHER FILES:",len(all_files)-len(all_phps)-len(all_htmls))
```
_________________________________________________________________________________________________________

### Mail.rc Code:

```
set smtp=smtps://smtp.gmail.com:465
set smtp-auth=login
set smtp-auth-user=YOUR@EMAIL
set smtp-auth-password=YOURPASSWORD
set ssl-verify=ignore
set nss-config-dir=/etc/pki/nssdb/
```
_________________________________________________________________________________________________________

# Step-1:
Create container image that’s has Jenkins installed using dockerfile.

### Dockerfile Code & Description:

```
FROM centos:latest
RUN yum install sudo -y
RUN yum install python3 -y
RUN yum install mailx -y
RUN yum install net-tools -y
RUN yum install wget -y
RUN yum install java-11-openjdk.x86_64 -y
RUN wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
RUN rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
RUN yum install openssh-clients -y
RUN yum install sudo -y
RUN yum install jenkins -y
RUN yum install /sbin/service -y
RUN  mkdir /workdir
COPY ./check_forext.py /workdir/
RUN mkdir /storage/
COPY  ./mail.rc  /etc/mail.rc
RUN echo -e "jenkins ALL(=ALL) NOPASSWD: ALL" >> /etc/sudoers
CMD java -jar /usr/lib/jenkins/jenkins.war
EXPOSE 8080

#FROM is used for the image to be used

#RUN is used for executing command while building the new image, so that features will be pre installed

#CMD  is used to run a=command at the run time not at the build time and only one CMD should be there in one Dockerfile

#Expose is used to expose the docker port as docker is isolated we have to enable Port Address Translation
#and is also used to inform  that which port is exposed to the other techonolgies or tools or commands.

#COPY is used to copy the file from the base os to image at build time.

#IN THE DIRECTORY WHERE YOU HAVE COPIED this,  THE mail.rc and  check_forext.py should be copied there only as per my defined Dockerfile

# TO BUILD THE IMAGE USE THE COMMAND :

#docker build -t imagename:version  folder where the Dockerfile u have copied.

#and for running the conatiner from this image use command:

#docker run -it -P --name containername imagename:version

#U CAN ACCESS THE JENKINS FROM
#base os using BASEDOCKERHOSTIP:PORT MAPPED.
#u can see the port number to which 8080 port of the container is mapped using netstat -tnlp
```
_________________________________________________________________________________________________________

# Step-2:
When we launch this image, it should automatically starts Jenkins service in the container.
_________________________________________________________________________________________________________

# Step-3:
Create a job chain of job1, job2, job3, job4 and job5 using build pipeline plugin in Jenkins.
_________________________________________________________________________________________________________

### Job-1 Description:

- To Pull the Github repo automatically when some developers push repo to Github.

```
#POST-COMMIT HOOK

#!/bin/bash

git push
```
- FIRST OF ALL NEED TO CONFIGURE JENKINS i.e. Putting the initial password , and
- enbale KEY-BASED AUTHENTICATION with Remote host
- ssh-keygen  (on ssh client side i.e.  our jenkin container)
- and copy this key to remote host ie in case our base os where docker is installed
- ssh-copy-id root@IP   (IP of ur baseos)  
- INORDER  TO PREVENT FROM ENTERING  THE PASSWORD AGAIN AND AGAIN FOR REMOTE LOGIN.

- install Github , BUILD PIPELINE, DELIVERY PIPELINE And GIT PULL REQUEST BUILDER plugins in JENKIN.
- One should have some knowledge of JENKINS , GITHUB AND GIT AND DOCKER.

### JENKINS JOB 1 SCRIPT:

```
rm -rvf /storage/
mkdir /storage/
cp -rvf * /storage/

python3 /workdir/check_forext.py
```
_________________________________________________________________________________________________________

### Job-2 Description:

By looking at the code or program file,

- Jenkins should automatically start the respective language interpreter install image container to deploy code.
- eg. If code is of  PHP, then Jenkins should start the container that has PHP already installed.
- we can also use "file -s filename" to check for the content of the file also but i have created it for html js and php pages using different approach.

### JENKINS JOB 2 SCRIPT:

```
isempty_html=0
isempty_php=0                                                               #FIRST OF ALL NEED TO CREATE TWO DIRECTORIES  /htmltest_storage
if [[ $(ssh root@1IP  ls -A /htmltest_storage) ]]                            # and /phptest_storage
                                                                                #IP ==> BASE OS WHERE DOCKER IS INSTALLED
then
if ssh root@IP docker ps | grep test_html
then
ssh root@IP docker rm -f test_html
ssh root@IP docker run -dit -v /htmltest_storage:/usr/local/apache2/htdocs/ -p 8086:80 --name test_html httpd    #I HAVE USED httpd official image for html and js page deployment
else
ssh root@IP docker run -dit -v /htmltest_storage:/usr/local/apache2/htdocs/ -p 8086:80 --name test_html httpd
fi
else
if ssh root@IP docker ps | grep test_html
then
ssh root@IP docker rm -f test_html
echo EMPTY HTML FOLDER
fi
isempty_html=1
fi

if [[ $(ssh root@IP ls -A /phptest_storage) ]]      # to check for if directory is empty or not
then
if ssh root@IP docker ps | grep test_php
then
ssh root@IP docker rm -f test_php
ssh root@IP docker run -dit -v /phptest_storage:/var/www/html/ -p 8085:80 --name test_php vimal13/apache-webserver-php   #I HAVE USED docker image created by Vimal sir for php pages deployment
else
ssh root@IP docker run -dit -v /phptest_storage:/var/www/html/ -p 8085:80 --name test_php vimal13/apache-webserver-php
fi
else
if ssh root@IP docker ps | grep test_php
then
ssh root@IP docker rm -f test_php
echo EMPTY PHP FOLDER
fi
isempty_php=1
fi

if (( $isempty_html == 1 && $isempty_php == 1 ))
then

echo BOTH FOLDERS EMPTY
exit 1
fi
```
_________________________________________________________________________________________________________

### Job 3 & 4 Description:

- I HAVE COMBINED BOTH THE JOBS so that AFTER testing the Deployment is done if no issues.

- Job3 : Test your app if it  is working or not.
- Job4 : if app is not working , then send email to developer with error messages.

- test_php and test_html are testing env.
- prod_php and prod_html are production env.
- I used mailx to send the mail i have done all the configurations in jenkins server regarding the mailx.

```
no_html=0
no_php=0

if ssh root@IP docker ps | grep test_html
then
for files in $(ssh root@IP ls -A /htmltest_storage);
do status=$(curl -o /dev/null -w "%{http_code}" -s IP:8086/${files}) ;
if [[ $status == 200 ]];
then echo -e SUCCESS CODE:  $status  "  "   FILENAME: $files  | mail -v -s "TESTING SUCCESS" RECEIVER-EMAILID  ;
else echo ERROR CODE: $status FILENAME: $files  | mail -v -s "TESTING FAILED" RECEIVER-EMAILID;no_html=1  
ssh root@IP docker rm -f test_html
fi
done
else
no_html=1
fi

if ssh root@IP docker ps | grep test_php
then
for file in $(ssh root@IP ls -A /phptest_storage);
do status=$(curl -o /dev/null -w "%{http_code}" -s IP:8085/${file}) ;
if [[ $status == 200 ]];
then echo -e SUCCESS CODE: $status "   " FILENAME: $file  | mail -v -s "TESTING SUCCESS" RECEIVER-EMAILID  ;
else
echo ERROR CODE: $status FILENAME: $file  | mail -v -s "TESTING FAILED" RECEIVER-EMAILID; no_php=1  ;
ssh root@IP docker rm -f test_php
fi
done
else
no_php=1
fi

if (( $no_php == 1 && $no_html == 1 ))
then
echo "All codes failed testing"  | mail -v -s "TESTING FAILED" RECEIVER-EMAILID
exit 1
else
if (( ( $no_php == 0 && $no_html == 0 )  || ( $no_php == 1 && $no_html == 0 ) || ( $no_php == 0 && $no_html == 1) ))
then
if (( $no_php == 0 ))
then
if ssh root@IP docker ps | grep test_php
then
if ssh root@IP docker ps | grep prod_php
then
ssh root@IP docker rm -f prod_php
ssh root@IP docker run -dit -v /phptest_storage:/var/www/html/ -p 8082:80 --name prod_php vimal13/apache-webserver-php
else
ssh root@IP docker run -dit -v /phptest_storage:/var/www/html/ -p 8082:80 --name prod_php vimal13/apache-webserver-php
fi
fi
fi
if (( $no_html == 0 ))
then
if ssh root@IP docker ps | grep test_html
then
if ssh root@IP docker ps | grep prod_html
then
ssh root@IP docker rm -f prod_html
ssh root@IP docker run -dit -v /htmltest_storage:/usr/local/apache2/htdocs/ -p 8081:80 --name prod_html httpd
else
ssh root@IP docker run -dit -v /htmltest_storage:/usr/local/apache2/htdocs/ -p 8081:80 --name prod_html httpd
fi
fi
fi
fi
fi
```
_________________________________________________________________________________________________________

### Job-5 Description: Job to monitor.

- If container where app is running. fails due to any reason then this job should automatically start the container again.

```
if ssh root@IP docker ps | grep test_php
then
if ssh root@IP docker ps | grep prod_php
then
echo "PHP SERVER UP AND RUNNING"
else
echo PHP Production Server Down  | mail -v -s "RELAUNCHING PHP SERVER"  RECEIVER-EMAILID
ssh root@IP docker run -dit -v /phptest_storage:/var/www/html/ -p 8082:80 --name prod_php httpd
fi
else

echo PHP Test Server Down  | mail -v -s " PHP SERVER DOWN"  RECEIVER-EMAILID
fi

if ssh root@IP docker ps | grep test_html
then
if ssh root@IP docker ps | grep prod_html
then
echo "HTML/STATIC Production Server UP AND RUNNING"
else
echo HTML/STATIC Pages Production Server Down  | mail -v -s "RELAUNCHING SERVER"  RECEIVER-EMAILID
ssh root@IP docker run -dit -v /htmltest_storage:/usr/local/apache2/htdocs/ -p 8081:80 --name prod_html httpd
fi
else

echo HTML Test Server Down  | mail -v -s " HTML SERVER DOWN " RECEIVER-EMAILID
fi
```
_________________________________________________________________________________________________________

### Author:
----------------------------------
```diff
+ Vedant Shrivastava | vedantshrivastava466@gmail.com
```
