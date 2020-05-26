# DevOps Project-1:
____________________________________________________________________________________________________________________
JOB#1
If Developer push to dev branch then Jenkins will fetch from dev and deploy on dev-docker environment.

JOB#2
If Developer push to master branch then Jenkins will fetch from master and deploy on master-docke environment.
both dev-docker and master-docker environment are on different docker containers.

JOB#3
Jenkins will check (test) for the website running in dev-docker environment. If it is running fine then Jenkins will merge the dev branch to master branch and trigger #job 2

This is the repository where the Project has been uploaded.


### 1. Setting up the Development/Host  System

This is the system in which the developer works. We are considering it to be **Windows**. Here, Git is installed and authenticated. To make this project more realistic, we are going to work on 2 GitHub branches:
- master
- dev1

First, we create a new repository in GitHub. We copy the clone url of the repository and clone the it in the Desktop using `git clone <repo url>`.

We move inside the folder just created and create an `index.html` file.
Inside the file, lets type	<br>
`This is from master branch`
<br>
Then we add the file in the Staging Area using `git add . `<br>
Then we commit the changes  using the command: `git commit . -m "first commit from master"`<br>
Finally we push to GitHub using: `git push` <br>

We can also cross-check the contents from the GitHub website to confirm.

Now, we want to work with the `dev1` branch. We want to pull the contents from the `master` branch first.

To create and change the branch, we type: `git checkout -b dev1`
Now, all the codes from `master` branch are present in the `dev1` branch. We want to add an extra line in this branch:<br>
`cat >> This line is from Dev1 branch`

To stage, commit and push, we use the following commands:
```
git add . 
git commit . -m "Second commit from DEV1"
git push -u origin dev1
```

Now, we have setup 2 branches in Github, having some differential codes.


### 2. Setting up the Server System

This is the system where the Web Server will be running. We are going to use Docker to run 2 `httpd` servers, one for deploying the `master` branch and the other for the `dev1` branch. We are going to use RHEL 8 as the Operating System to host the Docker containers.<br>

As we are going to automate the process, we are going to configure Jenkins for creating the CI/CD pipeline.<br>

**Jenkins** would do 2 important tasks for each of the branches:
- Dowloading any code changes from GitHub to the server system.
- Starting the Webserver from Docker containers, to view the final website.

**Jenkins** would do another important task from the **Quality Assurance Team**. If the `dev1` branch is working fine, then Jenkins would merge it with the `master` branch, on being triggered to do so.

We create 2 directories on our Desktop directory from the terminal.
- lwtest
- lwtestdev

The codes downloaded from **GitHub** by **Jenkins** would be placed in these folders according to their branches.

Now, lets configure Jenkins to do all those tasks.

### 3. Configuring Jenkins

Lets start the Jenkins process from RHEL 8 by the command: `systemctl start jenkins`<br>
Now, on visiting the port **8080**, we can see the Jenkins Control Panel running.

But, we are going to configure it from Host / Developer system by getting the IP address of RHEL. <br> For that we use the command `ifconfig enp0s3`

After getting the IP Address, from any browser of the **Host System**, we can direct to the IP address.<br>
for e.g. `http://192.123.32.2932:8080` <br>
After logging in with both the username and password as `admin`, we come to the Jobs List page. Here we can see the Jobs we have submitted to be performed.

#### 3.1. Task-1 : Automatic Code Download 

For now, we have to create new Jobs for downloading the latest codes from **both** the branches of Github separately, to the Server system, for being deployed on the Web-server.<br>
i.e. The `master` branch should be downloaded in the `lwtest` folder and the `dev1` branch would be downloaded in the `lwtestdev` folder.

For creating the Job for downloading codes from the **master** branch:
1. Select `new item` option from the Jenkins menu. 
2. Assign a name to the Job ( eg. lwtest-1 )and select it to be a `Freestyle` project.
3. From the `Configure Job` option, we set the configurations.
4. From **Source Code Management** section, we select Git and mention the URL of our GitHub Repository and select the branch as `master`.
5. In the **Build Triggers** section, we select `Poll SCM` and set the value to `* * * * *`.<br> **This means that the Job would check any code change from GitHub every minute.**
6. In the Build Section, we type the following script:
			`sudo cp -v -r -f * /root/Desktop/lwtest`
   **This command would copy all the content downloaded from the GitHub master branch to the specified folder for deployment.**
7. On clicking the **Save** option, we add the Job to our Job List.

On coming back to the Job List page, we can see the **Job** is being built. If the color of the ball turns **blue**, it means the Job has been successfully excecuted. If the color changes to **red**, it means there has been some error in between. We can see the console output to check the error.

**The same process can be performed to create another Job `lwtest-2` for the `dev1` branch. Only the branch name needs to be changed to `dev1` and the script:**<br>`sudo cp -v -r -f * /root/Desktop/lwtestdev`

Till now, we have successfully downloaded the codes from both the branches of GitHub to our Server System automatically.


#### 3.2. Task-2 : Automatically Starting the Docker containers


Next, we create another Job for starting the docker containers once the codes have been copied into the `lwtest` and `lwtestdev` folders.

For starting the docker container once the `lwtest` folder updates from the **master** branch:
1.  Select `new item` option from the Jenkins menu. 
2.  Assign a name to the Job ( eg. lwtest-1-docker )and select it to be a `Freestyle` project.
3.  From the `Configure Job` option, we set the configurations.
4.  From **Build Triggers** section, we select `Build after other projects are built` and mention `lwtest` as the project to watch. This is called **DownStreaming**.
5.  In the **Build** Section, we type the following script:
```
if sudo docker ps | grep docker_master
then
echo "Already Running"
else
sudo docker run -d  -t -i -p 8085:80 -v /root/Desktop/lwtest:/usr/local/apache2/htdocs/ --name docker_master httpd
fi
```

This script first checks if any Docker container has been already created, in such case it exits. <br>
Else, it starts a new Docker container of the `httpd` webserver, with some parameters:
- Runs the container in background : `-d`
- Exposes the port so that the container can be operated from outside the Server system (RHEL)
- Mounts the `lwtest` folder on the `htdocs` folder, from where the webserver renders the pages.
- Sets the name of the container as `docker_master`
6. On clicking the **Save** option, we add the Job to our Job List.

**The same process can be performed to create another Job `lwtest-2-docker` for starting the docker containers once the `lwtestdev` folder updates from the `dev1` branch.**<br> **Only the build trigger needs to be set to watch `lwtestdev` and the corresponding folder name should be changed in the script.**

These 2 Jobs would automatically run just after the codes are updated in the respective folders, by the previous jobs.

Thus we have successfully set up a **CI/CD pipeline** where once the developer pushes the code on GitHub, **Jenkins** would automatically download the codes and start the **Docker containers** to run the Web-Server.


#### 3.3. Task-3 : Merging the `dev1` branch with `master` by QAT

Finally, this is the task of the **Quality Assurance Team**. When the team certifies that the `dev1` branch is working fine, they can merge it with the `master` branch using **Remote Build Triggers**.

To setup the Trigger we are going to use **Jenkins** to do the automation:
1. Select **new item** option from the Jenkins menu. 
2. Assign a name to the Job ( eg. Merge-test )and select it to be a **Freestyle** project.
3. From the **Configure Job** option, we set the configurations.
4. From **Source Code Management** section, we select Git and mention the URL of our GitHub Repository and select the branch to build as `dev1`.
5. From the **Additional Behavior** Dropdown list, we select `Merge Before Build`
6. There, we set the **Name of the Repository** as `origin` 
7. We set the **Branch to Merge to** as `master`
8. From the **Build Triggers** section, we select **Trigger builds remotely** option.
9. Provide an **Authentication Token**
10. From the **Post Build Actions** dropdown, we select **Git Publisher**.
11. We check **Push only if build succeeds** and **Merge Results** options.
12.  On clicking the **Save** option, we add the Job to our Job List.
13.  Now from **Build Triggers** section of the **lwtest** job, we select `Build after other projects are built` and mention `Merge-test` as the project to watch.

Thus, the Job has been setup. To Trigger the Build, the **QAT** would run the following command:<br>
`curl --user "<username>:<password>" JENKINS_URL/job/Mergetest/build?token=TOKEN_NAME`

e.g. `curl --user "admin:admin" http://192.123.32.2932:8080/job/Merge-test/build?token=redhat`

So, after merging, the **lwtest** Job is again fired, so that the updated code can be downloaded in the Server system to run in the web-server.


### Author:
----------------------------------
```diff
+ Vedant Shrivastava | vedantshrivastava466@gmail.com
```
