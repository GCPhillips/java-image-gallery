#!/usr/bin/bash

yum -y update
yum install -y tree python3 java-11-openjdk-devel git
amazon-linux-extras install -y java-openjdk11
amazon-linux-extras install -y nginx1
su ec2-user -l -c 'curl -s "https://get.sdkman.io | bash && source .bashrc && sdk install gradle'

cd /home/ec2-user
git clone https://github.com/gcphillips/java-image-gallery.git
chown -R ec2-user:ec2-user java-image-gallery

systemctl stop postfix
systemctl disable postfix
systemctl start nginx
systemctl enable nginx
