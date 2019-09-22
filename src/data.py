import pandas as pd
import matplotlib.pyplot as plt

seq = [1,5,10,25,50,75,100,150,300,500,800,1000,2000]
speed = [1.78,2.48,2.51,2.302,2.33,1.957,1.90,1.77,1.575,1.536,1.58,1.494,1.16]

dataset = [20,50,80,120,150,180]
sequential = [0.186,0.686,1.172,2.037,2.749,3.143]
parallel = [0.165,0.384,0.567,0.865,1.234,1.672]
speed_up = [1.127,1.78,2.067,2.355,2.100,1.879]

arch2_seq = [0.093, 0.313, 0.475, 0.752, 0.920, 1.07]
arch2_parl = [0.051, 0.115, 0.168,0.252,0.340,0.452]
arch2_speedup = [1.82, 2.72, 2.827, 2.98,2.70, 2.36]
arch2_speed = [2.21,2.67,2.72,2.64,2.57,2.38,2.25,2.1,2.03,1.88,1.75,1.73,1.56]

plt.figure(1)
plt.plot(seq,speed)
plt.xlabel('SequentialCutOff (1x10^3)')
plt.ylabel('Speedup')
plt.figure(2)
plt.plot(dataset, sequential, label = "Sequential run time")
plt.plot(dataset, parallel, label = "Paralle run time")
plt.legend(loc = 'best')
plt.xlabel('Dataset Size')
plt.ylabel('Run Time(s)')
plt.figure(3)
plt.plot(dataset, speed_up)
plt.xlabel('Dataset Size')
plt.ylabel('Speed Up')

#new achitecture
plt.figure(4)
plt.plot(dataset, arch2_seq, label = "Sequential Run Time")
plt.plot(dataset, arch2_parl, label = "Parallel Run Time")
plt.legend(loc="best")
plt.xlabel('Dataset Size')
plt.ylabel('Run Time(s)')
plt.figure(5)
plt.plot(dataset, arch2_speedup)
plt.xlabel('Dataset Size')
plt.ylabel('Speed Up')
plt.figure(6)
plt.plot(dataset, sequential, label = "2 Core Achitecture Sequential")
plt.plot(dataset, parallel, label = "2 Core Achitecture Parallel")
plt.plot(dataset, arch2_seq, label = "4 Core Achitecture Sequential")
plt.plot(dataset, arch2_parl, label = "4 Core Achitecture Parallel")
plt.legend(loc='best')
plt.xlabel('Dataset Size')
plt.ylabel('Run Time(s)')
plt.figure(7)
plt.plot(dataset, speed_up, label = "2 Core Speed-up")
plt.plot(dataset, arch2_speedup, label = "4 Core Speed-up")
plt.xlabel('Dataset Size')
plt.ylabel('Speed Up')
plt.legend(loc='nw')
plt.figure(8)
plt.plot(seq,arch2_speed)
plt.xlabel('SequentialCutOff (1x10^3)')
plt.ylabel('Speedup')











