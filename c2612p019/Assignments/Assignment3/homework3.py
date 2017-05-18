
import os, struct
#import matplotlib as plt
import matplotlib.pyplot as plt
from array import array as pyarray
#from numpy import append, array, int8, uint8, zeros as np
import numpy as np

def load_mnist(dataset="training", digits=range(10), path='C:\\Users\\Shashi\\Downloads\\Contracts\\UCB\\UCB Ext\\Python\\MNIST_data'):
    
    """
    Adapted from: http://cvxopt.org/applications/svm/index.html?highlight=mnist
    """

    if dataset == "training":
        fname_img = os.path.join(path, 'train-images-idx3-ubyte')
        #fname_lbl = os.path.join(path, 'train-labels-idx1-ubyte.mod')
        fname_lbl = os.path.join(path, 'train-labels-idx1-ubyte.small')
    elif dataset == "testing":
        fname_img = os.path.join(path, 't10k-images.idx')
        fname_lbl = os.path.join(path, 't10k-labels.idx')
    else:
        raise ValueError("dataset must be 'testing' or 'training'")

    flbl = open(fname_lbl, 'rb')
    magic_nr, size = struct.unpack(">II", flbl.read(8))
    lbl = pyarray("b", flbl.read())
    flbl.close()

    fimg = open(fname_img, 'rb')
    magic_nr, size, rows, cols = struct.unpack(">IIII", fimg.read(16))
    img = pyarray("B", fimg.read())
    fimg.close()

    ind = [ k for k in range(size) if lbl[k] in digits ]
    N = len(ind)

    #images = np.zeros((N, rows, cols), dtype=np.uint8)
    images = np.zeros((N, rows * cols), dtype=np.uint8)
    images_small = np.zeros((N, 2), dtype=np.uint8)
    labels = np.zeros((N, 1), dtype=np.int8)
    for i in range(len(ind)):
        #images[i] = np.array(img[ ind[i]*rows*cols : (ind[i]+1)*rows*cols ]).reshape((rows, cols))
        images[i] = np.array(img[ ind[i]*rows*cols : (ind[i]+1)*rows*cols ])
        labels[i] = lbl[ind[i]]
	# mod
        images_small[i] = np.array([images[i][9*28 +16],images[i][15*28 +9]])
        #images[i][9*28 +16] = 0 # 7 diff
        #images[i][15*28 +9] = 0 # 4 diff

    #return images, labels
    return images_small, labels

#from pylab import *
#from numpy import *
#import scipy.sparse as sparse
#import scipy.linalg as linalg

#images, labels = load_mnist('training', digits=[4,7],path=os.getcwd())
X, T = load_mnist('training', digits=[4,7],path=os.getcwd())

# converting from NX28X28 array into NX784 array
#flatimages = list()
#for i in images:
#    flatimages.append(i.ravel())
#X = np.asarray(flatimages)

print("Check shape of matrix", X.shape)
print("Check Mins and Max Values",np.amin(X),np.amax(X))
print("\nCheck training vector by plotting image \n")
'''
for i in range(len(X)) :
	#plt.imshow(X[i].reshape(28, 28),interpolation='None', cmap=plt.get_cmap('gray'))
	plt.imshow(X[i].reshape(1, 2),interpolation='None', cmap=plt.get_cmap('gray'))
	plt.title(str(T[i])+str(X[i]))
	plt.title(str(T[i])+str(X[i]))
	plt.show()
'''

print X
print T

X_mean = np.mean(X,axis=0).astype(float) #Avoiding potential issues with int
print X_mean

Z = X - X_mean
print Z

C = np.cov(X, rowvar=False, ddof=1)
print C
C_z = np.cov(Z, rowvar=False, ddof=1)
print C_z
C_alt = (np.dot(Z.transpose(), Z))/(Z.shape[0]-1)
print Z.shape[0]
print C_alt

[W,V_t]=np.linalg.eigh(C);
print W
print V_t
# Adjust:
W=np.flipud(W);V_t=np.fliplr(V_t);

print W
print V_t

#print type(X)
# Verify:
test_v = V_t[:,0]
#verif = np.round(np.dot(C,test_v)/(W[0]*test_v),3)
#verif = np.dot(C,test_v)-(W[0]*test_v)
verif = np.round(np.dot(C,test_v)-(W[0]*test_v),9)
if (not np.all(verif==0)) :
	print np.all(verif==1)
	print "Verification failed:"
	print verif
	exit(0)
else :
	print "Verification Passed"

P = np.dot(Z,V_t)
#P = np.dot(Z,V_t)
print P
#print P[:,0]
#print P[:,1]




