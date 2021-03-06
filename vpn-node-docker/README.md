Sentinel dVPN OpenVPN Node Hosting
===

This is a complete guide to enable a configure use the Sentinel dVPN OpenVPN Node on Linux with docker and share unused bandwidth to the Sentinel Network.

Setup Instructions
===

Here are the steps involved in getting a Sentinel dVPN OpenVPN Node up and running:

1. Install docker
2. Build/pull the **Sentinel dVPN** docker image
3. Configure Node


Installing the latest version of docker in Ubuntu or Debian based releases
---

Below are the instructions on installing docker:

1. Install required dependencies to get docker installation script from get.docker.com

    `sudo apt install -y curl`

    `curl -fsSL get.docker.com -o /tmp/get-docker.sh`

2. Run the downloaded script to setup & install docker

    `sudo sh /tmp/get-docker.sh`

3. Add current user to the docker group to give required access

   `sudo usermod -aG docker $USER`

Running the Sentinel dVPN Node
---

You can set up and run VPN node in two ways:

1. Quick Run - download docker image from Docker Hub
2. DIY - build your own docker image from the Sentinel code repository

### Method #1 [Download docker image from Docker Hub]

1. Navigate to the User's home directory

    `cd ~`

2. Create a new folder to pull the docker image and run it

    `mkdir -p $HOME/.sentinel`
    
3. Pull the official Sentinel docker image from Docker Hub & configure the node

    `sudo docker run -it --privileged --mount type=bind,source=$HOME/.sentinel,target=/root/.sentinel -p 3000:3000 -p 1194:1194/udp sentinelofficial/sentinel-vpn-node`

### Method #2 [Build Your Own Docker Image]

Do-it-Yourself setup by building your own docker image

1. Navigate to the root folder or anywhere that you want to set up the project and download code from the repository

    `cd ~`
2. Clone the Sentinel repository

    `git clone https://github.com/sentinel-official/sentinel.git`

3. Navigate to the folder with code to build the docker image

    `cd ~/sentinel/vpn-node-docker`

4. Build the docker image

    `sudo docker build --file Dockerfile.prod --tag sentinel-vpn-node --force-rm --no-cache .`

5. Navigate to the folder where the docker container was set up. In our case, it is the User's home directory.

    `mkdir -p $HOME/.sentinel`

6. Run the image and configure the node

    `sudo docker run -it --privileged --mount type=bind,source=$HOME/.sentinel,target=/root/.sentinel -p 3000:3000 -p 1194:1194/udp sentinel-vpn-node`

Updating an existing Sentinel dVPN OpenVPN Node
===

1. Stop the running docker container

    `sudo docker stop $(sudo docker ps -aq -f ancestor=sentinelofficial/sentinel-vpn-node)`

2. Remove the older version of docker container

    `sudo docker rm -f $(sudo docker ps -aq -f ancestor=sentinelofficial/sentinel-vpn-node)`

3. Remove the older version of docker image

    `sudo docker rmi -f $(sudo docker images -aq -f reference=sentinelofficial/sentinel-vpn-node)`

4. Set the token value to an EMPTY STRING in the config file

    Config file location: $HOME/.sentinel/config.data
    
    Example: {"token": "", "enc_method": "AES-256-CBC", "account_addr": "0x656b756e6c6335302e676c343572357232786970", "price_per_gb": 125.0}

After completing the above steps please follow the instructions on Method #1 to get the node up and running again.

Helpful docker commands to interact with the Sentinel dVPN Node
===

*  list a running Sentinel dVPN Node: 

    ```sudo docker ps```

* list both, running and stopped Sentinel dVPN Nodes:

    ```sudo docker ps -a```
    
* stop a particular Sentinel Node:

    * run ```sudo docker ps``` and copy the Container ID.
    * run ```sudo docker stop ContainerID```
 
     ###### Note: stopping a dVPN Node does not remove the container
 
* remove a particular Sentinel dVPN Node:
    ```sudo docker rm ContainerID_OR_ContainerName```

    ###### Note: The Sentinel dVPN Node must be stopped to be removed. Alternatively, you can use the `-f` flag to forcefully remove the Node if the node cannot be stopped.

* remove node using the force option `-f`:

    ```sudo docker rm -f ContainerID_OR_ContainerName```

* remove all stopped Sentinel dVPN Nodes:

    ```sudo docker rm $(sudo docker ps -a -q)```
    
* stop all the running dVPN Nodes and remove them:

    ```sudo docker stop $(sudo docker ps -a -q) && sudo docker rm $(sudo docker ps -a -q)```
