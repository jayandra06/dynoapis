# DAMS
Dyno APIs Webhook Server

Before starting, Please configure the db. Currently the db is set to postgres.
You can configure the db to mysql or postgres. In order to configure, please navigate to src->main->resources->application.properties

Minimum Java JDK required is Java 1.8
After configuring, You can build a jar file and run it from the command line (it should work just as well with Java 11 or newer)
Use the below command to start the server on port 8080
```
./mvnw package
java -jar target/*.jar
```
Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```

Steps for Deploying to Ubuntu 22

Update Ubuntu Packages

```sudo apt update```

Check if java is already installed

```java --version```


If Java is not currently installed, you’ll get the following output:

Output
Command 'java' not found, but can be installed with:

```
sudo apt install default-jre              # version 2:1.11-72build1, or
sudo apt install openjdk-11-jre-headless  # version 11.0.14+9-0ubuntu2
sudo apt install openjdk-17-jre-headless  # version 17.0.2+8-1
sudo apt install openjdk-18-jre-headless  # version 18~36ea-1
sudo apt install openjdk-8-jre-headless   # version 8u312-b07-0ubuntu1
```

Install default-jdk
```
sudo apt install default-jdk
```

Install Postgresql and configure DB
```
sudo apt install python3-pip python3-dev libpq-dev postgresql postgresql-contrib maven nginx

sudo -u postgres psql

CREATE DATABASE dams_db;

CREATE USER damsweb WITH PASSWORD 'password';

ALTER ROLE damsweb SET client_encoding TO 'utf8';

ALTER ROLE damsweb SET default_transaction_isolation TO 'read committed';

ALTER ROLE damsweb SET timezone TO 'UTC';

GRANT ALL PRIVILEGES ON DATABASE dams_db TO damsweb;

\q
```

Update your ``application.properties`` file with the db name, user and password

Copy/clone this repo to ``cd /home/user/``

Build the jar using the below command
```agsl
mvn clean install -DskipTests
```

Once the build is successful, create a service with the below code at ``/etc/systemd/system/damsservice.service``

```agsl
[Unit]
Description=Dams Server
After=syslog.target
After=network.target[Service]
User=user
Type=simple

[Service]
ExecStart=/usr/bin/java -jar /home/user/dams/target/dams-0.0.1-SNAPSHOT.jar
Restart=always
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=damsservice

[Install]
WantedBy=multi-user.target
```

Start the service
```agsl
sudo systemctl start damsservice
```

Verify the status of the service
```agsl
sudo systemctl status damsservice
```

Configure Nginx and proxy to the default springboot app running on port 8080
```agsl
sudo systemctl status nginx
sudo systemctl enable nginx

sudo ufw allow OpenSSH
sudo ufw allow in "Nginx Full"
sudo ufw enable

sudo ufw status
```

Navigate to ``/etc/nginx/sites-available`` and create a new file called damsapp with the below code
```
server {
        listen 80;
        listen [::]:80;

        server_name <ip_address or domain_name>;

        location / {
            proxy_pass http://localhost:8080/;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
            proxy_set_header X-Forwarded-Port $server_port;
        }
}
```
Link this file to sites-enabled conf by executing the below line
```
sudo ln -s /etc/nginx/sites-available/damsapp /etc/nginx/sites-enabled/
```

Unlink the default conf 
```
sudo unlink /etc/nginx/sites-enabled/default
```

Test to ensure nginx doesn't throw any errors
```
sudo nginx -t
```

Restart nginx server
```
sudo systemctl restart nginx
```
