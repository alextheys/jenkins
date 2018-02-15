#!groovy

pipeline {
    agent any
	tools {
        maven 'Maven 3.5.2'
    }
     stages {
        stage ('Initialize') {
        	steps {
        		sh 'mvn --version'
        	}
        }
        stage ('Build and test') {
	        def devAuthor = '127.0.0.1'
			def group = 'ctp.lottery'
			def project = 'lottery-content'
			def runner = (env.BRANCH_NAME == 'develop' || env.BRANCH_NAME == 'master') ? true : false

			if (runner) {
				echo "Building version ${project}-${v}"
			}
        	
        }
        
    }
}


@NonCPS
def version(text) {
    def matcher = text =~ '<version>(.+)</version>'
    matcher ? matcher[0][1] : null
}

