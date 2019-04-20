#!/bin/bash

#ec2-user

sudo -u ec2-user echo "#Userdata generated:" >> ~ec2-user/.bashrc

# Install wget and java
echo Install wget and java
yum -y update
yum -y install wget
yum -y install git
yum -y install java-1.8.0-openjdk-devel
rm -f /usr/lib/jvm/jdk
ln -s $(ls -tdr1 /usr/lib/jvm/java-1.8.0-openjdk-1.8* | sort -n | tail -1) /usr/lib/jvm/jdk

#Install my stuff
yum -y install tigervnc-server
yum -y install xterm
#yum -y install metacity
#yum -y install matchbox-window-manager
yum -y install links
yum -y install gcc
yum -y install vim-X11
#yum -y install gnome-terminal

yum -y install python2-pip
pip install numpy
pip install pandas
pip install matplotlib
pip install astral
pip install calmap
pip install jupyter

# Use a Linux editor such as vi to install the export line (below) into your ~/.bashrc:
echo "Use a Linux editor such as vi to install the export line (below) into your ~/.bashrc:"
#vi ~/.bashrc
#-----
#export JAVA_HOME=/usr/lib/jvm/jdk
sudo -u ec2-user \
echo "export JAVA_HOME=/usr/lib/jvm/jdk" >> ~ec2-user/.bashrc
#-----

# Download Spark to the ec2-user's home directory
pushd ~
#wget http://apache.claz.org/spark/spark-2.3.1/spark-2.3.1-bin-hadoop2.7.tgz
wget http://apache.mirrors.ionfish.org/spark/spark-2.3.3/spark-2.3.3-bin-hadoop2.7.tgz
#wget https://www-us.apache.org/dist/spark/spark-2.4.1/spark-2.4.1-bin-hadoop2.7.tgz

# Unpack Spark in the /opt directory
tar zxvf $(ls -tdr1 spark-*.tgz | tail -1) -C /opt
popd

# Create a symbolic link to make it easier to access
pushd /opt
rm -f /opt/spark
ln -fs $(ls -tdr1 /opt/spark-* | tail -1) /opt/spark
popd

#-----
sudo -u ec2-user \
echo 'export SPARK_HOME=/opt/spark' >> ~ec2-user/.bashrc
sudo -u ec2-user \
echo 'PATH=$PATH:$SPARK_HOME/bin' >> ~ec2-user/.bashrc
sudo -u ec2-user \
echo 'export PATH' >> ~ec2-user/.bashrc

echo "PySpark - Jupyter setup"
sudo -u ec2-user \
echo 'export PYSPARK_DRIVER_PYTHON=jupyter' >> ~ec2-user/.bashrc
sudo -u ec2-user \
echo "export PYSPARK_DRIVER_PYTHON_OPTS='notebook --no-browser --port=8080'" >> ~ec2-user/.bashrc
#echo 'export PYSPARK_PYTHON=python3' >> ~ec2-user/.bashrc

# This is the link to basemap:
#https://svwh.dl.sourceforge.net/project/matplotlib/matplotlib-toolkits/basemap-1.0.7/basemap-1.0.7.tar.gz

# Mate desktop
#sudo amazon-linux-extras -y mate-desktop
amazon-linux-extras install -y mate-desktop1.x
sudo -u ec2-user \
echo '#!/bin/bash' > ~ec2-user/.Xclients
sudo -u ec2-user \
echo 'exec "$(type -p mate-session)"' >> ~ec2-user/.Xclients
sudo -u ec2-user \
chmod 755 ~ec2-user/.Xclients

# Installing basemap and dependencies
yum -y install gcc-c++

#sudo pip3 install pillow
## Requires geos to be compiled
#export CXX="g++ -std=c++98"
#wget http://download.osgeo.org/geos/geos-3.4.2.tar.bz2
#tar xjvf geos-3.4.2.tar.bz2
#cd geos-3.4.2
#./configure
#make
#sudo make install
#cd ../
#unset CXX
#sudo pip3 install -U git+https://github.com/matplotlib/basemap.git
## It requeries an old version of pyproj
#sudo pip3 install pyproj==1.9.6
