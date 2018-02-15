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
    }
}

