#!/bin/bash

REVISION=`svnversion`

FILES="src/openwig/Engine.java src/gui/Midlet.java"

for i in $FILES; do
	sed -i s/##REV##/$REVISION/ $i
done
