[![Build Status](https://travis-ci.org/sam-lewis/github-issue-exporter.svg?branch=master)](https://travis-ci.org/sam-lewis/github-issue-exporter)
github-issue-exporter
=====================

A groovy github issue exporter which writes to a CSV.

Building
--------

`gradlew clean distZip` 


Running
--------
`cd build\distributions`

`unzip github-export-1.0.zip`

`cd github-export-1.0/bin`

`github-export <username> <password> <repo>`
