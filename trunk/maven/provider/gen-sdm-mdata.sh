find /home/alex/.m2/repository -name '*.jar' -exec unzip -l {} \; | grep -i 'archive:\|class' > mappings.txt
rm ModuleMetadata*.groovy
groovy /home/alex/projects/sdm/maven/provider/src/org/sdm/maven/provider/MappingGenerator.groovy 
rm /home/alex/projects/sdm/maven/metadata/src/org/sdm/maven/metadata/ModuleMetadata*.groovy
cp -vf ModuleMetadata*.groovy /home/alex/projects/sdm/maven/metadata/src/org/sdm/maven/metadata/
