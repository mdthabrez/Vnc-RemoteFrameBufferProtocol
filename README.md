# Vnc - Remote Frame Buffer Protocol

Vnc - Remote Frame Buffer Protocol is a simple JAVA and WEB SOCKETS based implementation of Vnc which follows the Remote Frame Buffer Protocol (RFC - 6143) https://datatracker.ietf.org/doc/html/rfc6143 .

This Vnc has the capabilities of only screen sharing over two or more computers that are connected through LAN like your home wifi or your school computer labs. One acts as a server and all others are the clients.


## Technologies and Concepts Used

**Java**  
**Web Sockets**  
**Multi-Threading**  
**Java Swing GUI**  



## Features

- **Screen Sharing:** Enables real-time sharing of the screen between multiple computers connected over a LAN (Local Area Network).



## What did I learn in this project?

-  Honed my skills in Java programming by implementing both the server and client components of the VNC application.
- I explored WebSocket as a communication protocol for establishing real-time connections between the server and clients.
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
## 🔗 Contact
Mohammed Thabrez G

[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/md-thabrez/)




