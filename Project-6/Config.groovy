job('G Job1') {
    description('Job1')
	scm {
        	github('Vedant-S/Sample-Website','master')
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
