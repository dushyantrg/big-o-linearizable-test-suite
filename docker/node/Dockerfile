# See https://github.com/jgoerzen/docker-debian-base
# See https://hub.docker.com/r/jgoerzen/debian-base-standard
FROM arm64v8/debian:bullseye

# I think this is a bug--debian-base-setup crashes because policy-rc.d isn't
# present in this image, and if you create it, exim crashes... do we actually NEED this? Maybe not...
#RUN mkdir /usr/sbin/policy-rc.d
#RUN run-parts --exit-on-error --verbose /usr/local/debian-base-setup

RUN apt-get update

# Basic system stuff
RUN apt-get install -y apt-transport-https

# Install packages
RUN apt-get -qy update && \
    apt-get -qy install \
        dos2unix \
        openssh-server \
        pwgen

# When run, boot-debian-base will call this script, which will copy the
# authorized_keys file into root's .ssh
ADD setup-authorized-keys.sh /usr/local/preinit/03-setup-authorized-keys
RUN chmod +x /usr/local/preinit/03-setup-authorized-keys

# Configure SSHD
RUN sed -i "s/#PermitRootLogin prohibit-password/PermitRootLogin yes/g" /etc/ssh/sshd_config

# Enable SSH server
ENV DEBBASE_SSH enabled

# Install Jecci deps
RUN apt-get install -qy build-essential bzip2 curl faketime iproute2 \
    iptables iputils-ping libzip4 logrotate man man-db net-tools ntpdate \
    psmisc rsyslog sudo tar unzip vim wget ca-certificates

EXPOSE 22

# For some db like postgres, starting the db as root is not allowed
# So add a user here
RUN groupadd jecci &&\
    useradd -m jecci -g jecci -s /bin/bash &&\
    echo "jecci:123456" | /usr/sbin/chpasswd && chage -M 99999 jecci &&\
    echo "jecci ALL=(ALL) NOPASSWD:ALL" >>/etc/sudoers &&\
    mkdir -p /home/jecci/.ssh

#install jre 20
RUN sudo apt install software-properties-common -y
RUN sudo apt update
RUN wget -O- https://apt.corretto.aws/corretto.key | sudo apt-key add -
RUN add-apt-repository 'deb https://apt.corretto.aws stable main'
RUN  apt-get update; sudo apt-get install -y java-20-amazon-corretto-jdk

COPY bootscript.sh /tmp/bootscript.sh
RUN chmod +x /tmp/bootscript.sh

COPY bigo-0.1-all-optimized.jar /usr/bin/bigo-raft.jar

COPY startscript.sh /usr/bin/startscript.sh
RUN chmod +x /usr/bin/startscript.sh


CMD ["/tmp/bootscript.sh"]