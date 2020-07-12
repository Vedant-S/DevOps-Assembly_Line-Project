# DevOps Project-3:
____________________________________________________________________________________________________________________
### Continuous Deployment Using Kubernetes:
____________________________________________________________________________________________________________________
### Kubernetes Replication Controller Deployment in Jenkins: <br/>
____________________________________________________________________________________________________________________
<img src="https://miro.medium.com/max/662/1*WcsxEzPbIGvFBQY6j2in5Q.png"></img>
____________________________________________________________________________________________________________________
### PROBLEM STATEMENT:
Perform second task on top of Kubernetes where we use Kubernetes resources like Pods, ReplicaSet, Deployment, PVC and Service.
1. Create container image that’s has Jenkins installed  using dockerfile  Or You can use the Jenkins Server on RHEL 8/7
2.  When we launch this image, it should automatically starts Jenkins service in the container.
3.  Create a job chain of job1, job2, job3 and  job4 using build pipeline plugin in Jenkins
4.  Job1 : Pull  the Github repo automatically when some developers push repo to Github.
5. Job2 :
    1. By looking at the code or program file, Jenkins should automatically start the respective language interpreter installed image container to deploy code on top of Kubernetes ( eg. If code is of  PHP, then Jenkins should start the container that has PHP already installed )
    2.  Expose your pod so that testing team could perform the testing on the pod
    3. Make the data to remain persistent ( If server collects some data like logs, other user information )
6.  Job3 : Test your app if it  is working or not.
7.  Job4 : if app is not working , then send email to developer with error messages and redeploy the application after code is being edited by the developer
_________________________________________________________________________________________________________
### For connecting to Kubernetes(k8s) and Jenkins via the DOCKERFILE :-
      FROM centos
      RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/`curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt`/bin/linux/amd64/kubectl

      RUN chmod +x kubectl
      RUN mv kubectl /usr/bin
      RUN mkdir /root/.kube/
      COPY /config /root/.kube/
      COPY /essentials/* /root/
      RUN yum install git -y
      RUN yum install java -y
      RUN yum install sudo -y
      RUN yum install openssh-server -y
      RUN mkdir /jenkins
      RUN ssh-keygen -A
      EXPOSE 22
      CMD ["/usr/sbin/sshd","-D"] && /bin/bash
_____________________________________________________________________________________________________________________________________________________________________
### The DOCKERFILE to expose HTTPD is:-

            FROM centos
            RUN yum install httpd -y
            RUN echo /usr/sbin/httpd >> /etc/bashrc
            COPY / /var/www/html/
            EXPOSE 80

### The config.YAML file can be seen as :-

            apiVersion: v1
            kind: Config

            clusters:
            - cluster:
                server: https://<YOUR_IP>:4243
                certificate-authority: /root/ca.crt
              name: kubecluster

            contexts:
            - context:
                cluster: kubecluster
                user: Anmol

            users:
            - name: Anmol
              user:
                client-key: /root/client.key
                client-certificate: /root/client
_________________________________________________________________________________________________________________________________
![email](https://miro.medium.com/max/600/1*qzDG-ROC1aUkVZ-LYVe5pA.jpeg)
_____________________________________________________________________________________________________________________________________________________________________
## E-Mailing in Jenkins
Several times you will encounter emailing problems for QAT team and/or client. Instead of using your SMTP python code in pipeline just do the following steps:-<br/>
Jenkin Email notification is a Java based plugin tool to automate notification alert whenever an Email is received. It suits best for the CI (Continuous Integration) code. Its plug-in framework makes it highly adaptive and Master-Slave framework makes it highly scalable, which in turn makes it worthy for start-up/Labs and big enterprises at the same time.

`step 1)` Go back to manage Jenkin, select the configuration system option. Go to the E-mail notification section and Enter the SMTP server name, Email address, and password, check SSL and enter SMTP port.

`step 2)` Test the Jenkin Email notification by sending a test email.

`step 3)` Configure the recipients in the Jenkins project - When you configure any Jenkins build project, right at the end is the ability to add recipients who would get email notifications for unstable or broken builds. Then click on the Save button.

Apart from the default, there are also notification plugin’s available in the market. An example is the notification plugin from Tikal Knowledge which allows sending Job Status notifications in JSON and XML formats. This plugin enables end-points to be configured as shown below.


Here are the details of each option −

"Format" − This is the notification payload format which can either be JSON or XML.

"Protocol" − protocol to use for sending notification messages, HTTP, TCP or UDP.

"Event" − The job events that trigger notifications: Job Started, Job Completed, Job Finalized or All Events (the default option).

"URL" − URL to send notifications to. It takes the form of "http://host" for HTTP protocol, and "host:port" for TCP and UDP protocols.

"Timeout" − Timeout in milliseconds for sending notification request, 30 seconds by default.

____________________________________________________________________________________________________________________
### Author:
----------------------------------
```diff
+ Vedant Shrivastava | vedantshrivastava466@gmail.com
```
