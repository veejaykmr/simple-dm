VERSION=0.5-SNAPSHOT
DIST=dist

rm -rf $DIST
mkdir -p $DIST/lib

cp -v core/target/sdm-core-$VERSION.jar $DIST
cp -v core/conf/sdm-config.groovy $DIST/lib

cp -v maven/provider/target/sdm-mvn-provider-$VERSION.jar $DIST/lib
cp -v maven/metadata/target/sdm-mvn-metadata-$VERSION.jar $DIST/lib

HOME=${USERPROFILE}

cp -v ${HOME}/.m2/repository/org/codehaus/groovy/groovy-all/1.7.2/groovy-all-1.7.2.jar $DIST/lib
cp -v ${HOME}/.m2/repository/org/apache/ivy/ivy/2.1.0/ivy-2.1.0.jar $DIST/lib
