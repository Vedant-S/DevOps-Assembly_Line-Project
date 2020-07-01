# DevOps Project-6:
____________________________________________________________________________________________________________________
### CI/CD Auto Deployment Pipeline with Groovy, Jenkins to deploy applications on Kubernetes according to their respective language interpreter with auto testing:
____________________________________________________________________________________________________________________
### PROBLEM STATEMENT:
In this Project, we will be using Tools such as Github, Jenkins, Kubernetes, Docker Language used for creating and managing the Pipeline will be Groovy. We will use jenkins inbuilt plugin Job DSL for configuring jobs.

The Job DSL plugin attempts to solve this problem by allowing jobs to be defined in a programmatic form in a human readable file. Writing such a file is feasible without being a Jenkins expert as the configuration from the web UI translates intuitively into code.

Job DSL/Plugin, a project made up of two parts: the Domain Specific Language that allows users to describe jobs using Groovy-based language, and a Jenkins plugin which manages the scripts and the updating of Jenkins jobs which are created and maintained as a result.

____________________________________________________________________________________________________________________
### List of Jobs (Task) to perform in this project:

`Job1` : Pull the Github repo automatically when some developers push repo to Github.

`Job2` :

  1. Launching the pod: By looking at the code or program file, Jenkins should automatically start the respective language interpreter installed image container to deploy code on top of Kubernetes. For example, if code is of PHP, then Jenkins should start the container that has PHP already installed.

  2. Exposing the pod: so that later testing team could perform the testing on the pod.

  3. Creating persistent storage: In this process we will mount PVCs to the pods to store the data permanently if in case, server collects some data like logs, other user information.

`Job3` : Testing the app, if it is working or not. If the app is not working , then an email will be sent to developer with error messages and the app will be redeployed after the code have been edited by the developer.

`Developer site:`

`Step 1`: Pushing the Code to Github repository: Created git hooks for post-commit push. I have used two git repository:

- Git Repo containing application Code: This coontains all the data and code related to the application which will be deployed.
- Git repo containing Deployment Code.

`Deployment Code contains:`

- Nodeport Service, through which the pod will be exposed.
- Persistent Volume which will be attached to the pod.
- Deployment Code: It contains the definition of pod which includes its name, the image which will be used to launch the container in the pod, the mount path of the Persistent volume created in the previous step and the services using which this pod will be exposed.

`This is it at the developer site.`

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

I have also created a build Pipeline and for this project I have enabled `Poll SCM` in the seed job. So whenever there is any update in the application repository, the application to the client will be updated automatically.
____________________________________________________________________________________________________________________
### Author:
----------------------------------
```diff
+ Vedant Shrivastava | vedantshrivastava466@gmail.com
```
