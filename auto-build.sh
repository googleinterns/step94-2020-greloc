 #!/bin/bash

# Argument validation
if [ $# -gt 0 ] && [ "$1" != "r" ] && [ "$1" != "d" ]; then
    echo "argument $1 is invalid"
    exit
fi

# Remove old distribution files from appengine deploy folder
rm -r src/main/webapp/dashboard/*

# Move to vue directory
cd vue-dash

# Build vue project
npm run build

# Copy the new distributino files from /dist to /webapp
cp -a dist/. ../src/main/webapp/dashboard

# Move back to main directory
cd ..

if [ $# -eq 0 ]; then
    echo "No argument provided, will not run or deploy"
    exit
elif [ "$1" = "r" ]; then
    mvn package appengine:run
elif [ "$1" = "d" ]; then
    mvn package appengine:deploy
fi