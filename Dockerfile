FROM maven:3.6.1-jdk-8

ENV LANG C.UTF-8

#设置spring profile或者自定义的jvm参数  ()
#ENV SERVICE_OPTS=-Dspring.profiles.active=production