#!groovy
node {
    def devAuthor = '127.0.0.1'

    def group = 'ctp.lottery'
    def project = 'lottery-content'
    def artifact = 'pli'

    def runner = (env.BRANCH_NAME == 'develop' || env.BRANCH_NAME == 'master') ? true : false

    if (runner) {
        // get latest from GIT
        checkout scm
        def v = version(readFile('pom.xml'))
        // Build & Test
        stage 'Build & Test'
        echo "Building version ${project}-${v}"
        timeout(time: 10, unit: 'MINUTES') {
            try {
            	mvn '--version'
            	mvn 'help:effective-settings'
            	wrap([$class: 'ConfigFileBuildWrapper', managedFiles: [[fileId: '3355ec39-5cd9-464f-ada8-1be44782dc63', replaceTokens: false, targetLocation: '', variable: 'MAVEN_SETTINGS']]]) {
                    echo "$MAVEN_SETTINGS"
                    mvn '-s $MAVEN_SETTINGS clean verify -B -Dconcurrency=1'
                }
            } catch (err) {
            	echo 'error occurred'
                step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/*.xml'])
                throw err
            } finally {
                step([$class: 'Mailer', notifyEveryUnstableBuild: true, recipients: 'camelot@adobe.com', sendToIndividuals: true])
            }
        }

        // Archive Artifact & JUnit results
        stage 'Archive Artifact & JUnit results'
        echo 'archiving zip'
        step([$class: 'ArtifactArchiver', artifacts: '**/target/*.zip', fingerprint: true])
        stash includes: '**/target/*.zip', name: 'target-site'
        //step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/*.xml'])

        // Automation Test
        stage 'Automation Test'
        echo 'run Automation Test'

        // Accessibility Checker
        stage 'Accessibility Checker'
        echo 'run Accessibility Checker'

        // Sonar report
        //stage('SonarQube analysis') {
        //    withSonarQubeEnv('SonarQube') {
        //        mvn 'sonar:sonar'
        //    }
        //}

        // Deploy to Nexus
        stage 'Deploy to Artifactory'
        wrap([$class: 'ConfigFileBuildWrapper', managedFiles: [[fileId: '3355ec39-5cd9-464f-ada8-1be44782dc63', replaceTokens: false, targetLocation: '', variable: 'MAVEN_SETTINGS']]]) {
            mvn '-s $MAVEN_SETTINGS clean deploy -B -Dconcurrency=1 -Dmaven.test.skip=true -Dusername=admin -Dpassword=admin'
        }

        // Deploy on Author
        stage 'Deploy on Author'
        switch (env.BRANCH_NAME) {
            case "master":
                unstash 'target-site'
                sh 'ls -al'
                sh 'ls -al ui.apps'
                sh 'ls -al ui.apps/target'
                sh "curl -u Jenkins:Jenkins00# -F file=@\"content/target/${artifact}.ui.app-${v}.zip\" -F force=true -F install=true http://${devAuthor}:4502/crx/packmgr/service.jsp"
                break
            //case "master":
            //    unstash 'target-site'
            //    sh "curl -u Jenkins:Jenkins00# -F file=@\"content/target/${project}-${v}.zip\" -F force=true -F install=true http://${uatAuthor}:4502/crx/packmgr/service.jsp"
            //    break
            default:
                break
        }

        // Replicate on Publish
        stage 'Replicate on Publish'
        switch (env.BRANCH_NAME) {
            case "develop":
                unstash 'target-site'
		sh "curl -u Jenkins:Jenkins00# -X POST -d cmd=\"replicate\" http://${devAuthor}:4502/crx/packmgr/service/script.html/etc/packages/${group}/${project}-${v}.zip"
                break
        //    case "master":
        //        unstash 'target-site'
		//sh "curl -u Jenkins:Jenkins00# -X POST -d cmd=\"replicate\" http://${uatAuthor}:4502/crx/packmgr/service/script.html/etc/packages/${group}/${project}-${v}.zip"
        //        break
            default:
                break
        }
    }
}

def mvn(args) {
    sh "${tool 'maven-local'}/bin/mvn ${args}"
}

@NonCPS
def version(text) {
    def matcher = text =~ '<version>(.+)</version>'
    matcher ? matcher[0][1] : null
}
