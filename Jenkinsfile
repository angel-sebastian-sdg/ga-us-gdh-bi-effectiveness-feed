import java.text.SimpleDateFormat
def version = ""
def gcsPath = ""
def gcpAuthProjectId = ""
def nexusProjectPath = ""
def gcpServiceAcctCreds = ""
def datePart = ""

pipeline {
    environment{
        NEXUS_URL = ""
    }
    agent any

    stages {

        stage('Validate gcsPath param') {
            steps {
                script {
                    echo 'Starting validation'
                    if ( params.gcsPath ==~ /^gs:\/\/[a-zA-Z-_]*\/[a-zA-Z-_\/]+\/$/) {
                        result = 'valid'
                    } else {
                        error 'gcsPath is not in correct Format. Example: gs://bucket-name/subdirectory/ or gs://bucket_name/subdirectory/'
                        currentBuild.result = 'FAILURE'
                    }
                }

            }

        }

        stage('Build') {
            tools {
                jdk "jdk-1.8.101"
            }
            steps {

                echo 'Starting assembly'
                sh '''

                ${SBT} assembly -Dsbt.log.noformat=true -Dnexus.host=${NEXUS_PROD}

                '''
            }


        }
        stage('Get Version') {
            steps{
                sh '${SBT} -no-colors version > tmp.info'
                sh 'cat tmp.info |  tail -1 | cut -d " " -f 2 > version.info'
                script{
                    version = readFile('version.info').trim()

                }
                print "Build version: " + version
            }
        }
        stage('Publish') {
            tools {
                jdk "jdk-1.8.101"
            }
            steps {
                script {
                    echo 'Publish.....'
                    def status = sh(returnStdout: true, script: '${SBT} -Dsbt.log.noformat=true --J-Xms4G -J-Xmx4G -J-Xss4M  publish -Dnexus.host=${NEXUS_PROD}')
                    echo 'status is: ${status}'
                    if (status.trim().contains("destination file exists and overwrite == false")) {
                        echo "Error: Command exited with error ${status}"
                        error 'FAIL'
                        currentBuild.result = 'FAILURE'
                    } else {
                        echo "Command executed successfully, Command exited with status ${status}"
                    }
                }
            }

        }
        stage('Archive, Remove existing objects and upload to GCS') {
            steps {
                script{
                    version = readFile('version.info').trim()
                    nexusProjectPath = params.nexusProjectPath
                    datePart = new Date().format("MM-dd-yyyy")

                    gcsPath = params.gcsPath
                    gcpAuthProjectId = params.gcpAuthProjectId
                    gcpServiceAcctCreds = params.gcpServiceAcctCreds
                }

                print "Building the version: " + version
                print "GCS path : " + gcsPath


                withCredentials([file(credentialsId: gcpServiceAcctCreds, variable: 'GCLOUD_CREDS'),
                                 usernamePassword(credentialsId: 'sys-nx2jk', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {

                    withEnv(["NEXUS_URL=${NEXUS_PROD}${nexusProjectPath}/${version}/ga-us-gdh-bi-effectiveness-feed-${version}-assembly.jar", "jar_version=${version}","date=${datePart}"]) {
                        print "Getting jar from URL: " + env.NEXUS_URL

                        sh 'wget --user=${NEXUS_USER} --password=${NEXUS_PASS} ${NEXUS_URL} -O /tmp/ga-us-gdh-bi-effectiveness-feed-${jar_version}-assembly.jar'
                        sh '''
                        gcloud auth activate-service-account --key-file="$GCLOUD_CREDS" --project=${gcpAuthProjectId}
                        touch temp.csv
                        gsutil mv temp.csv ${gcsPath}
                        gsutil mv ${gcsPath} gs://scala-artifacts-archive/${date}/
                        gsutil cp /tmp/ga-us-gdh-bi-effectiveness-feed-${jar_version}-assembly.jar ${gcsPath}

                        '''
                    }


                }
            }
        }
    }

}