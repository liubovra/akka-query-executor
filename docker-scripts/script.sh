#!/bin/bash

java -cp /etc/lib/*.jar:/etc/lib/*:. com.example.StartupApp jdbc:postgresql://hh-pgsql-public.ebi.ac.uk:5432/pfmegrnargs reader NWDMCE5xdipIjRrp org.postgresql.Driver 6