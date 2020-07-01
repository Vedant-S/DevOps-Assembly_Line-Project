FROM centos
RUN yum install wget -y ; yum install sudo -y ; wget -O /etc/yum.repos.d/jenkins.repo http://pkg.jenkins.io/redhat/jenkins.repo ; rpm --import http://pkg.jenkins.io/redhat/jenkins.io.key ; yum install initscripts -y ; yum install jenkins -y ; yum install java-11-openjdk.x86_64 -y ; yum install git -y ; yum install python3 -y 
EXPOSE 8080
RUN echo -e "jenkins ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers
RUN mkdir /rahul
USER jenkins
ENV USER jenkins

CMD ["java", "-jar", "/usr/lib/jenkins/jenkins.war"]
	



