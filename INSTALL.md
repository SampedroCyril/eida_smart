# How To Install this app step by step on Ubuntu Mate 20.04 :


## Set-up :

### Maven and JDK11 :

- Installing Maven on Ubuntu using apt is a simple, straightforward process.

- `$ sudo apt update` and `$ sudo apt install maven`.

- `$ sudo apt install default-jdk`.


### MySQL :

- Open terminal: We will begin with the installation of mysql server


- `$ sudo apt update` then `$ sudo apt upgrade`.

- `$ sudo apt install mysql-server`.

- `$ sudo systemctl status mysql`.

- `$ sudo mysql_secure_installation `.

- Tap 'y' for yes in all fields.

- Once it's done, select '0' for low password and create a new password. This password will grant you access to the MySQL shell and will be useful for the next steps.

- Enter your super user password twice and then tap again 'y' in the 4 fields.

- The MySQL server setup is now done, congrats! (If you need more information about it, just click on the following link : [**Link for installing MySQL-server**](https://linuxhint.com/install_mysql_ubuntu_2004/)).


### Tomcat9 : 

- To install Tomcat on Ubuntu, use this command : `$ sudo apt install tomcat9`.

- When you have installed Tomcat, disable the server whith those commands : `$ sudo systemctl disable tomcat9` and `$ sudo systemctl stop tomcat9`.

### GitHub :

- Now you can clone the repository which contains the project with : `git clone git@github.com:avenir-84-org/eida_SMART.git` (it has to be the ssh key).


## Run The Project :

- Now you can run the project by opening a command liner in your eida_Project repository, go in SMART folder with `cd SMART/`.

- `$ sudo mysql -p < init.sql`

- And now you just have to use this command to run the project : `$ ./mvnw spring-boot:run`.

- Open your navigator and in the url adress enter http://localhost:8080/

- And Voilà now your project is successfully Installed!

### Apache2 : (a corriger et voir plus tard)

- To install Apache2 on Ubuntu, use this command : `$ sudo apt install apache2`.

- We will grant access to Apache2 through the firewall on the 80 port with this command `$ sudo ufw allow 'Apache full'`.

- You can also check the status of apache2 with `$ sudo ufw status`.

- To make your server an HTTPS request you have to do some more steps, begin with : `$ sudo a2enmod proxy_http` and enter your password.

- After this you have to do this : `$ sudo a2enmod rewrite`.

- Next you will update the default configuration file of apache2 websites : `$ sudo nano /etc/apache2/sites-available/000-default.conf`.

- Delete all the content of 000-default.conf and replace it with: 
```
    <VirtualHost *:80>

        ServerName localhost
        DocumentRoot/var/www/html
                                                                                
        ProxyRequests Off
        ProxyPass / http://localhost:8080/
        ProxyPassReverse / http://localhost:8080/
                                                                                
    </VirtualHost>
```

- Tomcat must be configured to accept communications from a protocol named AJP. To do this, you have to modify your configuration file : `$ sudo nano -l /etc/tomcat9/server.xml`.

- Delete comment tags from lines 116 and 121, and replace line 118 with : `secretRequired="false"`.

- It is necessary to restart the servers for the new configurations to be taken into account : `$ sudo systemctl restart tomcat9`, `$ sudo systemctl restart apache2`.

- To generate a self-signed certificate, run the following command : `$ sudo openssl req -x509 -nodes -days 365 -newkey rsa:4096 -keyout/etc/ssl/private/apache-selfsigned.key -out /etc/ssl/certs/apache-selfsigned.crt`.

- The next step is to edit the apache configuration file and duplicate the virtualhost block to add the same block for port 443 with a few more lines (over the lines we added just before) :

```
    <VirtualHost *:443>

        ServerName localhost
        DocumentRoot /var/www/html

        ProxyRequests Off
        ProxyPass / http://localhost:8080/
        ProxyPassReverse / http://localhost:8080/

        SSLEngine on
        SSLCertificateFile /etc/ssl/certs/apache-selfsigned.crt
        SSLCertificateKeyFile /etc/ssl/private/apache-selfsigned.key

    </VirtualHost>

    <VirtualHost *:80>

        ServerName localhost
        DocumentRoot /var/www/html

        ProxyRequests Off
        ProxyPass / http://localhost:8080/
        ProxyPassReverse / http://localhost:8080/

    </VirtualHost>
```               

- After activating the SSL module and restarting the apache server with the following commands, you should be able to access the HTTPS homepage at https://box.local/ : `$ sudo a2enmod ssl` , `$ sudo systemctl restart apache2`.


- Finally, you only have to modify 000-default.conf to automatically redirect an HTTP request to the corresponding HTTPS request :
```
    <VirtualHost *:443>

        ServerName localhost
        DocumentRoot /var/www/html

        ProxyRequests Off
        ProxyPass / http://localhost:8080/
        ProxyPassReverse / http://localhost:8080/

        SSLEngine on
        SSLCertificateFile /etc/ssl/certs/apache-selfsigned.crt
        SSLCertificateKeyFile /etc/ssl/private/apache-selfsigned.key

    </VirtualHost>


    <VirtualHost *:80>

        Redirect permanent / https://box.local/

    </VirtualHost>
```
    
- After a last reboot of Apache it should no longer be possible to connect to http://box.local without being automatically redirected to https://box.local.

