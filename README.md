<img src="https://miro.medium.com/max/3000/1*mqv03KrlG5LK2XU1uV4LJg.gif" height=100 width=200>

_________________________________________________________________________________________________________________

DevOps is a set of practices that combines software development (Dev) and information-technology operations (Ops) which aims to shorten the systems development life cycle and provide continuous delivery with high software quality. Demand for the development of dependable, functional apps has soared in recent years. In a volatile and highly competitive business environment, the systems created to support, and drive operations are crucial. Naturally, organizations will turn to their in-house development teams to deliver the programs, apps, and utilities on which the business counts to remain relevant."

"That's a lot to ask from DevOps. Fortunately, there are tools to help deliver custom, quality applications in a timely fashion. Jenkins is one of them. Conjuring images of the stereotypical English butler, the tool acts as a "faithful servant" of sorts, easing cumbersome development tasks."

Let us now begin this article to take a deep dive into what is DevOps, its features and architecture. Main Technology/Tools Used:
__________________________________________________________________________________________________________________
## <img src="https://i2.wp.com/tennexas.com/wp-content/uploads/2018/09/kubernetes.gif" width=100 height=100>Kubernetes:

        Kubernetes (commonly stylized as 'k8s') is an open-source container-orchestration system for automating application deployment, scaling, and management.
        It was originally designed by Google, and is now maintained by the Cloud Native Computing Foundation.
10 Kubernetes Features to Know

### Once you’ve got a grasp on the basics of k8s, you’ll likely want to start taking advantage of the advanced functionality and features.

### 1. Sidecars

Sidecars are a feature that enables you to run an additional container within a pod to be used as a watcher or proxy. You use this extra container to direct data to be mounted and exposed to other containers in the pod. For example, sidecars could be used to handle logging or authentication for a primary container. Sidecars are useful when you want to allow related processes within a pod to communicate without modifying the primary container source code.

### 2. Helm Charts

Helm is a package manager for Kubernetes that you can use to streamline the installation and management of k8s applications. It uses charts composed of a description of the package and templates containing k8s manifest files. You use manifest files in k8s to create, modify, and delete resources.

Helm enables you to create and share reproducible builds of k8s applications. There are many preconfigured charts available with their dependencies included. Helm charts enable you to deploy applications quickly and easily.

### 3. Custom Controllers

Controllers are loops that regulate the state of your system or resources. With custom controllers, you can accomplish tasks that aren’t included with standard controllers. For example, you can dynamically reload application configurations. Custom controllers can be used with native or custom resource types.

Using these controllers can be an easier way to manage deployments than toolchains. Controllers are a single piece of code while toolchains require familiarity with a number of interfaces.

### 4. Custom Scheduling

K8s comes with a default scheduler for assigning newly created pods to nodes. If this scheduler doesn’t fit your needs or if you would like to run multiple schedulers, you can create a custom scheduler. For example, you could use custom scheduling to facilitate cloud bursting. Cloud bursting is when you move a workload to the cloud in order to meet higher performance or traffic demands.

Custom schedulers can be useful if you have special pods that you want to be handled separately from the others. You can set schedulers according to preference or requirement. Scheduling can rely on strict matching or inclusion and exclusion lists.

### 5. PodDisruptionBudget (PDB)

PDB is a feature that enables you to restrict the number of pods in a cluster that you can stop voluntarily. It helps ensure that a minimum number of pods stay active during tasks like maintenance, auto-scaling down, or upgrading.

With PDB, you can prevent application availability or performance from being affected by administrative tasks. An important limit to keep in mind with PDB is that it does not have an effect if a node fails, only when changes are done voluntarily.

### 6. Go Modules

Go modules are collections of Go packages you can use to manage your application dependencies. Dependencies are bits of code, libraries or frameworks that your application depends on to run. Kubernetes supports Go modules from v1.15 on.

Modules enable you to use multiple versions of the same dependency package or to use dependencies stored in different environments. You can use modules to ease code sharing between distributed teams and minimize the impact of changes in dependencies.

### 7. Taints and Tolerations

Taints and tolerations are a feature that enables you to direct nodes to “attract” or “repel” pods. Taints are assigned to nodes and specify that pods that do not tolerate the taint assigned should not be accepted.

Tolerations are assigned to pods and signal to nodes with matching taints that pods can be accepted. This feature is useful if you need to deploy an application on specific hardware or if you want to dedicate a set of nodes to specific users.

### 8. Cluster Federation

Cluster federation enables you to treat multiple clusters as a single logical cluster, managed through a single control plane. Federation enables clusters running in different locations or environments to be more easily managed. You can use federation to make clusters both highly available and resilient by enabling workloads to be passed between clusters.

### 9. Health Checking

You can check the health of pods or applications in k8s by defining probes to be run by a kubelet agent. You can define readiness, liveness, and startup probes, as follows:

    Readiness—determine if a container can receive requests. If it fails the pod IP address is removed from any endpoints directing traffic to the pod. 
    Liveness—determine if a container needs to be restarted. Failure means the container is killed and restarted. 
    Startup—determine if an application within the container has started. In the case of failure, the container is killed and restarted.

You can customize probes with timeouts, retry attempts, minimum success or failure thresholds, and delayed runtimes.

### 10. Feature Gates

Feature gates are a functionality in k8s that you can use to turn features on or off on a node, cluster, or platform level. This functionality enables you to safely test features without risking critical components or your production environment.

You can use gates to control alpha, beta, or stable (GA) features. Each feature gate controls only one feature. Alpha features default to off and beta and GA features default to on. For a full list of gates, feature descriptions and statuses, check the documentation here.

### Conclusion

Kubernetes is a powerful tool for orchestrating container deployments, but it can be challenging to work with. Understanding the functionality and features that are available to you is necessary to get the greatest benefit from this tool.

If any of the features introduced here seem like they might be useful for your deployment, investigate them further. K8s documentation is the best place to start and can often provide a tutorial for configuration or direct you to an external source.
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
![CI/CD_Process](https://hackernoon.com/hn-images/1*1kUhczYDfpkWXSFt0mI2dA.png)
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

### Author:
----------------------------------
```diff
! Vedant Shrivastava | vedantshrivastava466@gmail.com
```

