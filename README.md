This plugin has been deprecated, and future development will be focused on [che-incubator/che-workspace-telemetry-woopra-plugin](https://github.com/che-incubator/che-workspace-telemetry-woopra-plugin)

[![Master Build Status](https://ci.centos.org/buildStatus/icon?subject=master&job=devtools-che-workspace-telemetry-woopra-backend-build-master/)](https://ci.centos.org/view/Devtools/job/devtools-che-workspace-telemetry-woopra-backend-build-master/)
[![Nightly Build Status](https://ci.centos.org/buildStatus/icon?subject=nightly&job=devtools-che-workspace-telemetry-woopra-backend-che-nightly/)](https://ci.centos.org/view/Devtools/job/devtools-che-workspace-telemetry-woopra-backend-che-nightly/)

[![Contribute](https://camo.githubusercontent.com/7ca4f6be43fb5eb61a73ba6d40b3481d93ef5813/68747470733a2f2f6368652e6f70656e73686966742e696f2f666163746f72792f7265736f75726365732f666163746f72792d636f6e747269627574652e737667)](https://che.openshift.io/f?url=https://github.com/redhat-developer/che-workspace-telemetry-woopra-backend)

# che-workspace-telemetry-woopra-backend

## Prerequisites

This repo depends on packages in the [GitHub maven package registry](https://github.com/features/packages).
A [personal access token](https://github.com/settings/tokens) with `read:packages` access is required to pull down
dependencies from GitHub.

To compile a native image, you also need [GraalVM 19.2.1](https://www.graalvm.org/) and `native-image`
installed.

Add a repository entry in `$HOME/.m2/settings.xml`:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
   <servers>
      <server>
         <id>che-incubator</id>
         <username>YOUR GITHUB USERNAME</username>
         <password>YOUR PERSONAL ACCESS TOKEN</password>
      </server>
   </servers>

   <profiles>
      <profile>
         <id>github</id>
         <activation>
            <activeByDefault>true</activeByDefault>
         </activation>
         <repositories>
            <repository>
               <id>central</id>
               <url>https://repo1.maven.org/maven2</url>
               <releases><enabled>true</enabled></releases>
               <snapshots><enabled>false</enabled></snapshots>
            </repository>
            <repository>
               <id>che-incubator</id>
               <name>GitHub navikt Apache Maven Packages</name>
               <url>https://maven.pkg.github.com/che-incubator/che-workspace-telemetry-client</url>
            </repository>
         </repositories>
      </profile>
   </profiles>
</settings>
```

## Building

`mvn package -Pnative`

## Building the Docker Image

### If you have previously built the native binary with `mvn package -Pnative`:

`docker build -f src/main/docker/Dockerfile.native -t image-name:tag .`

### To use the multistage Dockerfile to build the GraalVM native image and docker image

The `Dockerfile` needs two build arguments:

+ `GITHUB_USERNAME` - the GitHub username you intend to use to build the native image.  This user should have an access token with `read:packages` permissions associated with it.
+ `GITHUB_TOKEN` - a GitHub Personal Access Token with `read:packages` permissions to pull down the necessary Maven dependencies.

Then pass in the build arguments:

`docker build --build-arg GITHUB_USERNAME=<github username> --build-arg GITHUB_TOKEN=<personal access token> -f src/main/docker/Dockerfile.multi -t image-name:tag .`

## Running Tests

`mvn verify`


## Publishing a new version of the plugin `meta.yaml` file

The plugin `meta.yaml` is hosted on a CDN at [static.developers.redhat.com](https://static.developers.redhat.com).  In order to push a new version, you will need the appropriate Akamai credential file, with the following layout:

```
[default]
key = key = <Secret key for the Akamai NetStorage account>
id = <NetStorage account ID>
group = <NetStorage storage group>
host = <NetStorage host>
cpcode = <NetStorage CPCode>
```

Save this file as `akamai-auth.conf`.

In the root of this repository, run:

```shell
docker run -w /root/app -v $(pwd):/root/app -v \
  /path/to/akamai-auth.conf:/root/.akamai-cli/.netstorage/auth \
  akamai/cli netstorage upload \
  --directory rh-che/plugins/che-workspace-telemetry-woopra-backend/0.0.1 \
  meta.yaml
```
