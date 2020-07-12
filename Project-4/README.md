# DevOps Project-4:
____________________________________________________________________________________________________________________
### Jenkins-Kubernetes Dynamic Slave-Master Cluster :
____________________________________________________________________________________________________________________
### PROBLEM STATEMENT:
1. Create container image that’s has Linux and other basic configuration required to run Slave for Jenkins. (example here we require kubectl to be configured)
2. When we launch the job it should automatically starts job on slave based on the label provided for dynamic approach.
3. Create a job chain of job1 & job2 using build pipeline plugin in Jenkins
4. Job1 : Pull the Github repo automatically when some developers push repo to Github and perform the following operations as:<br><br>
4.1 Create the new image dynamically for the application and copy the pplication code into that corresponding docker image.<br><br>
4.2 Push that image to the docker hub.(Public repository)

( Github code contain the application code and Dockerfile to create a new image )

5. Job2 (Should be run on the dynamic slave of Jenkins configured with Kubernetes kubectl command): Launch the application on the top of Kubernetes cluster performing following operations:<br><br>
5.1 If launching first time then create a deployment of the pod using the image created in the previous job. Else if deployment already exists then do rollout of the existing pod making zero downtime for the user.<br><br>
5.2  If Application created first time, then Expose the application. Else don’t expose it.
____________________________________________________________________________________________________________________
### Essential Requirements:
`1` SSH and JNLP protocol for use in Redhat and Windows connection.</br>
`2` Concept of slave/agent program explained and requirement of storage area/path information.</br>
`3` Configuring slave nodes Redhat and Windows, using Redhat IP-Ssh key, and Windows JNLP(JAVA Network Launching Protocol).
term Dynamic Distributed Job Cluster.</br>
`4` Knowledge of Docker Services server and Docker client.</br>
`5` Idea of fd:// in docker daemon, which has no tcp support and current Docker config which has no socket/network support.</br>
`6` Necessity to alter 'docker.service' file to add tcp port and IP address.</br>
`7` Idea about reload of daemon in linux and Docker.</br>
`8` Changing of DOCKER_HOST variable.</br>
`9` Setup docker within Jenkins with appropriate plugins.</br>
`10` Setup/configuration of Cloud services for jenkins to link to remote Docker server as dynamic slave node.</br>
`11` Setup ssh-key gen and pulled appropriate image from docker hub.</br>
______________________________________________________________________________________________________
### STEPS:
______________________________________________________________________________________________________
`Step 1`:
- Create a local git repo.
- Write a Dockerfile for creating a docker image which will have httpd installed in it for deploying our webserver.
- Also create a index.html page that you want deploy on the webserver.
- Create a New Repository on GitHub and don't initialise it with a Readme file.
- Run the commands listed on the page in git bash after you have added and committed to local git.
- Create post-commit where you will specify what has to to done after you commit the code on local git. ex. I have used git push which will automatically push the code to GitHub as soon it is commited and then a trigger which will run the first job of jenkins.

```
FROM centos
RUN yum install sudo -y
# Macking Package-repository up to date
RUN yum update -qy
RUN yum install  git -y
# Installing a SSH Server
RUN yum install -y openssh-server
RUN mkdir -p /var/run/sshd
# Installing JDK
RUN yum install java-11-openjdk-devel -y
# Creating and adding Jenkins user
RUN useradd jenkins
RUN echo "jenkins:jenkins" | chpasswd
RUN sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g'  -i /etc/pam.d/sshd
RUN mkdir /home/jenkins/.m2
RUN echo "jenkins ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/jenkins
RUN chmod 0440 /etc/sudoers.d/jenkins
RUN ssh-keygen -A
ENV NOTVISIBLE "in users profile"
RUN echo "export VISIBLE=now" >> /etc/profile
RUN rm /run/nologin
RUN chown -R jenkins:jenkins /home/jenkins/.m2/
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl
RUN chmod +x kubectl
RUN cp kubectl /usr/bin
WORKDIR /home/jenkins
COPY ca.crt /home/jenkins
COPY client.crt /home/jenkins
COPY client.key /home/jenkins
COPY config.yml /root/.kube/config
RUN sudo chown -R jenkins /root/.kube
EXPOSE 22  
CMD ["/usr/sbin/sshd", "-D"]
```
______________________________________________________________________________________________________
`Step 2`: Start jenkins and Create Job 1:
This job will automatically run as soon as the developer commit code on git.
- Specify the GitHub repository from where the code is to be downloaded.
- Create trigger , this the same trigger whose URL is specified in the post-commit file.
- Then write the commands for building the Dockerfile that is downloaded from GitHub and then push the docker image made to Docker Hub.
````
FROM centos
WORKDIR /root/jenk_task4
COPY . /var/www/html
RUN yum install httpd -y
CMD [ "/usr/sbin/httpd","-D","FOREGROUND" ]
Expose 80
````
______________________________________________________________________________________________________
`Step 3`: Create Dockerfile for Kubernetes slave:
- This dockerfile will create a docker image that will be used by jenkins to launch a slave node.
- To built the docker file use the command:
`docker build -t suhani15/kubectl:v1`
______________________________________________________________________________________________________
`Step 4`: Create a Config.yml file for Kubernetes.
______________________________________________________________________________________________________
`Step 5`: Configure jenkins for launching a slave node.
- Go to Manage Jenkins --> Manage nodes and cloud.
- Then configure it as doe in the following images.

In the Docker Host URI we have specified the `IP` of docker server but by default docker does not allow anyone to use it's server, and so that it can allow jenkins to use it we have to configure `docker.service` file which is in the directory:

- `/usr/lib/systemd/system/docker.service`
______________________________________________________________________________________________________
`Step 6`: Create Job 2 :
- Restrict the project to run in the slave node by specifying the label of the cloud we had configured.
- Build a trigger for running this job after the first job.
- In the execute shell write the code for creating deployment, scaling it and exposing it only it doesn't already exist. We can also use code snippet for doing all these and run the code file in the execute shell.
- On building this job a slave node will be launched.
______________________________________________________________________________________________________
`Step 7`: Build Pipeline View.
____________________________________________________________________________________________________________________
### Author:
----------------------------------
```diff
+ Vedant Shrivastava | vedantshrivastava466@gmail.com
```
