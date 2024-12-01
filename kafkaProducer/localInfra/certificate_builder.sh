#!/bin/bash

PASSWORD="changeit"                 # Keystore and Truststore password
DAYS=365                              # Certificate validity in days
HOSTNAME="localhost"                  # Hostname for Kafka server certificate
CA_CN="Kafka-Producer-Demo"           # Common Name for your CA
KEYSTORE="kafka.keystore.jks"  # Keystore filename
TRUSTSTORE="kafka.truststore.jks"  # Truststore filename
CLIENT_TRUSTSTORE="kafka.client.truststore.jks"

echo "### Step 1: Create own private Certificate Authority (CA)"
openssl req -new -newkey rsa:4096 -days $DAYS -x509 -subj "/CN=$CA_CN" -keyout ca-key -out ca-cert -nodes

sleep 1

echo "### Step 2: Create Kafka Server Certificate and store in KeyStore"
keytool -genkeypair \
  -keystore $KEYSTORE \
  -alias kafka-server \
  -keyalg RSA \
  -keysize 4096 \
  -validity $DAYS \
  -storepass $PASSWORD \
  -keypass $PASSWORD \
  -dname "CN=$HOSTNAME" \
  -storetype pkcs12

sleep 1
echo "### Step 2.1: Verify the server certificate"
keytool -list -v -keystore $KEYSTORE -storepass $PASSWORD

echo "### Step 3: Create Certificate Signing Request (CSR)"
keytool -keystore $KEYSTORE -certreq -file cert-file -alias kafka-server -storepass $PASSWORD -keypass $PASSWORD

echo "### Step 4: Sign the CSR with the CA"
openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file -out cert-file-signed -days $DAYS -CAcreateserial

echo "### Step 4.1: Verify the signed certificate"
keytool -printcert -v -file cert-file-signed

echo "### Step 5: Import CA certificate into Keystore"
keytool -keystore $KEYSTORE -alias CARoot -import -file ca-cert -storepass $PASSWORD -keypass $PASSWORD -noprompt

echo "### Step 6: Import Signed CSR into Keystore"
keytool -keystore $KEYSTORE -alias kafka-server -import -file cert-file-signed -storepass $PASSWORD -keypass $PASSWORD -noprompt

echo "### Step 7: Create and import CA certificate into TrustStore"
keytool -keystore $TRUSTSTORE -alias CARoot -import -file ca-cert -storepass $PASSWORD -keypass $PASSWORD -noprompt

echo "### Cleanup temporary files"
rm -f ca-key cert-file cert-file-signed ca-cert.srl

keytool -keystore $CLIENT_TRUSTSTORE -alias CARoot -import -file ca-cert -storepass $PASSWORD -keypass $PASSWORD -noprompt

mv $KEYSTORE server_certs/$KEYSTORE
mv $TRUSTSTORE server_certs/$TRUSTSTORE

echo "### All done!"
echo "Generated files:"
echo " - Keystore: $KEYSTORE"
echo " - Truststore: $TRUSTSTORE"
