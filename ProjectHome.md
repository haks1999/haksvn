
# Haksvn #
  * simple web application for source release helper tool
  * using trunk source for development environment, branch or tag source for production environment
  * move trunk sources to branch by approval chain



# Introduction #
## Basic Transfer Flow ##
  * development source is in the Trunk, and production source is in the Branch. After releasing, production source will be copyed to Tags.
  * in haksvn application, it can be done by 'request > approve > transfer' process.

> ![http://haksvn.googlecode.com/svn/wiki/images/ProcessFlow.png](http://haksvn.googlecode.com/svn/wiki/images/ProcessFlow.png)

> ![http://haksvn.googlecode.com/svn/wiki/images/ProcessFlow_1.png](http://haksvn.googlecode.com/svn/wiki/images/ProcessFlow_1.png)

> ![http://haksvn.googlecode.com/svn/wiki/images/ProcessFlow_2.png](http://haksvn.googlecode.com/svn/wiki/images/ProcessFlow_2.png)

> ![http://haksvn.googlecode.com/svn/wiki/images/ProcessFlow_3.png](http://haksvn.googlecode.com/svn/wiki/images/ProcessFlow_3.png)


## Production & Development Source Code Management ##
  * Trunk for Development, Branches for Production
  * Approval process for transferring

> ![http://haksvn.googlecode.com/svn/wiki/images/TransferRequest.png](http://haksvn.googlecode.com/svn/wiki/images/TransferRequest.png)


## Source Browsing ##
  * browsing source code by web browser

> ![http://haksvn.googlecode.com/svn/wiki/images/SourceBrowse.png](http://haksvn.googlecode.com/svn/wiki/images/SourceBrowse.png)

> ![http://haksvn.googlecode.com/svn/wiki/images/SourceBrowseDetail.png](http://haksvn.googlecode.com/svn/wiki/images/SourceBrowseDetail.png)

## Code Review ##
  * check diff with revisions
  * give scores and leave comment

> ![http://haksvn.googlecode.com/svn/wiki/images/SourceChangesReview.png](http://haksvn.googlecode.com/svn/wiki/images/SourceChangesReview.png)

## Trace Transfer History ##
  * trace source transfer history by flow chart

> ![http://haksvn.googlecode.com/svn/wiki/images/TraceSource.png](http://haksvn.googlecode.com/svn/wiki/images/TraceSource.png)


# Getting Started #
> ## 1. Download & Start Application ##
    * checkout source code or download war/jar from here [download](https://drive.google.com/folderview?id=0BzfKf3nLzdtdWTVOUGVmNTZRNzA&usp=sharing).
    * check your java version is 1.6+
    * if you want to use haksvn-x.x.x.war, Tomcat version should be 6.x+.
    * if you want to use haksvn-x.x.x.jar, just exeute jar by below command
> > `java -jar haksvn-x.x.x.jar` (here is more options [Tomcat executable jar](http://tomcat.apache.org/maven-plugin-trunk/executable-war-jar.html))


> ## 2. Create Repository ##
    1. log in as administrator (default: admin//admin)
    1. go to 'Configuration > Repositories > Add' and create your repository.
    1. go to 'Configuration > Users > Add' and create users.
    1. go to 'Configuration > Repositories > Repository User' and add new users to the repository.

> ## 3. Create Transfer Request ##
    1. log in as commiter.
    1. go to 'Transfer > Request' and create your request.
    1. find and add sources for transfer.
    1. save and request.

> ## 4. Approve Transfer ##
    1. log in as reviewer.
    1. go to 'Transfer > Request' and find 'Request' state request.
    1. check conditions and approve it.

> ## 5. Transfer ##
    1. log in as reviewer.
    1. go to 'Transfer > Request Group' and create group.(only approved request could be added to group)
    1. save and transfer.

