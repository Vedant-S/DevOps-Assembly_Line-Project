job("task6_jb1"){
        description("This will pull the developers code from github & filter & save the php and html codes in different directories")
        scm {
                 github('Vedant-S/Sample-Website' , 'master')
             }
        triggers {
                scm("* * * * *")
                
  	}
        steps {
        shell('''if sudo ls /Vedant-S | grep html
                 then
                 echo "dir for html code is already present"
                 else
                 sudo mkdir /Vedant-S/html
                 fi

                 if sudo ls | grep .html
                 then
                 sudo cp -vrf *.html /Vedant-S/html
                 fi
 
                 if sudo ls /Vedant-S | grep php
                 then
                 echo "dir for php code is already present"
                 else
                 sudo mkdir /Vedant-S/php
                 fi

                 if sudo ls | grep .php
                 then
                 sudo cp -vrf *.php /Vedant-S/php
                 fi''')
      }
}


job("task6_jb2"){
        description("to deploy our applications on k8s")
        
        triggers {
        upstream {
    upstreamProjects("task6_jb1")
    threshold("Fail")
        }
        }
        steps {
        shell('''if sudo ls /Vedant-S | grep html
then
  if sudo kubectl get pvc --kubeconfig /Vedant-S/kubectlconfig | grep html
  then
  echo "pvc for html already created"
  else
  sudo kubectl create -f /Vedant-S/deply/html-pvc.yaml --kubeconfig /Vedant-S/kubectlconfig
  fi
  if sudo kubectl get deploy --kubeconfig /Vedant-S/kubectlconfig | grep html-webserver
  then
    echo "already running"
  else
    sudo kubectl create -f /Vedant-S/deply/html-deply.yaml --kubeconfig /Vedant-Skubectlconfig
  fi
else
echo "no html code from developer to host"
fi

if sudo ls /Vedant-S | grep php
then
  if sudo kubectl get pvc --kubeconfig /Vedant-S/kubectlconfig | grep php
  then
  echo "pvc for php already created"
  else
  sudo kubectl create -f /Vedant-S/deply/php-pvc.yaml --kubeconfig /Vedant-S/kubectlconfig
  fi
  if sudo kubectl get deploy --kubeconfig /Vedant-S/kubectlconfig | grep php-webserver
  then
    echo "already running"
  else
    sudo kubectl create -f /Vedant-S/deply/php-deply.yaml --kubeconfig /Vedant-S/kubectlconfig
  fi
else
echo "no php code from developer to host"
fi


 sleep 60
htmlpod=$(sudo kubectl get pod -l app=html-webserver -o jsonpath="{.items[0].metadata.name}" --kubeconfig /Vedant-S/kubectlconfig)
sudo kubectl cp /Vedant-S/html/*   $htmlpod:/usr/local/apache2/htdocs --kubeconfig /Vedant-S/kubectlconfig
phppod=$(sudo kubectl get pod -l app=php-webserver -o jsonpath="{.items[0].metadata.name}" --kubeconfig /Vedant-S/kubectlconfig)
sudo kubectl cp /Vedant-S/php/* $phppod:/var/www/html --kubeconfig /Vedant-S/kubectlconfig
''')
      }
}


job("task6_jb3"){
        description("Testing applications")
        
        triggers {
                upstream {
    upstreamProjects("task6_jb2")
    threshold("Fail")
   } 
        }
        steps {
        shell('''status=$(curl -o /dev/null -sw "%{http_code}" http://192.168.99.100:30001/web.html)
 if [[ $status == 200 ]]
then
echo "Running Good"
else
echo " not Running good"
fi

status=$(curl -o /dev/null -sw "%{http_code}" http://192.168.99.100:30002/web1.php)
 if [[ $status == 200 ]]
then
echo "Running Good"
else
echo " not Running good"
fi''')
      }
}

job("task6_jb4"){
        description("email notification")
         triggers {
                upstream {
    upstreamProjects("task6_jb3")
    threshold("Fail")
   } 
         }
        steps {
        shell('''status=$(curl -o /dev/null -sw "%{http_code}" http://192.168.99.100:30002/web1.php)
 if [[ $status == 200 ]]
then
echo "Running Good"
else
sudo python3 /Vedant-S/mail.py
fi
status=$(curl -o /dev/null -sw "%{http_code}" http://192.168.99.100:30001/web.html)
 if [[ $status == 200 ]]
then
echo "Running Good"
else
sudo python3 /Vedant-S/mail.py
fi''')
      }
}

buildPipelineView('DevOps-task'){
    filterBuildQueue()
    filterExecutors()
    title('CI/CD Pipeline')
    displayedBuilds(3)
    selectedJob('task6_jb1')
    alwaysAllowManualTrigger()
    showPipelineParameters()
    refreshFrequency(5)
}