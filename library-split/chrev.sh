#!/bin/bash

REVISION=`svnversion`

FILES="OpenWIGLibrary/src/cz/matejcik/openwig/Engine.java mobile/src/gui/Midlet.java"

for i in $FILES; do
	sed -i "s/String VERSION = \".*\";/String VERSION = \"$REVISION\";/" $i
done
