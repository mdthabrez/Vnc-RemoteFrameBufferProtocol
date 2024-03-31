
# Vnc - Remote Frame Buffer Protocol

Vnc - Remote Frame Buffer Protocol is a simple JAVA and WEB SOCKETS based implementation of Vnc which follows the Remote Frame Buffer Protocol (RFC - 6143) https://datatracker.ietf.org/doc/html/rfc6143 .

This Vnc has the capabilities of only screen sharing over two or more computers that are connected through LAN like your home wifi or your school computer labs. One acts as a server and all others are the clients.




## Deployment

To download this, run the following command

```bash
git clone https://github.com/mdthabrez/Vnc-RemoteFrameBufferProtocol.git
```
Computer 1
```bash
cd src/remoteserver
java ServerInitiator.java
```
Computer 2
```bash
cd src/remoteclient
java ClientInitiator.java
```

### Password for Vnc Authentication: 123456



