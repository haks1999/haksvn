# haksvn
Automatically exported from code.google.com/p/haksvn


# Haksvn #
  * simple web application for source release helper tool
  * using trunk source for development environment, branch or tag source for production environment
  * move trunk sources to branch by approval chain



# Introduction #
## Basic Transfer Flow ##
  * development source is in the Trunk, and production source is in the Branch. After releasing, production source will be copyed to Tags.
  * in haksvn application, it can be done by 'request > approve > transfer' process.

> ![https://github.com/haks1999/haksvn/blob/wiki/%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4%20%EC%A0%95%EB%A6%AC.png](https://github.com/haks1999/haksvn/blob/wiki/%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4%20%EC%A0%95%EB%A6%AC.png)
> ![https://github.com/haks1999/haksvn/blob/wiki/%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4%20%EC%A0%95%EB%A6%AC%20(1).png
](https://github.com/haks1999/haksvn/blob/wiki/%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4%20%EC%A0%95%EB%A6%AC%20(1).png
)
> ![https://github.com/haks1999/haksvn/blob/wiki/%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4%20%EC%A0%95%EB%A6%AC%20(2).png
](https://github.com/haks1999/haksvn/blob/wiki/%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4%20%EC%A0%95%EB%A6%AC%20(2).png
)
> ![https://github.com/haks1999/haksvn/blob/wiki/%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4%20%EC%A0%95%EB%A6%AC%20(3).png
](https://github.com/haks1999/haksvn/blob/wiki/%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4%20%EC%A0%95%EB%A6%AC%20(3).png
)


## Production & Development Source Code Management ##
  * Trunk for Development, Branches for Production
  * Approval process for transferring

> ![https://github.com/haks1999/haksvn/blob/wiki/TransferRequest.png](https://github.com/haks1999/haksvn/blob/wiki/TransferRequest.png)


## Source Browsing ##
  * browsing source code by web browser

> ![https://github.com/haks1999/haksvn/blob/wiki/SourceBrowse.png](https://github.com/haks1999/haksvn/blob/wiki/SourceBrowse.png)

> ![https://github.com/haks1999/haksvn/blob/wiki/SourceBrowseDetail.png](https://github.com/haks1999/haksvn/blob/wiki/SourceBrowseDetail.png)

## Code Review ##
  * check diff with revisions
  * give scores and leave comment

> ![https://github.com/haks1999/haksvn/blob/wiki/SourceChangesReview.png](https://github.com/haks1999/haksvn/blob/wiki/SourceChangesReview.png)


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
