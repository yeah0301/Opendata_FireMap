#!/usr/bin/python

"""
Simple example of setting network and CPU parameters
NOTE: link params limit BW, add latency, and loss.
There is a high chance that pings WILL fail and that
iperf will hang indefinitely if the TCP handshake fails
to complete.
"""

from mininet.topo import Topo
from mininet.net import Mininet
from mininet.node import CPULimitedHost,RemoteController
from mininet.link import TCLink
from mininet.util import dumpNodeConnections
from mininet.log import setLogLevel
from mininet.cli import CLI
import httplib  
import json  

class StaticFlowPusher(object):  
  
    def __init__(self, server):  
        self.server = server  
  
    def get(self, data):  
        ret = self.rest_call({}, 'GET')  
        return json.loads(ret[2])  
  
    def set(self, data):  
        ret = self.rest_call(data, 'POST')  
        return ret[0] == 200  
  
    def remove(self, objtype, data):  
        ret = self.rest_call(data, 'DELETE')  
        return ret[0] == 200  
  
    def rest_call(self, data, action):  
        path = '/wm/staticflowentrypusher/json'  
        headers = {  
            'Content-type': 'application/json',  
            'Accept': 'application/json',  
            }  
        body = json.dumps(data)  
        conn = httplib.HTTPConnection(self.server, 8080)  
        conn.request(action, path, body, headers)  
        response = conn.getresponse()  
        ret = (response.status, response.reason, response.read())  
        print ret  
        conn.close()  
        return ret  

pusher = StaticFlowPusher('192.168.119.129') 
flow1 = {  
    'switch':"00:00:00:00:00:00:00:04",  
    "name":"flow-mod-1",
    "vlan_id":"1",
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"1",  
    "active":"true",  
    "actions":"strip-vlan,output=2,output=3"  
    }  
flow11 = {  
    'switch':"00:00:00:00:00:00:00:04",  
    "name":"flow-mod-11",
    "vlan_id":"2",
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"1",  
    "active":"true",  
    "actions":"strip-vlan,output=4,output=5"  
    }  
      
flow2 = {  
    'switch':"00:00:00:00:00:00:00:04",  
    "name":"flow-mod-2",  
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"2",  
    "active":"true",  
    "actions":"set_vlan_id=1,output=1, output=3"   
    }  
  
flow3 = {  
    'switch':"00:00:00:00:00:00:00:04",  
    "name":"flow-mod-3",  
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"3",  
    "active":"true",  
    "actions":"output=2,set_vlan_id=1,output=1"   
    }  
  
flow4 = {  
    'switch':"00:00:00:00:00:00:00:04",  
    "name":"flow-mod-4",  
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"4",  
    "active":"true",  
    "actions":"output=5,set_vlan_id=2,output=1"    
    }  
      
flow5 = {  
    'switch':"00:00:00:00:00:00:00:04",  
    "name":"flow-mod-5",  
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"5",  
    "active":"true",  
    "actions":"output=4,set_vlan_id=2,output=1"  
    }
flow6 = {  
    'switch':"00:00:00:00:00:00:00:05",  
    "name":"flow-mod-6",
    "vlan_id":"1",
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"1",  
    "active":"true",  
    "actions":"strip-vlan,output=2,output=3"  
    }  
flow61 = {  
    'switch':"00:00:00:00:00:00:00:05",  
    "name":"flow-mod-61",  
    "vlan_id":"2",
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"1",  
    "active":"true",  
    "actions":"strip-vlan,output=4,output=5"  
    }  
      
flow7 = {  
    'switch':"00:00:00:00:00:00:00:05",  
    "name":"flow-mod-7",  
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"2",  
    "active":"true",  
    "actions":"set_vlan_id=1,output=1,output=3"   
    }  
  
flow8 = {  
    'switch':"00:00:00:00:00:00:00:05",  
    "name":"flow-mod-8",  
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"3",  
    "active":"true",  
    "actions":"output=2,set_vlan_id=1,output=1"   
    }  
  
flow9 = {  
    'switch':"00:00:00:00:00:00:00:05",  
    "name":"flow-mod-9",  
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"4",  
    "active":"true",  
    "actions":"output=5,set_vlan_id=2,output=1"    
    }  
      
flow10 = {  
    'switch':"00:00:00:00:00:00:00:05",  
    "name":"flow-mod-10",  
    "cookie":"0",  
    "priority":"32768",  
    "ingress-port":"5",  
    "active":"true",  
    "actions":"output=4,set_vlan_id=2,output=1"  
    }   

pusher.set(flow1)
pusher.set(flow11)
pusher.set(flow2)
pusher.set(flow3)
pusher.set(flow4)
pusher.set(flow5)
pusher.set(flow6)
pusher.set(flow61)
pusher.set(flow7)
pusher.set(flow8)
pusher.set(flow9)
pusher.set(flow10)
   


class SingleSwitchTopo(Topo):
    "Single switch connected to n hosts."
    def __init__(self, n=2, **opts):
        Topo.__init__(self, **opts)
        switch1 = self.addSwitch('s1',)
	switch2 = self.addSwitch('s2',)
        switch3 = self.addSwitch('s3',)
        switch4 = self.addSwitch('s4',)
        switch5 = self.addSwitch('s5',)
        switch6 = self.addSwitch('s6',)
        switch7 = self.addSwitch('s7',) 
        
        # Each host gets 50%/n of system CPU
        host1 = self.addHost('h1',)
        host2 = self.addHost('h2',)
        host3 = self.addHost('h3',)
        host4 = self.addHost('h4',)
        host5 = self.addHost('h5',)
        host6 = self.addHost('h6',)
        host7 = self.addHost('h7',)
        host8 = self.addHost('h8',)   
            

	# 10 Mbps, 5ms delay, 10% loss
        self.addLink(switch1,switch4,bw=100)
        self.addLink(switch1,switch5,bw=100)
        self.addLink(switch4,host5,bw=100)  
        self.addLink(switch4,host6,bw=100)
        self.addLink(switch5,host7,bw=100)
        self.addLink(switch5,host8,bw=100)         
            
        self.addLink(switch2, switch4,
                         bw=10, loss=5)
        self.addLink(switch4, switch6,
                         bw=10, loss=5)
        self.addLink(switch3, switch5,
                         bw=10, loss=5) 
        self.addLink(switch5, switch7,
                         bw=10, loss=5)
        self.addLink(switch2, host1,
                         bw=10, loss=5)
        self.addLink(switch3, host2,
                         bw=10, loss=5)
        self.addLink(switch6, host3,
                         bw=10, loss=5)     
        self.addLink(switch7, host4,
                         bw=10, loss=5)  
def perfTest():
    "Create network and run simple performance test"
    topo = SingleSwitchTopo( n=8 )
    net = Mininet( topo=topo,
                   host=CPULimitedHost, link=TCLink,
                   autoStaticArp=True, controller=None )
    net.addController('controller',controller=RemoteController,ip='127.0.0.1',port=6633)


    net.start()
    net.pingAll()
    print "Dumping host connections"
    dumpNodeConnections(net.hosts)
    print "Testing bandwidth (h1,h3) (h5,h7)"
    h1, h3 = net.getNodeByName('h1', 'h3')
    #net.iperf( ( h1, h3 ), l4Type='UDP' )
    h5, h7 = net.getNodeByName('h5', 'h7')
    #net.iperf( ( h5, h7 ), l4Type='UDP' )
    
    h1.popen('iperf -s -u -i 1','127.0.0.0')
    h3.cmdPrint('iperf -c'+h1.IP()+' -u -t 10 -i 1 -b 100m')
    h5.popen('iperf -s -u -i 1','127.0.0.0')
    h7.cmdPrint('iperf -c'+h5.IP()+' -u -t 10 -i 1 -b 100m')
    

    #h1.popen('curl -d')
    CLI(net)
    
    
    net.stop()
    

if __name__ == '__main__':
    setLogLevel('info')
    perfTest()
