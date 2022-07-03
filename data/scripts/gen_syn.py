import numpy as np
import sys
from collections import defaultdict
import heapq
import csv
import os

# sys.argv: 1) 0 5000 2) 1 10000
np.random.seed(0)
DATA_PATH = "./"
LAYOUT_PATH = os.path.join(DATA_PATH, "layouts")
DST_PATH = os.path.join(DATA_PATH, "transactions")

def get_from_layout(layout_name):
   rack_cnt, picker_cnt, robot_cnt = 0, 0, 0
   f = open(os.path.join(LAYOUT_PATH, layout_name), "r")
   line = f.readline()
   row = len(line.strip())
   col = 0
   while line:
      col += 1
      rack_cnt += line.count('R')
      picker_cnt += line.count('P')
      robot_cnt += line.count('A')
      line = f.readline()
   f.close()
   print("%d x %d" % (row, col))
   return rack_cnt, picker_cnt, robot_cnt


layout_file = ["synA.txt", "synB.txt"][int(sys.argv[1])]
N_TASKS = 5000 if len(sys.argv) == 2 else int(sys.argv[2])
N_TIMESTEP = 100
layout_name = layout_file.split(".")[0]
N_RACKS, N_PICKERS, N_ROBOTS = get_from_layout(layout_file)
print("%d racks, %d pickers, %d robot, %d tasks" % (N_RACKS, N_PICKERS, N_ROBOTS, N_TASKS))
PICKING_TIME_LOWERBOUND = 20
PICKING_TIME_UPPERBOUND = 60

data = np.zeros(shape=(N_TASKS, 4), dtype=np.int32)
data[:, 0] = np.arange(0, N_TASKS, dtype=np.int32)
data[:, 1] = np.random.randint(0, N_RACKS, N_TASKS, dtype=np.int32)
data[:, 2] = np.random.randint(PICKING_TIME_LOWERBOUND, PICKING_TIME_UPPERBOUND + 1, N_TASKS, dtype=np.int32)
data[:, 3] = np.sort(np.random.randint(0, N_TIMESTEP, N_TASKS, dtype=np.int32))
np.savetxt(os.path.join(DST_PATH, f"tasks_{layout_name}.csv"), data, fmt="%d", delimiter=',')


# calculate a suitable rack_to_picker map
# calculate all racks time
rack_time = defaultdict(int)
for i in range(N_TASKS):
   rack_time[data[i, 1]] += data[i, 2]

rack_pickers = np.zeros(shape=N_RACKS, dtype=np.int32)

time_picker = [(0, i) for i in range(N_PICKERS)]
heapq.heapify(time_picker)
for rack_id in rack_time:
   duration = rack_time[rack_id]
   single_time_picker = heapq.heappop(time_picker)
   rack_pickers[rack_id] = single_time_picker[1]
   heapq.heappush(time_picker, (single_time_picker[0] + duration, single_time_picker[1]))
print(heapq.nlargest(1, time_picker))
print(heapq.nsmallest(1, time_picker))

writer = csv.writer(open(os.path.join(DST_PATH, 'rackToPickers_%s.csv' % layout_name), 'w', encoding='utf-8', newline=''))
for i in range(N_RACKS):
   writer.writerow([i, rack_pickers[i]])
