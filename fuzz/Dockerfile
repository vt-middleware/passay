FROM --platform=linux/amd64 maven:3.9-amazoncorretto-17
RUN yum update -y && \
  yum install -y tar && \
  yum clean all && \
  rm -rf /var/cache/yum
