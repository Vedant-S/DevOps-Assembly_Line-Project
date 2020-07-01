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
