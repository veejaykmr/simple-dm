rm -rf dist
mkdir -p dist/lib

cp -v core/target/sdm-core-1.0-SNAPSHOT.jar dist

cp -v maven/provider/target/sdm-maven-provider-1.0-SNAPSHOT.jar dist/lib
cp -v maven/metadata/target/sdm-maven-metadata-1.0-SNAPSHOT.jar dist/lib

cp -v ~/.m2/repository/org/codehaus/groovy/groovy-all/1.7.2/groovy-all-1.7.2.jar dist/lib
cp -v ~/.m2/repository/org/apache/ivy/ivy/2.1.0/ivy-2.1.0.jar dist/lib
