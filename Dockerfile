FROM gradle:7.3.3-jdk17 as builder
USER root
WORKDIR /builder
ADD . /builder
RUN ["gradle", "clean", "MyBigJar"]

FROM openjdk:17-oracle
WORKDIR /simpleimplementationblockchain
COPY --from=builder /builder/build/libs/SimpleImplemetationBlockchain-1.0.jar .
ENTRYPOINT ["java", "-jar", "SimpleImplementationBlockchain-1.0.jar"]