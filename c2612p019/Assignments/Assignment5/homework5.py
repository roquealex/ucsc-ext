import math
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

def stump_algo(Xs,Txs) :
	# 1. Define
	N = Xs.shape[0]
	Tn = np.count_nonzero(Txs-1)
	Tp = np.count_nonzero(Txs+1)
	#Tn -=1
	#print N, Tn, Tp
	if ( N != (Tn+Tp) ) :
		print "Error: number of negatives (",Tn,") plus positives (",Tp,") is not equal to total",N
		exit(1)

	# 2. Set
	An = 0 # Number of acc negative samples
	Ap = 0 # Number of acc positive samples
	I0 = Iopt = float(Tn*Tp)/(N**2)
	tao = np.asscalar(Xs[0])
	#print An, Ap, I0, Iopt, tao

	# 3.
	for i in range(1,N) : 
		#print i
		if np.asscalar(Txs[i-1]) < 0 :
			An += 1
		else :
			Ap += 1
		Icurr = ((float(An*Ap)/(An+Ap)) + (float((Tn-An)*(Tp-Ap))/(Tn+Tp-An-Ap))) / N
		#print An, Ap, Icurr
		if Icurr < Iopt :
			Iopt = Icurr
			tao = np.asscalar(Xs[i])

	# 4.
	delta = I0 - Iopt
	#print An, Ap, I0, Iopt, tao, delta
	return delta, tao








excel_file = 'short.xlsx'
#excel_file = 'Assignment_1_Data_and_Template.xlsx'


#data_frame = pd.read_csv('Height.csv')
#data_frame = pd.read_excel('Assignment_2_Data_and_Template.xlsx',sheetname='Data')
data_frame = pd.read_excel(excel_file,sheetname='Data')
#data_frame = pd.read_excel('dummy.xlsx',sheetname='Data')
data_frame["Height"] = data_frame["Height_Feet"]*12 + data_frame["Height_Inches"]
print data_frame
#X = np.array(data_frame['Height'])
X = data_frame.as_matrix(['Height'])
print X
T = np.zeros((data_frame['Gender'].count(),1),dtype=float)
for i,cl in enumerate(data_frame['Gender']) :
	T[i] = 1.0 if cl == 'Male' else -1.0
	#T[i] = i
print T

X_sorted_idx = np.argsort(X[:,0])
print X_sorted_idx

X_sorted = X[X_sorted_idx]
T_sorted = T[X_sorted_idx]

print X_sorted
#print len(X_sorted)
#print X_sorted.shape[0]

print T_sorted
#print np.count_nonzero(T_sorted+1)
#print np.count_nonzero(T_sorted-1)

delta, tao = stump_algo(X_sorted, T_sorted)
print delta, tao
#exit()

# This is the result vlidated with spreadsheet:
# 7 12 0.2275 0.0777777777778 70 0.149722222222


'''
test = np.array([[3,8,1,2,0,1]]).transpose()
print test
I = np.argsort(test[:,0])
print I
#test_sorted = test[I,:]
test_sorted = test[I,:]
print test_sorted
'''

print X

Xa = np.append(np.ones((X.shape[0],1),dtype=float),X,1)
print Xa
Xapinv = np.linalg.pinv(Xa)
print Xapinv

# Calculate the w's
W = np.dot(Xapinv, T)
print W

exit()

# Decent plot:
#alpha_cloud = 0.01
alpha_cloud = 0.25
#plt.subplot(2,2,1)
plt.scatter(X,T,color='k',marker="o",alpha=alpha_cloud)
#plt.scatter(X[index_p][:,0],X[index_p][:,1],color='b',marker="o",alpha=alpha_cloud)
plt.plot(tao,0,"kx")
#plt.axis('equal')
plt.xlabel('X1')
plt.ylabel('X2')
plt.grid()
plt.show()



exit()
#queries = pd.read_excel('short.xlsx',sheetname='Queries',header=1,names=['Height','Handspan'] )
queries = pd.read_excel(excel_file,sheetname='Queries',header=1)[['Height','Handspan']]
print queries

min_height = data_frame["Height"].min()
max_height = data_frame["Height"].max()
min_handspan = data_frame["HandSpan"].min()
max_handspan = data_frame["HandSpan"].max()

print "Max height:",max_height
print "Min height:",min_height
print "Max handspan",max_handspan
print "Min handspan",min_handspan

#print max_height-min_height 
#print max_handspan-min_handspan

# with the assignment sprteadsheet the range height bins 22 and handspan bins 20 creates an array where all the integer values are in the middle or the 0.5 values in case of the handspan

#height_bins = int(max_height-min_height + 1)
height_bins = 22
#height_bins = 8
#handspan_bins = int(max_handspan-min_handspan +1)
handspan_bins = 20
#handspan_bins = 8


print "Height bins:",height_bins
print "Handspan bins:",handspan_bins

height_width = float(max_height-min_height)/(height_bins-1)
handspan_width = float(max_handspan-min_handspan)/(handspan_bins-1)

print "height_width", height_width
print "Handspan width", handspan_width

#height_bins = 17
#handspan_bins = 14
#height_bins = 9
#handspan_bins = 4
#handspan_bins = 3

male_hist = np.zeros((height_bins,handspan_bins),int)
female_hist = np.zeros((height_bins,handspan_bins),int)

# Hist:
male_df = data_frame.query('Sex=="Male"')
female_df = data_frame.query('Sex=="Female"')

#print male_df
#print female_df
male_n = male_df['Sex'].count()
female_n = female_df['Sex'].count()
print "Number of males:",male_n
print "Number of females:",female_n
print "Total",female_n+male_n

#print male_df[['Height','HandSpan']]
features = ['Height','HandSpan']
#print male_df[features].as_matrix().mean(axis=0)
#print features
#male_means = male_df.mean()
#female_means = female_df.mean()
male_means = male_df[features].as_matrix().mean(axis=0)
female_means = female_df[features].as_matrix().mean(axis=0)

#numpy.cov(m, y=None, rowvar=True, bias=False, ddof=None, fweights=None, aweights=None)[source]
male_covar = np.cov(male_df[features], rowvar=False, ddof=1)
female_covar = np.cov(female_df[features], rowvar=False, ddof=1)

print male_means
print female_means

print male_covar
print female_covar

print data_frame[features]
X = np.array(data_frame[features])
#T = np.array([ 1.0 if cl == 'Male' else -1.0 for cl in data_frame['Sex']])
#T = np.array(data_frame['Sex'])
T = np.zeros((data_frame['Sex'].count(),1),dtype=float)
for i,cl in enumerate(data_frame['Sex']) :
	T[i] = 1.0 if cl == 'Male' else -1.0
print X
print X.shape
print T
print T.shape
print type(T)
X0 = np.ones((X.shape[0],1),dtype=float)
print X0
print X0.shape
print type(X0)
Xa = np.append(X0,X,1)
print Xa
Xapinv = np.linalg.pinv(Xa)
print Xapinv

# Calculate the w's
W = np.dot(Xapinv, T)
print W
print max_height
print min_height
line = -1*W[:-1]/W[-1,0]
#line = np.array([16,1])
Xa_max = np.amax(Xa,axis=0)
Xa_min = np.amin(Xa,axis=0)
print Xa_max
print Xa_min
Xrange = np.array([Xa_min,Xa_max])
print line
#exit()
print Xrange
print Xrange.shape
Xpoints = Xrange[:,:-1]
print Xpoints
Xd = np.dot(Xpoints,line)
print Xrange[:,1]
print Xd
#exit()

index_n = np.where(T<0)[0]
index_p = np.where(T>0)[0]
print index_n
print index_p


alpha_cloud = 0.5
#plt.subplot(2,2,1)
plt.scatter(X[index_n][:,0],X[index_n][:,1],color='r',marker="o",alpha=alpha_cloud)
plt.scatter(X[index_p][:,0],X[index_p][:,1],color='b',marker="o",alpha=alpha_cloud)
#plt.plot([60,61],[22,20],"k")
plt.plot(Xrange[:,1],Xd,"k")
plt.axis('equal')
plt.xlabel('X1')
plt.ylabel('X2')
plt.grid()
plt.show()




exit()


#male_covar_inv = np.linalg.inv(male_covar)
#female_covar_inv = np.linalg.inv(female_covar)

#print male_covar_inv
#print female_covar_inv
#x = [72,21]
#x = np.array([0,0])
#mu = np.array([1,1])
#sigma = [[9,1],[1,4]]
#p = norm_pdf(x,male_means,male_covar)
#p = norm_pdf(x,mu,sigma)
#print p

#mul = np.matmul(male_covar,male_covar_inv)
#mul = np.matrix(male_covar)*np.matrix(male_covar_inv)
#mul = np.matrix(female_covar)*np.matrix(female_covar_inv)
#print mul



#exit()

for index, row in male_df.iterrows():
	#print index, row
	hist_row = bin_index(row["Height"],height_bins,min_height,max_height)
	hist_col = bin_index(row["HandSpan"],handspan_bins,min_handspan,max_handspan)
	#male_hist[hist_row,hist_col] = hist_row*handspan_bins + hist_col + 1
	male_hist[hist_row,hist_col]+=1

print male_hist

for index, row in female_df.iterrows():
	#print index, row
	hist_row = bin_index(row["Height"],height_bins,min_height,max_height)
	hist_col = bin_index(row["HandSpan"],handspan_bins,min_handspan,max_handspan)
	#female_hist[hist_row,hist_col] = hist_row*handspan_bins + hist_col + 2
	female_hist[hist_row,hist_col]+=1

print female_hist

y = np.asfarray(range(height_bins))
# This is the bottom of the range but thats how the graph comes
#y_ticks = np.around(bin_center(y,height_bins,min_height,max_height)-height_width/2,1)
y_ticks = np.around(bin_bottom(y,height_bins,min_height,max_height),2)
print y_ticks
print np.around(bin_center(y,height_bins,min_height,max_height),2)

x = np.asfarray(range(handspan_bins))
x_ticks = np.around(bin_center(x,handspan_bins,min_handspan,max_handspan),2)
print x_ticks
print np.around(bin_bottom(x,handspan_bins,min_handspan,max_handspan),2)

# Reconstructing histogram from normal:
#male_hist_rec = np.zeros((height_bins,handspan_bins),int)
#female_hist_rec = np.zeros((height_bins,handspan_bins),int)
male_hist_rec = np.zeros((height_bins,handspan_bins))
female_hist_rec = np.zeros((height_bins,handspan_bins))
for row in range(height_bins) :
	for col in range(handspan_bins) :
		heigth = bin_center(row,height_bins,min_height,max_height)
		handspan = bin_center(col,handspan_bins,min_handspan,max_handspan)
		male_pdf = norm_pdf([heigth,handspan],male_means,male_covar)
		female_pdf = norm_pdf([heigth,handspan],female_means,female_covar)
		male_hist_rec[row,col]=male_n*male_pdf*height_width*handspan_width
		female_hist_rec[row,col]=female_n*female_pdf*height_width*handspan_width

print np.round(male_hist_rec)
print np.round(female_hist_rec)

#exit()

# Fix in the formatting of Handspan that has leading spaces
queries['Handspan'] = queries['Handspan'].str.strip()
for index, row in queries.iterrows():
	#print row.as_matrix()
	rownum = pd.to_numeric(row).astype(float)
	print rownum
	#xval = pd.to_numeric(row).astype(float).as_matrix()
	print rownum.as_matrix()
	hist_row = bin_index(rownum["Height"],height_bins,min_height,max_height)
	hist_col = bin_index(rownum["Handspan"],handspan_bins,min_handspan,max_handspan)
	print "row",hist_row
	print "col",hist_col
	male_sample = male_hist[hist_row,hist_col]
	female_sample = female_hist[hist_row,hist_col]
	print "male_sample",male_sample
	print "female_sample",female_sample

	try :
		p_female_hist = ( float(female_sample)
				/ (female_sample+male_sample) )
	except ZeroDivisionError as err:
		print "WARNING:",err
		p_female_hist = float('NaN')
	print "prob hist",p_female_hist

	male_pdf = norm_pdf(rownum.as_matrix(),male_means,male_covar)
	female_pdf = norm_pdf(rownum.as_matrix(),female_means,female_covar)
	p_female_norm = ( float(female_n*female_pdf)
			/ (female_n*female_pdf+male_n*male_pdf) )
	print "prob norm",p_female_norm
	

#exit()

#plot_histogram(male_hist, female_hist,max_height,min_height,max_handspan,min_handspan)
#plot_histogram(np.round(male_hist_rec,1), np.round(female_hist_rec,1),max_height,min_height,max_handspan,min_handspan)


'''

#z = np.zeros((height_bins,handspan_bins),int)
#for i in x :
#	for j in y :
#		z[j,i] = j*handspan_bins + i + 1
#print z

#male_hist[2,1] = 2
male_rows,male_cols = np.nonzero(male_hist)
male_val = male_hist[male_rows,male_cols]
print male_rows,male_cols
print male_val

#female_hist[2,1] = 5
female_rows,female_cols = np.nonzero(female_hist)
female_val = female_hist[female_rows,female_cols]
print female_rows,female_cols
print female_val

#row, col = male_hist.shape
#print row
#print col


#y = np.asfarray(range(height_bins))
#x = np.asfarray(range(handspan_bins))

#y_ticks = np.around(bin_center(y,height_bins,min_height,max_height)-height_width/2,1)
#x_ticks = np.around(bin_center(x,handspan_bins,min_handspan,max_handspan)-handspan_width/2,1)


fig = plt.figure()
ax1 = fig.add_subplot(111,projection='3d')
#y_ticks = np.around(bin_bottom(y,height_bins,min_height,max_height),1)
ax1.bar3d(
	(bin_bottom(female_rows,height_bins,min_height,max_height)),
	(bin_bottom(female_cols,handspan_bins,min_handspan,max_handspan)),
	np.zeros(len(female_val),int),
	height_width,
	handspan_width,
	female_val,
	color='r',
	alpha=0.5)

ax1.bar3d(
	(bin_bottom(male_rows,height_bins,min_height,max_height)),
	(bin_bottom(male_cols,handspan_bins,min_handspan,max_handspan)),
	np.zeros(len(male_val),int),
	height_width,
	handspan_width,
	male_val,
	color='b',
	alpha=0.5)

plt.show()

#print male_df
#print female_df
#print data_frame.query('Sex=="Male"') 
#for data in data_frame.query('Sex=="Male"') :
#	print data
	#male_bin_array[bin_index(data,num_bins,min_height,max_height)]+=1

'''


'''
fig = plt.figure()
ax1 = fig.add_subplot(111,projection='3d')
X, Y = np.meshgrid(x, y)
Zhandspan_binshandspan_bino = np.zeros((height_bins,handspan_bins),int)
print X.ravel()
print Y
print Z

#ax1.plot_surface(X, Y, Z)
#dx = np.ones((height_bins,handspan_bins))/2
#dy = np.ones((height_bins,handspan_bins))/2
dx = 1
dy = 1
dz = z
ax1.bar3d(X.ravel(), Y.ravel(), Z.ravel(), dx, dy, dz.ravel(), color='b',alpha=0.5)

secx= [1,2]
secy =[2,3]
secz =[0,0]
valz =[20,21]
valz2 =[3,5]
ax1.bar3d(secx, secy, secz, 1, 1, valz, color='r',alpha=0.5)
plt.show()
'''




