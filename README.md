

# CorDapp Name and lastname example

Demo to do the transaction on corda network by storing name and lastname on Blockchain node.

## Running tests inside IntelliJ

We recommend editing your IntelliJ preferences so that you use the Gradle runner - this means that the quasar utils
plugin will make sure that some flags (like ``-javaagent`` - see below) are
set for you.

To switch to using the Gradle runner:

* Navigate to ``Build, Execution, Deployment -> Build Tools -> Gradle -> Runner`` (or search for `runner`)
  * Windows: this is in "Settings"
  * MacOS: this is in "Preferences"
* Set "Delegate IDE build/run actions to gradle" to true
* Set "Run test using:" to "Gradle Test Runner"

If you would prefer to use the built in IntelliJ JUnit test runner, you can run ``gradlew installQuasar`` which will
copy your quasar JAR file to the lib directory. You will then need to specify ``-javaagent:lib/quasar.jar``
and set the run directory to the project root directory for each test.

## Running the nodes

You can build the nodes by opening the build.gradle file. And execute the deploy nodes task. Then, Go to the build/nodes and run ./runnodes in cmd.

## Interacting with the nodes

### Shell

When started via the command line, each node will display an interactive shell:

    Welcome to the Corda interactive shell.
    Useful commands include 'help' to see what is available, and 'bye' to shut down the node.
    
    Tue Nov 06 11:58:13 GMT 2018>>>

You can use this shell to interact with your node. For example, enter `run networkMapSnapshot` to see a list of 
the other nodes on the network:

    Tue Nov 06 11:58:13 GMT 2018>>> run networkMapSnapshot
    [
      {
      "addresses" : [ "localhost:10002" ],
      "legalIdentitiesAndCerts" : [ "O=Notary, L=London, C=GB" ],
      "platformVersion" : 3,
      "serial" : 1541505484825
    },
      {
      "addresses" : [ "localhost:10005" ],
      "legalIdentitiesAndCerts" : [ "O=PartyA, L=London, C=GB" ],
      "platformVersion" : 3,
      "serial" : 1541505382560
    },
      {
      "addresses" : [ "localhost:10008" ],
      "legalIdentitiesAndCerts" : [ "O=PartyB, L=New York, C=US" ],
      "platformVersion" : 3,
      "serial" : 1541505384742
    }
    ]
    
    Tue Nov 06 12:30:11 GMT 2018>>> 

You can find out more about the node shell [here](https://docs.corda.net/shell.html).

      
     Wed Aug 28 10:58:39 IST 2019>>> flow list
     com.template.flows.Initiator
     com.template.flows.NameFlow
     net.corda.core.flows.ContractUpgradeFlow$Authorise
     net.corda.core.flows.ContractUpgradeFlow$Deauthorise
     net.corda.core.flows.ContractUpgradeFlow$Initiate
     
 Check the flow name as NameFlow 
    
       Wed Aug 28 10:59:34 IST 2019>>> flow start NameFlow name: kumar, lastname: Tiwari, party: partyA     

 To Check the states of each node use this command
 
        Wed Aug 28 18:50:02 IST 2019>>> run vaultQuery contractStateType: com.template.states.NameState
        

### API EndPoints (Checked in postman)

1.Get Method to display all peers in network: http://localhost:8080/name/api/peers

        {
            "peers": [
                {
                    "commonName": null,
                    "organisationUnit": null,
                    "organisation": "PartyA",
                    "locality": "London",
                    "state": null,
                    "country": "GB",
                    "x500Principal": {
                        "name": "O=PartyA,L=London,C=GB",
                        "encoded": "MC8xCzAJBgNVBAYTAkdCMQ8wDQYDVQQHDAZMb25kb24xDzANBgNVBAoMBlBhcnR5QQ=="
                    }
                },
                {
                    "commonName": null,
                    "organisationUnit": null,
                    "organisation": "PartyC",
                    "locality": "Paris",
                    "state": null,
                    "country": "FR",
                    "x500Principal": {
                        "name": "O=PartyC,L=Paris,C=FR",
                        "encoded": "MC4xCzAJBgNVBAYTAkZSMQ4wDAYDVQQHDAVQYXJpczEPMA0GA1UECgwGUGFydHlD"
                    }
                }
            ]
        }

2.Get Method to show your node details : http://localhost:8080/name/api/peer

        {
            "peer": {
                "commonName": null,
                "organisationUnit": null,
                "organisation": "PartyB",
                "locality": "New York",
                "state": null,
                "country": "US",
                "x500Principal": {
                    "name": "O=PartyB,L=New York,C=US",
                    "encoded": "MDExCzAJBgNVBAYTAlVTMREwDwYDVQQHDAhOZXcgWW9yazEPMA0GA1UECgwGUGFydHlC"
                }
            }
        }
 
 3.Post method to deploy the node details :http://localhost:8080/name/api/post-name
 
        name : Prateek7
        lastname : Tiwari
        partyName: O=PartyA,L=London,C=GB
        
        Output:- 
        Transaction id E2D8B49F69C47A1235FBE50330AB808B0F246177FDF8095CC585C6B4DC80803C committed to ledger.   


                 
### Client

`clients/src/main/kotlin/com/template/Client.kt` defines a simple command-line client that connects to a node via RPC 
and prints a list of the other nodes on the network.


