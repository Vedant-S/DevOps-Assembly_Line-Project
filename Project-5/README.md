# DevOps Project-5:
____________________________________________________________________________________________________________________
### Prometheus Grafana Integration:
____________________________________________________________________________________________________________________
# <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTZfasd4cKMF_H222ed1Pz5S6o-QJ5M7Nj72w&usqp=CAU" width=100 height=100>Jenkins integration with Prometheus - Grafana <img src="https://www.influxdata.com/wp-content/uploads/Prometheus-1.png" width=100 height=100>
____________________________________________________________________________________________________________________
### PROBLEM STATEMENT:
Integrate Prometheus and Grafana and perform in following way:
1. Deploy them as pods on top of Kubernetes by creating resources Deployment, ReplicaSet, Pods or Services.
2. And make their data to be remain persistent.
3. And both of them should be exposed to outside world.
____________________________________________________________________________________________________________________
### Prerequisite:
- `Minikube and kubectl configured if not follow this link:` https://kubernetes.io/docs/setup/learning-environment/minikube/
- One Node for matrices.
____________________________________________________________________________________________________________________
## GENERAL CONCEPTS:
![Differences](https://cloud.vmware.com/community/wp-content/uploads/9/sites/9/2018/03/Metrics-vs-Logs-fig-1.png)
____________________________________________________________________________________________________________________
### STEPS:
____________________________________________________________________________________________________________________
`Step 1`: Here We need to make a deployment of Prometheus and Grafana.
- Open your text-editor and type the following code > save it > grafana.yaml.

```
apiVersion: v1
kind: Service
metadata:
  name: grafana
  labels:
    app: graf
spec:
  ports:
    - port: 3000
  selector:
    app: graf
  type: NodePort
---  
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: graf-pvc
spec:
  accessModes:
   - ReadWriteOnce
  resources:
    requests:
      storage: 2Gi   


---


apiVersion: apps/v1
kind: Deployment
metadata:
  name: grafana-deploy
  labels:
    app: graf
spec:
  replicas: 1
  selector:
    matchLabels:
      app: graf
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: graf
    spec:
      containers:
      - image: vimal13/grafana
        name: graf
        ports:
        - containerPort: 3000
          name: graf
        volumeMounts:
          - name: graf-vol
            mountPath: /var/lib/grafana
      volumes:
      - name: graf-vol
        persistentVolumeClaim:
          claimName: graf-pvc

```  
```
Explanation: Here U need to expose the port i,e 3000 , because Grafana runs on port 3000, in k8s to expose the port we need to use services i,e NodePort, for making data permanent here we use kind PersistentVolumeClaim , and gave access ReadWriteOnce, After that we have to make deployment of grafana in k8s kind is Deployment, spec for specification, gave image name i,e(vimal13/grafana), continerport (3000), for making data permanent here we have to mount the dir i,e (var/lib/grafana) in the PersistentVolumeClaim.
```
____________________________________________________________________________________________________________________
`Step 2`: Configure file of prometheus.
- Open your text-editor and type the following code > save it > prometheus.yaml.

```
apiVersion: v1
kind: Service
metadata:
  name: prometheus
  labels:
    app: prom
spec:
  ports:
    - port: 9090
  selector:
    app: prom
  type: NodePort
---


apiVersion: v1
kind: ConfigMap
metadata:
  name: p-config
  labels:
    app: prom
data:
  prometheus.yml: |
    global:
      scrape_interval:     15s
      evaluation_interval: 15s
    scrape_configs:
     - job_name: 'prometheus'
       static_configs:
       - targets: ['localhost:9090']
     - job_name: 'node1'
       static_configs:
       - targets: ['192.168.43.98:9100']
---       


apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: prom-vol
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 2Gi
---   


apiVersion: apps/v1
kind: Deployment
metadata:
  name: prometh-deploy
  labels:
    app: prom
spec:
  replicas: 1
  selector:
    matchLabels:
      app: prom
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: prom
    spec:
      containers:
      - image: vimal13/prometheus
        name: prom
        ports:
        - containerPort: 9090
          name: prom
        volumeMounts:
        - name: p-config
          mountPath: /etc/prometheus/prometheus.yml
          subPath: prometheus.yml
        - name: prom-vol
          mountPath: /prometheus/data
      volumes:
        - name: p-config
          configMap:
            name: p-config
        - name: prom-vol
          persistentVolumeClaim:
            claimName: prom-vol
```
```
Explanation: Here U need to expose port no. 9090, prometheus runs on port no. 9090, in k8s for exposing the port, kind is NodePort, to make the config file, Here we need to configure he kind ConfigMap, in the config map set your node ipi,e (IP:9100) port no. 9100 for node_exporter, for making data permanent here we use kind PersistentVolumeClaim, and gave access ReadWriteOnce, After that we have to make deployment in k8s, kind is Deployment , spec for specification, gave image name i,e(vimal13/prometheus), conatinerPort(9090), Now here we have to mount to things first ConfigMap location (/etc prometheus/prometheus.yaml) and second PersistentVolumeClaim location (/prometheus/data) for making our data permament.
```
____________________________________________________________________________________________________________________
`Step 3`: Configure kustomization file.
- Open your text-editor and type the following code > save it > kustomization.yaml.
```
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization
resources:
  - grafana.yaml
  -
  - prometheus.yaml

```
```
Explanation: specifies the common resources and common customizations to those resources, Here where need to use kink Kustomization in k8s, gave resources and it executes the resources, Here resources are yaml files .
```
`To Run the setup type > kubectl apply -k .`
____________________________________________________________________________________________________________________
`Step 4`: Output of prometheus:
- open your command prompt type > kubectl get services.
- Now open your browser > type > IP of minikube: port no given by the NodePort i.e. 31555.
____________________________________________________________________________________________________________________
`Step 5`: Setup of Grafana.
- open your command prompt type > kubectl get services.
- Now open your browser > type > IP of minikube:port no given by the NodePort i.e. 32106.
- To login> type> username(admin)> password(admin).
- Type your password to login > submit.
- Click on Data Sources for setup prometheus.
- Select prometheus.
- Type name , Gave the IP:port of prometheus.
- Now Here our prometheus configured in the Grafana.
____________________________________________________________________________________________________________________
`Step 6`: To create Dashboard in Grafana:
- Goto Grafana Homepage.
- Click on the + icon to create Dashboard , Here u need to configure the Panel for Dashboard.
- Click on Add panel .
- Select Myprometheus.
- Give the query (node_memory_MemFree_bytes) for usage of RAM > gave Visualization i.e (Graph).
- Give the name and save it.
- Add new Panel > Configure using following image:
- Add query node_cpu_seconds_total(for CPU usage)> select Gauge Visualization.

- Gave name , Save it.
- After that add new panel > configure using following image:
- Add query node_disk_read_bytes_total(For Disk Usage), Select Bar gauge Visualization.

- Gave name , Save it.
- Add One more New Panel > configure using following image:
- Add query sum by (job)(prometheus_http_requests_total{code="200"}) , it means we need to see how many http_request on prometheus, sum if for sum the values , code=200 means valid request.

- Gave name , Save it.
- Output after setup.
- After that we have to save our Dashboard.
- Click on the save symbol.
- Give name and save it.
____________________________________________________________________________________________________________________
`Step 7`: Now let's test whether our data is permanent or not.
- Open command prompt and delete the pod of grafana.

```
Explanation: Here you can see our pod has been deleted, and after that it starts new pod this is because we already use deployment, if pod is deleted deployment start new ones, here U can see for some second our data , dashboard has gone.
```
____________________________________________________________________________________________________________________
`Hence the project is complete.`

____________________________________________________________________________________________________________________
### Author:
-----------------------------------
```diff
+ Vedant Shrivastava | vedantshrivastava466@gmail.com 
```
