# DevOps Project-6:
____________________________________________________________________________________________________________________
### CI/CD Auto Deployment Pipeline with Groovy, Jenkins to deploy applications on Kubernetes according to their respective language interpreter with auto testing:
____________________________________________________________________________________________________________________
Further, deploy applications on Kubernetes according to their respective language interpreter with auto testing.
<img src="https://miro.medium.com/max/2772/1*GrauvM0qGnsxJTP5WFu7Fg.png" width=500 height=200>
____________________________________________________________________________________________________________________
### PROBLEM STATEMENT:
In this Project, we will be using Tools such as Github, Jenkins, Kubernetes, Docker Language used for creating and managing the Pipeline will be Groovy. We will use jenkins inbuilt plugin Job DSL for configuring jobs.

The Job DSL plugin attempts to solve this problem by allowing jobs to be defined in a programmatic form in a human readable file. Writing such a file is feasible without being a Jenkins expert as the configuration from the web UI translates intuitively into code.

Job DSL/Plugin, a project made up of two parts: the Domain Specific Language that allows users to describe jobs using Groovy-based language, and a Jenkins plugin which manages the scripts and the updating of Jenkins jobs which are created and maintained as a result.

____________________________________________________________________________________________________________________
### List of Jobs (Task) to perform in this project
____________________________________________________________________________________________________________________
<img src="https://www.jenkins.io/images/post-images/vscode-pipeline-linter/example1.gif"></br>
____________________________________________________________________________________________________________________
`Job1` : Pull the Github repo automatically when some developers push repo to Github.

`Job2` :

  1. Launching the pod: By looking at the code or program file, Jenkins should automatically start the respective language interpreter installed image container to deploy code on top of Kubernetes. For example, if code is of PHP, then Jenkins should start the container that has PHP already installed.

  2. Exposing the pod: so that later testing team could perform the testing on the pod.

  3. Creating persistent storage: In this process we will mount PVCs to the pods to store the data permanently if in case, server collects some data like logs, other user information.

`Job3` : Testing the app, if it is working or not. If the app is not working , then an email will be sent to developer with error messages and the app will be redeployed after the code have been edited by the developer.
____________________________________________________________________________________________________________________
`Developer site:`

`Step 1`: Pushing the Code to Github repository: Created git hooks for post-commit push. I have used two git repository:

- Git Repo containing application Code: This contains all the data and code related to the application which will be deployed.
- Git repo containing Deployment Code.

`Deployment Code contains:`

- Nodeport Service, through which the pod will be exposed.
- Persistent Volume which will be attached to the pod.
- Deployment Code: It contains the definition of pod which includes its name, the image which will be used to launch the container in the pod, the mount path of the Persistent volume created in the previous step and the services using which this pod will be exposed.
_________________________________________________________________________________________________________________________________________

     apiVersion: v1
     kind: Service
     metadata:
       name: webserver-service
       labels:
         app: httpd
     spec:
       ports:
         - nodePort: 30002
           port: 80
           targetPort: 80
       selector:
         app: httpd
         tier: web
       type: NodePort
     ---
     apiVersion: v1
     kind: PersistentVolumeClaim
     metadata:
       name: httpdweb2-pv-claim
       labels:
         app: httpd
     spec:
       accessModes:
         - ReadWriteOnce
       resources:
         requests:
           storage: 1Gi
     ---
     apiVersion: apps/v1
     kind: Deployment
     metadata:
       name: httpd-g-web
       labels:
         app: httpd
     spec:
       selector:
         matchLabels:
           app: httpd
           tier: web
       strategy:
         type: Recreate
       template:
         metadata:
           labels:
             app: httpd
             tier: web
         spec:
           containers:
           - image: vimal13/apache-webserver-php
             name: httpd-g-web
             ports:
             - containerPort: 80
               name: httpd-g-web
             volumeMounts:
             - name: httpd-web-persistent-storage
               mountPath: /var/www/html
           volumes:
           - name: httpd-web-persistent-storage
             persistentVolumeClaim:
               claimName: httpdweb2-pv-claim

`This is it at the developer site.`
____________________________________________________________________________________________________________________
`Operational site:`

- I am using jenkins to create a Code Pipeline. Through this, an environment of Continuous integeration and continuos delievery will be setup. This jenkins is setup inside a pod which is managed by Deployment controller in Kubernetes.

- Through this I have tried to achieve a system which can monitors itself and in case of any failure can recovers itself, i.e. without any need of operationals unit to manually keep a check and manually restarting the system in case of failure.

`To create the deployment controller:`

`Step 1`: Setting up Jenkins inside a container along with Kubectl, a client side program to manage kubernetes cluster (Minikube). For this create a directory and in this

- Create a dockerfile, which is as follows:
```
Note: To configure kubectl in this image, you have to transfer the certificates and keys which is stored by minikube at ${HOME}/.minikube and and config file which is stored by kubectl in ${HOME}/.kube directory. For this Dockerfile, I have all these files in my host sysetm as follows.
```
- Config file for configuring the kubectl.
- Build this Image using command: In this user_name is the user name of your docker account.
- Upload this image in the docker hub Registry
____________________________________________________________________________________________________________________
`Next step is to create the Deployment file (filename.yml) which will create a pod using this image. Start writing following code snippets.`

`Step 1`: Creating a NodePort Service.
`Step 2`: Creating two persistent Volumes:
- This Volume will store all the jenkins data and its configurations.
- This Volume is created to store all the files of the application.
`Step 3`: Creating the Deployment:

This is it.
Now, Create this deployment using command:
`kubectl create -f filename.yml`

After the Pod is setup successfully, Access the jenkins server on the port: 192.168.99.100:30000.
____________________________________________________________________________________________________________________
In jenkins, download the Github, Git Plugin, Job DSL plugin and configure the email settings in the jenkins and then proceed to followining steps:

`Project methodology`: In the process of Configuration as code, We will create a job in jenkins, this job will download the groovy script, which will contain the configuration of other jobs job 1, job 2 and job 3. The purpose of this job is to download the groovy script is to run it, which in turn will create these 3 main jobs required for CI/CD Pipeline. This job which will create other jobs is called seed job.

Create a file named `config.groovy` and start writing following job definitions in it.

`For Job 1`: ( I have used G Job1 in place of the name Job 1. Just to diffrentiate these jobs with my other projects).
`For Job 2`: As provided.
`For Job 3`: As provided.

`Now push this .grrovy file in the github repository.

- Create the seed job. It is simply just another job in jenkins, no difference. The job definition is:
- Save and build the Seed Job and Thats it. All the other jobs Job 1, Job 2 and Job 3 will be created automatically and will build automatically in the order.
- Output the seed job:
____________________________________________________________________________________________________________________
```
    job('G Job1') {
        description('Job1')
     scm {
             github('anmol-sinha-coder/Sample-Website','master')
        }
        steps {
             shell("cp * -vrf /home/jenkins")
          }
       triggers {
             scm('* * * * *')
          }
     triggers {
              upstream('Admin Job (Seed)', 'SUCCESS')
           }
    }

    job('G Job2') {
        description('Job2')
     scm {
             github('Vedant-S/DevOps-Assembly_Line-Project/tree/master/Project-6','master')
          }
     triggers {
              upstream('G Job1', 'SUCCESS')
           }
     steps {
             shell('''
       if  ls /home/jenkins | grep php
       then
         if kubectl get deployment --selector "app in (httpd)" | grep httpd-web
            then
          kubectl apply -f Deployment.yml
             else
                        kubectl create -f Deployment.yml
             fi
             POD=$(kubectl get pod -l app=httpd -o jsonpath="{.items[0].metadata.name}")
             kubectl cp /home/jenkins/index.php ${POD}:/var/www/html
       fi
       ''')
           }

    }
    job('G Job3') {
        description('Job3')
     triggers {
              upstream('G Job2', 'SUCCESS')
           }
        steps {
            shell('''
      status=$(curl -o /dev/null -s -w "%{http_code}" http://192.168.99.100:30002)
      if [[ $status == 200 ]]
      then
       exit 0
      else
       exit 1
          fi
      ''')
            }
      publishers {
            extendedEmail {
                recipientList('vedantshrivastava466@gmail.com')
                defaultSubject('Job status')
               attachBuildLog(attachBuildLog = true)
                defaultContent('Status Report')
                contentType('text/html')
                triggers {
                    always {
                        subject('build Status')
                        content('Body')
                        sendTo {
                            developers()
                            recipientList()
                        }
                    }
                }
            }
        }
    }
```   
____________________________________________________________________________________________________________________
I have also created a build Pipeline and for this project I have enabled `Poll SCM` in the seed job. So whenever there is any update in the application repository, the application to the client will be updated automatically.
____________________________________________________________________________________________________________________
### Author:
----------------------------------
```diff
+ Vedant Shrivastava | vedantshrivastava466@gmail.com
```
