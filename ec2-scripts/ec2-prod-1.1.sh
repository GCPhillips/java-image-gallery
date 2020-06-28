#!/usr/bin/bash

yum -y update
yum install -y java-11-openjdk-devel git jq
amazon-linux-extras install -y java-openjdk11
amazon-linux-extras install -y nginx1
su ec2-user -l -c 'curl -s "https://get.sdkman.io" | bash && source .bashrc && sdk install gradle'

cd /home/ec2-user
git clone https://github.com/gcphillips/java-image-gallery.git
chown -R ec2-user:ec2-user java-image-gallery

CONFIG_BUCKET="s3://edu.au.gcp0015.image-gallery-config"
aws s3 cp ${CONFIG_BUCKET}/nginx/nginx.conf /etc/nginx/nginx.conf
aws s3 cp ${CONFIG_BUCKET}/nginx/default.d/image_gallery.conf /etc/nginx/default.d/image_gallery.conf
aws --region "us-east-2" rds describe-db-instances --db-instance-identifier m5-image-gallery-db | jq -r '.DBInstances' | jq - r '.[0].Endpoint.Address' > .dbhostname

systemctl stop postfix
systemctl disable postfix
systemctl start nginx
systemctl enable nginx

su ec2-user -l -c 'cd ~/java-image-gallery && ./start' > /var/log/image_gallery.log 2>&1 &
