# Overview

iperf is a common tool used to measure network bandwidth. This is my own version of this tool in
Java using sockets called ”Iperfer”. I will use the tools to measure the performance
of virtual networks in Mininet and explain how link characteristics and multiplexing impact performance.

## Environment Setup

A mininet VM is required to run the scripts.


## 1. Implementing Iperfer

The tool, called Iperfer, will send and receive TCP packets between a pair of hosts using
sockets.

When operating in client mode, Iperfer will send TCP packets to a specific host for a specified time window
and track how much data was sent during that time frame; it will calculate and display the bandwidth based
on how much data was sent in the elapsed time. When operating in server mode, Iperfer will receive TCP
packets and track how much data was received during the lifetime of a connection; it will calculate and
display the bandwidth based on how much data was received and how much time elapsed between received
the first and last byte of data.

### Client Mode

To operate Iperfer in client mode, it should be invoked as follows:

java Iperfer -c -h <server hostname> -p <server port> -t <time>

- -c indicates this is the iperf client which should generate data.
- Server hostname is the hostname or IP address of the iperf server which will consume data.
- Server port is the port on which the remote host is waiting to consume data; the port should be in the
    range 1024 ≤ server port ≤ 65535.
- Time is the duration in seconds for which data should be generated.

When running as a client, Iperfer will establish a TCP connection with the server and send data as quickly
as possible for time seconds. Data is sent in chunks of 1000 bytes and the data will be all zeros.

After time seconds have passed, Iperfer client will stop sending data and close the connection. Before the
program terminates, it will print a one line summary including:

- The total number of bytes sent (in kilobytes)
- The rate at which traffic could be sent (in megabits per second (Mbps))

sent=6543 KB rate=5.234 Mbps

Assume 1 kilobyte (KB) = 1000 bytes (B) and 1 megabyte (MB) = 1000 KB. As always, 1 byte
(B) = 8 bits (b).


### Server Mode

To operate Iperfer in server mode, it should be invoked as follows:

java Iperfer -s -p <listen port>

- -s indicates this is the iperf server which should consume data
- Listen port is the port on which the host is waiting to consume data; the port should be in the range
    1024 ≤ listen port ≤ 65535.

When running as a server, Iperfer will listen for TCP connections from a client and receive data as quickly
as possible until the client closes the connection. Data is read in chunks of 1000 bytes. 

After the client has closed the connection, Iperfer server will also print a one line summary that includes
the following and then terminate:

- The total number of bytes received (in kilobytes)
- The rate at which traffic could be received (in megabits per second (Mbps))

received=6543 KB rate=4.758 Mbps

## Testing

You can test Iperfer on any computer you have access to. However, be aware the certain ports may be
blocked by firewalls on end hosts or in the network, so you may not be able to test your program on all hosts
or in all networks.

You should receive the same number of bytes on the server as you sent from the client. However, the timing
on the server may not perfectly match the timing on the client. Hence, the bandwidth reported by client
and server may be slightly different; in general, they should not differ by more than 2 Mbps. Note, this
behavior mirrors the behavior of the actual iperf tool.