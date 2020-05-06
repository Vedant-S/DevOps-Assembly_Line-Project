<img src="https://miro.medium.com/max/3000/1*mqv03KrlG5LK2XU1uV4LJg.gif" height=100 width=200>

_________________________________________________________________________________________________________

DevOps is a set of practices that combines software development (Dev) and information-technology operations (Ops) which aims to shorten the systems development life cycle and provide continuous delivery with high software quality. Demand for the development of dependable, functional apps has soared in recent years. In a volatile and highly competitive business environment, the systems created to support, and drive operations are crucial. Naturally, organizations will turn to their in-house development teams to deliver the programs, apps, and utilities on which the business counts to remain relevant."

"That's a lot to ask from DevOps. Fortunately, there are tools to help deliver custom, quality applications in a timely fashion. Jenkins is one of them. Conjuring images of the stereotypical English butler, the tool acts as a "faithful servant" of sorts, easing cumbersome development tasks."

Let us now begin this article to take a deep dive into what is DevOps, its features and architecture. Main Technology/Tools Used:
_______________________________________________________________________________________________________________
## <img src="https://miro.medium.com/max/1000/1*E8IgOSkMTpBRs0w0-Zsx2g.gif" width=200 height=100>Docker:

Docker is a set of platform as a service (PaaS) products that uses OS-level virtualization to deliver software in packages called containers. It is a category of cloud computing services that provides a platform allowing customers to develop, run, and manage applications without the complexity of building and maintaining the infrastructure typically associated with developing and launching an app. Docker is one of the tools that used the idea of the isolated resources to create a set of tools that allows applications to be packaged with all the dependencies installed and ran wherever wanted. Docker has two concepts that is almost the same with its VM containers as the idea, an image, and a container. An image is the definition of what is going to be executed, just like an operating system image, and a container is the running instance of a given image.

**Differences between Docker and VM:**

    Docker containers share the same system resources, they don’t have separate, dedicated hardware-level resources for them to behave like completely independent machines.
    They don’t need to have a full-blown OS inside.
    They allow running multiple workloads on the same OS, which allows efficient use of resources.
    Since they mostly include application-level dependencies, they are pretty lightweight and efficient. A machine where you can run 2 VMs, you can run tens of Docker containers without any trouble, which means fewer resources = less cost = less maintenance = happy people.

## <img src="https://www.edureka.co/blog/wp-content/uploads/2016/11/Jenkins-4.gif" width=300 height=200> Jenkins:

a. What is Jenkins and Continuous Integration?

**_'Jenkins to the rescue!'_**

As a Continuous Integration tool, Jenkins allows seamless, ongoing development, testing, and deployment of newly created code. Continuous Integration is a process wherein developers commit changes to source code from a shared repository, and all the changes to the source code are built continuously. This can occur multiple times daily. Each commit is continuously monitored by the CI Server, increasing the efficiency of code builds and verification. This removes the testers' burdens, permitting quicker integration and fewer wasted resources.

b. What are the Jenkins Features?

####    Easy Installation:
    Jenkins is a platform-agnostic, self-contained Java-based program, ready to run with packages for Windows, Mac OS, and Unix-like operating systems.
-----------------------------------------------------------------------------------------------------------
####    Easy Configuration:
    Jenkins is easily set up and configured using its web interface, featuring error checks and a built-in help function.
-----------------------------------------------------------------------------------------------------------
####    Available Plugins:
    There are hundreds of plugins available in the Update Center, integrating with every tool in the CI and CD toolchain.
-----------------------------------------------------------------------------------------------------------
####    Extensible:
    Jenkins can be extended by means of its plugin architecture, providing nearly endless possibilities for what it can do.
-----------------------------------------------------------------------------------------------------------
####    Easy Distribution:
    Jenkins can easily distribute work across multiple machines for faster builds, tests, and deployments across multiple platforms.
-----------------------------------------------------------------------------------------------------------
####    Free Open Source:
    Jenkins is an open source resource backed by heavy community support.
-----------------------------------------------------------------------------------------------------------
____________________________________________________________________________________________________________________________________
# Implementation and Understanding

* Explanation and use case of Docker as a container based software to host programs such as any OS. 

* Introduction to concept of an environment which consists of WebServer and Distros.

* Launching of OS in isolation (locally) from docker.

* Defining OS Image as a bootable i/o system or device. 

* Also setup local webserver httpd from docker.
```diff
# docker pull httpd
# docker run -d -t -i --name Redhat_WebServer httpd
# docker cp new.html Redhat_WebServer
```

* Concept of mounting docker to remote developer's system explained.
```diff
# docker run -d -t -i /lwweb:/user/local/apache2/docs/ --name MyWebPage httpd
```

* Explanation of PAT networking to make local WebServer available to clients globally.
```diff
# docker run -d -t -i /lwweb:/user/local/apache2/docs/ -p 8001:80 --name ClientSideOpen httpd
```

* Integrating both Jenkins and Docker technologies along with the VCS such as Github with update data obtained from developers system.

* Explaining the necessity of two independent job creation for (1)obtaining code pushed to github by pull request by Jenkins, and (2)Deploying code to the environment.

* Explanation of the method of job chaining as build trigger , where one job depends on the others success, instead of simply being queued with it.

* Providing the root access to Jenkins using sudo command, after editing /etc/sudoers file, instead of the setfacl Access Control Listing command. 

# The Architecture:

1. The DEVELOPER & Local Workspace:

Each developer maintains a local workspace in personal system, and commits the changes/patches to Github through Git as and when necessary.

2. Github:

Once the changes are pushed to Github, the challenge is to have an automated architecture or system in order to deploy these changes to the webserver (after testing) in order for the clients to avail the benefits.

3. Jenkins:

This challenge is solved by Jenkins which serves as a middlemen to automate the deployment process.

<img src="https://d2908q01vomqb2.cloudfront.net/7719a1c782a1ba91c031a682a0a2f8658209adbf/2019/10/20/Diagram2.png" align="center">

### Team Members
----------------------------------
```diff
+ Anmol Sinha        | 1805553@kiit.ac.in
! Vedant Shrivastava | vedantshrivastava466@gmail.com
```
