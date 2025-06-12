FROM openjdk:21-jdk-slim

# Versões
ENV MAVEN_VERSION=3.9.9 \
    TOMCAT_VERSION=10.1.39

# Paths e variáveis de ambiente
ENV JAVA_HOME=/usr/local/openjdk-21 \
    MAVEN_HOME=/opt/maven \
    TOMCAT_HOME=/opt/tomcat \
    CATALINA_HOME=/opt/tomcat \
    PATH="$MAVEN_HOME/bin:$TOMCAT_HOME/bin:$JAVA_HOME/bin:$PATH"

# Dependências
RUN apt-get update && apt-get install -y \
    wget \
    unzip \
    curl \
    gnupg2 \
    procps \
 && rm -rf /var/lib/apt/lists/*

# Instalar Maven
RUN wget https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.zip \
 && unzip apache-maven-${MAVEN_VERSION}-bin.zip -d /opt \
 && ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven \
 && rm apache-maven-${MAVEN_VERSION}-bin.zip

# Instalar Tomcat
RUN wget https://archive.apache.org/dist/tomcat/tomcat-10/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz \
 && tar -xzf apache-tomcat-${TOMCAT_VERSION}.tar.gz -C /opt \
 && ln -s /opt/apache-tomcat-${TOMCAT_VERSION} /opt/tomcat \
 && rm apache-tomcat-${TOMCAT_VERSION}.tar.gz

# Ajusta permissões do catalina.sh para garantir que seja executável
RUN chmod +x /opt/tomcat/bin/catalina.sh

# Copia o projeto para dentro do container
COPY backend /app

# Compila a aplicação com Maven usando caminho absoluto para o Maven
RUN /opt/maven/bin/mvn -f /app/pom.xml clean package

# Move o .war gerado para o Tomcat (nomeando como ROOT.war)
RUN cp /app/target/*.war /opt/tomcat/webapps/backend.war

# Expõe a porta padrão do Tomcat
EXPOSE 8080

# Comando de inicialização
CMD ["/opt/tomcat/bin/catalina.sh", "run"]