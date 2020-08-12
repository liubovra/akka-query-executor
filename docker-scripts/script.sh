#!/bin/bash

java -cp /etc/lib/*.jar:/etc/lib/*:. com.example.StartupApp $DB_IP $DB_USER $DB_PASSWORD $DB_DRIVER $LIMIT_SIZE