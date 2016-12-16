# Peterson-Lock-In-A-Binary-Tree
The code is a generalization of the two thread Peterson lock formed by arranging a number of 2-thread Peterson locks in a binary tree. 
Each thread is assigned a leaf lock, which it shares with one other thread. 
Each lock treats one thread as thread 0 and the other as thread 1. 
In the tree-lock's acquire method, the thread acquires every two-thread Peterson lock from that thread's leaf to the root. 
The treelock's release method for the tree-lock unlocks each of the 2-thread Peterson locks that thread has acquired, 
from the root back to its leaf. 
